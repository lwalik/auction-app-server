package pl.wit;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NewAuctionAppServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9001); // Port serwera

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Tworzenie i wypełnienie mapy obiektów
                Map<Integer, Product> data = createData();

                // Przesyłanie mapy obiektów do klienta
                sendMapToClient(data, clientSocket);

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Integer, Product> createData() {
        // Tworzenie i wypełnienie mapy obiektów
        Map<Integer, Product> data = new HashMap<>();
        data.put(1, new Product(1, "Product 1", 100.00, 500.00, "/images/pobrane.png"));
        data.put(2, new Product(2,"Product 2", 85.00, 170.00, "/images/apple-iphone-xs.jpg"));
        data.put(3, new Product(3,"Product 3", 15.00, 45.00,  "/images/apple-iphone-xs.jpg"));
        return data;
    }

    private static void sendMapToClient(Map<Integer, Product> data, Socket clientSocket) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

        // Przesyłanie mapy obiektów do klienta
        outputStream.writeObject(data);

        outputStream.close();
    }
}
