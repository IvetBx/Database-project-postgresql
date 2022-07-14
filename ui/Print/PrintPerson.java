package ui.Print;

import rdg.Person.Person;
import rdg.PlacesInCity.Address;
import rdg.PlacesInCity.AddressFinder;

import java.sql.SQLException;

public class PrintPerson {

    private static final PrintPerson INSTANCE = new PrintPerson();

    public static PrintPerson getInstance() { return INSTANCE; }

    private PrintPerson() { }

    public void print(Person person) throws SQLException {
        if (person == null) {
            throw new NullPointerException("person cannot be null");
        }

        System.out.print("Id: ");
        System.out.print(person.getId());
        System.out.print(" Name: ");
        System.out.print(person.getFirst_name());
        System.out.print(person.getLast_name());
        System.out.print(" Date of birth : ");
        System.out.print(person.getDate_of_birth());
        System.out.print(" Tel.number : ");
        System.out.print(person.getTel_number());
        System.out.print(" Address: ");
        Address a = AddressFinder.getInstance().findById(person.getAddress_id());
        PrintAddress.getInstance().print(a);
    }

}
