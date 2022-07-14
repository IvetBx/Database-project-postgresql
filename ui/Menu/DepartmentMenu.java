package ui.Menu;

import rdg.Department.Department;
import rdg.Department.DepartmentFinder;
import ui.Print.PrintDepartment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class DepartmentMenu extends Menu {

    @Override
    public void print() {
        System.out.println("************************************************");
        System.out.println("* 1. list of all departments                   *");
        System.out.println("* 2. add new department                        *");
        System.out.println("* 3. edit department                           *");
        System.out.println("* 4. delete department                         *");
        System.out.println("* 5. exit                                      *");
        System.out.println("************************************************");
    }

    @Override
    public void handle(String option) throws IOException, SQLException {
        switch (option) {
            case "1":   listAllDepartments(); break;
            case "2":   addDepartment(); break;
            case "3":   editDepartment(); break;
            case "4":   deleteDepartment(); break;
            case "5":   exit(); MainMenu m = new MainMenu(); m.run(); break;
            default:    System.out.println("Unknown option"); break;
        }
    }


    private void listAllDepartments() throws SQLException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter number of page:");
        int page = Integer.parseInt(br.readLine());

        List<Department> dep = DepartmentFinder.getInstance().findAll(page);

        for (Department d : dep) {
            PrintDepartment.getInstance().print(d);
        }
    }

    private void addDepartment() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter department name:");
        String name = br.readLine();
        System.out.println("Enter manager_id:");
        int managerId = Integer.parseInt(br.readLine());

        try{
            Department d = Department.addDepartment(name, managerId);
            System.out.println("The department has been sucessfully added");
            System.out.print("The department's id is: ");
            System.out.println(d.getId());
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        } catch (IllegalStateException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void editDepartment() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter department name:");
        String name = br.readLine();
        System.out.println("Enter new department name:");
        String newName = br.readLine();
        System.out.println("Enter new manager id:");
        int managerId = Integer.parseInt(br.readLine());

        try {
            Department.editDepartment(name, newName, managerId);
            System.out.println("The department has been successfully updated");
        } catch (IllegalStateException ex){
            System.out.println(ex.getMessage());
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }

    }


    private void deleteDepartment() throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a department's name:");
        String name = br.readLine();

        Department d = DepartmentFinder.getInstance().findByName(name);

        if (d == null) {
            System.out.println("No such department exists");
        } else {
            d.delete();
            System.out.println("The department has been successfully deleted");
        }
    }


}
