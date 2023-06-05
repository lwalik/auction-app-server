package pl.wit;

import utills.UniqueIdGenerator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AuctionAppServer {
    private static final int PORT = 9001;
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

//    private UniqueIdGenerator generator =  new UniqueIdGenerator();
    private static Map<Integer, Product> products =  new HashMap();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private void initProducts() {
        this.products.put(1, new Product(1, "Product 1", 100.00, 500.00, "/images/pobrane.png"));
        this.products.put(2, new Product(2,"Product 2", 85.00, 170.00, "/images/apple-iphone-xs.jpg"));
        this.products.put(3, new Product(3,"Product 3", 15.00, 45.00,  "/images/apple-iphone-xs.jpg"));
    }

//    private static Map<Integer, Product> getProducts() {
//        return productStorage.getAll();
//    }

    public static class Handler extends Thread {
        private String name;
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private AuctionAppServer server;
        private ObjectOutputStream outObject;
//        private ProductStorage productStorage;

        public Handler(Socket socket) {
            this.socket = socket;
            this.server = new AuctionAppServer();
            server.initProducts();
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                outObject = new ObjectOutputStream(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                out.println("Dane:");
                outObject.writeObject(products);
                outObject.flush();

                writers.add(out);

                while (true) {
                    for (PrintWriter writer: writers) {
                        writer.println("Done");

                    }
                }


            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }

                try {
                    outObject.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
