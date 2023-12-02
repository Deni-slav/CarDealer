package CarDealer;

import CarDealer.Car;

import java.io.*;
import java.net.*;

public class CarDealerClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Buy a car");
                System.out.println("2. Evaluate your car");
                System.out.println("3. Sell your car");
                System.out.println("4. Exit");

                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        buyCar(out, in, reader);
                        break;
                    case 2:
                        evaluateCar(out, in, reader);
                        break;
                    case 3:
                        sellCar(out, in, reader);
                        break;
                    case 4:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static void buyCar(ObjectOutputStream out, ObjectInputStream in, BufferedReader reader) throws IOException, ClassNotFoundException {
        System.out.println("Enter the car model you want to buy (e.g., 'Toyota Corolla'):");
        String carModel = reader.readLine();
        out.writeObject(carModel);
        Object response = in.readObject();
        System.out.println("Response from server: " + response);
    }
    private static void evaluateCar(ObjectOutputStream out, ObjectInputStream in, BufferedReader reader) throws IOException, ClassNotFoundException {
        System.out.println("Enter the details of your car for evaluation:");
        System.out.print("Enter the brand: ");
        String brand = reader.readLine();
        System.out.print("Enter the model: ");
        String model = reader.readLine();
        System.out.print("Enter the price: ");
        double price = Double.parseDouble(reader.readLine());
        Car userCar = new Car(brand, model, price);
        out.writeObject(userCar);
        Object response = in.readObject();
        System.out.println("Car evaluation from server: " + response);
    }
    private static void sellCar(ObjectOutputStream out, ObjectInputStream in, BufferedReader reader) throws IOException, ClassNotFoundException {
        System.out.println("Enter the details of your car for selling:");
        System.out.print("Enter the brand: ");
        String brand = reader.readLine();
        System.out.print("Enter the model: ");
        String model = reader.readLine();
        System.out.print("Enter the price: ");
        double price = Double.parseDouble(reader.readLine());
        Car userCar = new Car(brand, model, price);
        out.writeObject(userCar);
        Object response = in.readObject();
        System.out.println("Response from server: " + response);
    }
}
