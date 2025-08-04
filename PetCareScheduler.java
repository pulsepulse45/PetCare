// PetCareScheduler.java
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PetCareScheduler {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Pet> pets = new HashMap<>();
    private static final String DATA_FILE = "pet_data.txt";

    public static void main(String[] args) {
        loadData();
        while (true) {
            System.out.println("\n1. Register Pet\n2. Schedule Appointment\n3. Store Data\n4. Display Records\n5. Generate Reports\n6. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> registerPet();
                case 2 -> scheduleAppointment();
                case 3 -> storeData();
                case 4 -> displayRecords();
                case 5 -> generateReports();
                case 6 -> System.exit(0);
                default -> System.out.println("Invalid option.");
            }
        }
    }private static void registerPet() {
        System.out.print("Pet ID: ");
        String id = scanner.nextLine();
        if (pets.containsKey(id)) {
            System.out.println("Pet ID already exists.");
            return;
        }
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Species: ");
        String species = scanner.nextLine();
        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Owner Name: ");
        String owner = scanner.nextLine();
        System.out.print("Contact Info: ");
        String contact = scanner.nextLine();
        pets.put(id, new Pet(id, name, species, age, owner, contact, LocalDate.now()));
        System.out.println("Pet registered.");
    }private static void scheduleAppointment() {
        System.out.print("Pet ID: ");
        String id = scanner.nextLine();
        Pet pet = pets.get(id);
        if (pet == null) {
            System.out.println("Pet not found.");
            return;
        }
        System.out.print("Appointment Type: ");
        String type = scanner.nextLine();
        System.out.print("DateTime (yyyy-MM-dd HH:mm): ");
        String dt = scanner.nextLine();
        System.out.print("Notes: ");
        String notes = scanner.nextLine();
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            if (dateTime.isBefore(LocalDateTime.now())) {
                System.out.println("Appointment must be in future.");
                return;
            }
            pet.addAppointment(new Appointment(type, dateTime, notes));
            System.out.println("Appointment added.");
        } catch (Exception e) {
            System.out.println("Invalid datetime format.");
        }
    }
    private static void storeData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(pets);
            System.out.println("Data saved.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            pets = (Map<String, Pet>) in.readObject();
        } catch (Exception ignored) {}
    }

    private static void displayRecords() {
        System.out.println("\n1. All Pets\n2. Appointments by Pet\n3. Upcoming Appointments\n4. Past Appointments");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> pets.values().forEach(System.out::println);
            case 2 -> {
                System.out.print("Pet ID: ");
                String id = scanner.nextLine();
                Pet pet = pets.get(id);
                if (pet != null) pet.getAppointments().forEach(System.out::println);
                else System.out.println("Pet not found.");
            }
            case 3 -> showUpcomingAppointments();
            case 4 -> showPastAppointments();
        }
    }

    private static void showUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        pets.values().forEach(pet ->
            pet.getAppointments().stream()
                .filter(a -> a.getDateTime().isAfter(now))
                .forEach(a -> System.out.println(pet.getName() + " -> " + a))
        );
    }

    private static void showPastAppointments() {
        LocalDateTime now = LocalDateTime.now();
        pets.values().forEach(pet ->
            pet.getAppointments().stream()
                .filter(a -> a.getDateTime().isBefore(now))
                .forEach(a -> System.out.println(pet.getName() + " -> " + a))
        );
    }

    private static void generateReports() {
        System.out.println("\n--- Upcoming in next 7 days ---");
        LocalDateTime now = LocalDateTime.now();
        pets.values().forEach(pet ->
            pet.getAppointments().stream()
                .filter(a -> {
                    long days = Duration.between(now, a.getDateTime()).toDays();
                    return days >= 0 && days <= 7;
                })
                .forEach(a -> System.out.println(pet.getName() + " -> " + a))
        );

        System.out.println("\n--- Pets overdue for vet visit (6+ months) ---");
        pets.values().stream()
            .filter(p -> !p.hasVetVisitInLast6Months())
            .forEach(System.out::println);
    }
}
