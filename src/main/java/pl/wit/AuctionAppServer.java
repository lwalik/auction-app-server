package pl.wit;

import utills.UniqueIdGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class AuctionAppServer {
    private static final int PORT = 9001;
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    private static Map<Integer, Product> products =  new HashMap();
//    private ServerSocket serverSocket;
    private Set<Socket> connectedClients = new HashSet<>();

    public AuctionAppServer() {
        this.initProducts();
    }

    public static void main(String[] args) throws Exception {
        AuctionAppServer server = new AuctionAppServer();
        server.start();
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");
            while (true) {
                    Socket clientSocket = serverSocket.accept();
                    connectedClients.add(clientSocket);
                    System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                    // Create a new Handler for each client
                    Handler handler = new Handler(clientSocket);
                    Thread clientThread = new Thread(handler);
                    clientThread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void initProducts() {
        this.products.put(1, new Product(1, "Product 1", 100.00, 500.00, "/images/pobrane.png"));
        this.products.put(2, new Product(2,"Product 2", 85.00, 170.00, "/images/apple-iphone-xs.jpg"));
        this.products.put(3, new Product(3,"Product 3", 15.00, 45.00,  "/images/apple-iphone-xs.jpg"));
    }


    private void removeClient(Socket clientSocket) {
        connectedClients.remove(clientSocket);
        System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
    }

    public class Handler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private static final long HEARTBEAT_INTERVAL = 5000;
//        private ProductStorage productStorage;
        private BufferedImage image;

        private class UpdateDataTask extends TimerTask {
            @Override
            public void run() {
                updateData();

                System.out.println("UpdateDataTask func");
                System.out.println("Klientów: " + connectedClients.size());
//             Wysłanie zaktualizowanych danych do wszystkich klientów
                for (Socket client : connectedClients) {
                    System.out.println("Dane wysłane do: " + client.getInetAddress().getHostAddress());
                    sendClientData();
                }
            }
        }

        public Handler(Socket socket) throws IOException {
            this.clientSocket = socket;
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
//            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Timer timer = new Timer();
            timer.schedule(new UpdateDataTask(), 5000, 10000);
        }

        public void run() {
            try {
                sendClientData();
                boolean isConnected = true;

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                while (isConnected) {

                        // Odczytanie danych od klienta
                        String receivedData = reader.readLine();
                        // Wykonanie odpowiednich operacji na otrzymanych danych
                        // ...
                        // Przykład warunku wyjścia z pętli
                        if (receivedData == null) {
                            closeConnection();
                            isConnected = false;
                            break;
                        }


                    try {
                        int readByte = clientSocket.getInputStream().read();
                        if (readByte == -1) {
                            // Klient rozłączył się
                            isConnected = false;
                            break;
                        }
                    } catch (IOException e) {
                        // Obsługa błędu we/wy
                        isConnected = false;
                        break;
                    }
                }
            } catch (IOException  e) {
                // Obsługa błędu we/wy
                System.out.println(e);
            }
            finally {
                closeConnection();
                removeClient(clientSocket);
            }

        }


        private void sendClientData() {
            try {
                outputStream.writeObject(products);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void closeConnection() {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
                if (clientSocket != null)
                    clientSocket.close();
            } catch (IOException e) {
                System.out.println("Klient rozłączony: " + clientSocket.getInetAddress().getHostAddress());
                e.printStackTrace();
            }
        }
    }

    private void updateData() {
        // Aktualizacja danych...
        Map<Integer, Product> newProducts = new HashMap<>();

        newProducts.put(1, new Product(1, "Product 55", 150.00, 500.00, "/images/pobrane.png"));
        newProducts.put(2, new Product(2,"Product 100", 85.00, 1000.99, "/images/apple-iphone-xs.jpg"));

        products = newProducts;
    }
}
