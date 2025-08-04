// Pet.java
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pet {
    private String petId;
    private String name;
    private String species;
    private int age;
    private String ownerName;
    private String contactInfo;
    private LocalDate registrationDate;
    private List<Appointment> appointments;

    public Pet(String petId, String name, String species, int age, String ownerName, String contactInfo, LocalDate registrationDate) {
        this.petId = petId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
        this.registrationDate = registrationDate;
        this.appointments = new ArrayList<>();
    }
public String getPetId() {
        return petId;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public int getAge() {
        return age;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public boolean hasVetVisitInLast6Months() {
        return appointments.stream()
            .anyMatch(a -> a.getType().equalsIgnoreCase("vet visit") &&
                    a.getDateTime().toLocalDate().isAfter(LocalDate.now().minusMonths(6)));
    }

    @Override
    public String toString() {
        return petId + ": " + name + " (" + species + ", Age: " + age + ") - Owner: " + ownerName;
    }
}