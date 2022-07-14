package ui.Menu;

import java.io.IOException;
import java.sql.SQLException;

public class MainMenu extends Menu {


    @Override
    public void print() {
        System.out.println("************************************************");
        System.out.println("* 1. continue with employees                   *");
        System.out.println("* 2. continue with departments                 *");
        System.out.println("* 3. continue with cases                       *");
        System.out.println("* 4. continue with places of investigation     *");
        System.out.println("* 5. continue with punishments                 *");
        System.out.println("* 6. exit                                      *");
        System.out.println("************************************************");
    }

    @Override
    public void handle(String option) throws IOException, SQLException {
        Menu m;
        switch (option) {
            case "1":   m = new EmployeeMenu(); m.run(); break;
            case "2":   m = new DepartmentMenu(); m.run(); break;
            case "3":   m = new CaseMenu(); m.run(); break;
            case "4":   m = new PlacesMenu(); m.run(); break;
            case "5":   m = new PunishmentsMenu(); m.run(); break;
            case "6":   exit(); break;
            default:    System.out.println("Unknown option"); break;
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        MainMenu mainMenu = new MainMenu();
        mainMenu.run();
    }
}
