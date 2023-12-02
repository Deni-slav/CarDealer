package CarDealer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class CarDealerServer {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 3;

    private List<Car> carsForSale;
    private List<ClientHandler> clientHandlers;

    public CarDealerServer() {
        carsForSale = new ArrayList<>();
        clientHandlers = new ArrayList<>();
        initializeCars();
    }

    private void initializeCars() {
        carsForSale.add(new Car("Toyota", "Corolla", 20000.0));
        carsForSale.add(new Car("Honda", "Civic", 18000.0));
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Car Dealer Server is running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                if (clientHandlers.size() < MAX_CLIENTS) {
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clientHandlers.add(clientHandler);
                    new Thread(clientHandler).start();
                } else {
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    out.writeObject("Server is busy. Please try again later.");
                    out.close();
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.out = new ObjectOutputStream(clientSocket.getOutputStream());
                this.in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Object clientRequest = in.readObject();
                    if (clientRequest instanceof String) {
                        handleStringRequest((String) clientRequest, out);
                    } else if (clientRequest instanceof Car) {
                        handleCarRequest((Car) clientRequest, out);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                clientHandlers.remove(this);
            }
        }
        private void handleStringRequest(String request, ObjectOutputStream out) throws IOException {
        }

        private void handleCarRequest(Car car, ObjectOutputStream out) throws IOException, ClassNotFoundException {
            double calculatedPrice = calculateCarPrice(car);

            out.writeObject(calculatedPrice);
            out.writeObject("Do you want to sell your car? (yes/no)");
            String sellOption = (String) in.readObject();

            if ("yes".equalsIgnoreCase(sellOption)) {
                carsForSale.add(car);
                out.writeObject(calculatedPrice);
            } else {
                out.writeObject("Car not added to the list for sale.");
            }
        }

        private double calculateCarPrice(Car car) {
            return car.getPrice() * 1.1;
        }
    }

    public static void main(String[] args) {
        new CarDealerServer().startServer();
    }
}
