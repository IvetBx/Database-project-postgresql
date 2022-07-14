package ui.Print;

import rdg.PlacesInCity.Address;
import rdg.PlacesInCity.Place;
import rdg.PlacesInCity.PlaceFinder;

import java.sql.SQLException;

public class PrintAddress {

    private static final PrintAddress INSTANCE = new PrintAddress();

    public static PrintAddress getInstance() { return INSTANCE; }

    private PrintAddress() { }

    public void print(Address a) throws SQLException {
        if (a == null) {
            throw new NullPointerException("id cannot be null");
        }
        try {
            System.out.print(a.getStreet());
            System.out.print(" in ");
            Place p = PlaceFinder.getInstance().findById(a.getPlace_id());
            System.out.print(p.getName_city_district());
            System.out.print("(Id: ");
            System.out.println(a.getId() + ")");
        } catch (NullPointerException ex){
            System.out.println("city of district doesn't exist");
        }
    }

}
