package pl.wit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class AuctionAppServer {
    private static final int PORT = 9001;
    private static Map<Integer, Product> products;
    private static final HashSet<ObjectOutputStream> writers = new HashSet<ObjectOutputStream>();
    private static final HashSet<String> names = new HashSet<String>();

    public AuctionAppServer() {
        ProductStorage productStorage = new ProductStorage();
        products = productStorage.getAll();
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

                // Create a new Handler for each client
                Handler handler = new Handler(clientSocket);
                Thread clientThread = new Thread(handler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class Handler extends Thread {
        private final Socket clientSocket;
        private ObjectOutputStream outputStream;
        private BufferedReader reader;
        private String name;


        public Handler(Socket socket) throws IOException {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                    name = reader.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        name = generateUniqueNameWithRandomId(name);
                        names.add(name);
                        break;
                    }
                }

                System.out.println("New client connected: " + name);
                writers.add(outputStream);
                System.out.println("Klientów: " + writers.size());
                sendClientData();

                while (true) {
                    String receivedData = reader.readLine();
                    if (receivedData == null) {
                        return;
                    }

                    if (receivedData.startsWith("accepted")) {
                        updateData();
                    }

                    for (ObjectOutputStream writer : writers) {
                        System.out.println("Dane wysłane do: " + clientSocket.getInetAddress().getHostAddress());
                        writer.writeObject(products);
                        writer.flush();
                    }

                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                closeConnection();
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
                    writers.remove(outputStream);
                if (clientSocket != null)
                    clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert clientSocket != null;
                System.out.println("Klient rozłączony: " + name);
            }
        }

        private void updateData() {
            Map<Integer, Product> newProducts = new HashMap<>();

            newProducts.put(1, new Product(1, "Product 55", 150.00, 500.00, "/images/pobrane.png"));
            newProducts.put(2, new Product(2, "Product 100", 85.00, 1000.99, "/images/apple-iphone-xs.jpg"));

            products = newProducts;
        }
    }

    public static String generateUniqueNameWithRandomId(String name) {
        Random random = new Random();
        String nameWithId;
        do {
            nameWithId = name + "#" + (random.nextInt(900) + 100);
        } while (names.contains(nameWithId));

        return nameWithId;
    }
}
