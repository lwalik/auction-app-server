package pl.wit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class AuctionAppServer {
    private static final int PORT = 9001;
    private static Map<Integer, Product> products;
    private static final HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static final HashSet<String> names = new HashSet<>();
    private static final ProductStorage productStorage = new ProductStorage();


    public static void main(String[] args) {
        AuctionAppServer server = new AuctionAppServer();
        server.start();

    }

    public void start() {
        products = productStorage.getAll();
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
        private ObjectInputStream inputStream;
        private BufferedReader reader;
        private String name;

        public Handler(Socket socket) throws IOException {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
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
                sendProductToWriters(StatusCode.OK);
                while (true) {
                    Request request = (Request) inputStream.readObject();
                    if (request == null) {
                        return;
                    }


                    if (request.getMethod().equals("POST")) {
                        if (request.getMessage().equals("BID")) {
                            System.out.println("Aktualizacja Trwa....");
                            System.out.println("Nowa cena: " + request.getProduct().getCurrPrice() + " dla produkty: " + request.getProduct().getName());
                            productStorage.updateData(request.getProduct());
                            products = productStorage.getAll();
                            sendProductToWriters(StatusCode.UPDATED);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e);
            } finally {
                closeConnection();
            }
        }

        private void sendProductToWriters(StatusCode statusCode) throws IOException {
            for (ObjectOutputStream writer : writers) {
                Response response = new Response(statusCode.getCode(), products);
                System.out.println("Dane wysłane do: " + clientSocket.getInetAddress().getHostAddress());
                writer.writeObject(response);
                writer.flush();
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
