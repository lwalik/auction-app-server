package pl.wit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class AuctionAppServer {
    private static final int PORT = 9001;
    private static Map<Integer, Product> products;
    private final Set<Socket> connectedClients = new HashSet<>();

    public AuctionAppServer() {
        ProductStorage productStorage = new ProductStorage();
        products = productStorage.getAll();
    }

    public static void main(String[] args) {
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

    private void removeClient(Socket clientSocket) {
        connectedClients.remove(clientSocket);
    }

    public class Handler implements Runnable {
        private final Socket clientSocket;
        private final ObjectOutputStream outputStream;

        private class UpdateDataTask extends TimerTask {
            @Override
            public void run() {
                updateData();

                System.out.println("UpdateDataTask func");
                System.out.println("Klientów: " + connectedClients.size());
                // Wysłanie zaktualizowanych danych do wszystkich klientów
                for (Socket client : connectedClients) {
                    System.out.println("Dane wysłane do: " + client.getInetAddress().getHostAddress());
                    sendClientData();
                }
            }
        }

        public Handler(Socket socket) throws IOException {
            this.clientSocket = socket;
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            Timer timer = new Timer();
            timer.schedule(new UpdateDataTask(), 5000, 10000);
        }

        public void run() {
            try {
                sendClientData();
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                        String receivedData = reader.readLine();

                        if (receivedData == null) {
                            closeConnection();
                            break;
                        }
                }
            } catch (IOException  e) {
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
                if (outputStream != null)
                    outputStream.close();
                if (clientSocket != null)
                    clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert clientSocket != null;
                System.out.println("Klient rozłączony: " + clientSocket.getInetAddress().getHostAddress());
            }
        }

        private void updateData() {
            Map<Integer, Product> newProducts = new HashMap<>();

            newProducts.put(1, new Product(1, "Product 55", 150.00, 500.00, "/images/pobrane.png"));
            newProducts.put(2, new Product(2,"Product 100", 85.00, 1000.99, "/images/apple-iphone-xs.jpg"));

            products = newProducts;
        }
    }
}
