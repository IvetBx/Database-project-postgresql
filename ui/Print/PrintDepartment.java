package ui.Print;

import rdg.Department.Department;
import rdg.Employee.EmployeeFinder;

import java.sql.SQLException;

public class PrintDepartment {

    private static final PrintDepartment INSTANCE = new PrintDepartment();

    public static PrintDepartment getInstance() { return INSTANCE; }

    private PrintDepartment() { }

    public void print(Department d) throws SQLException {
        if (d == null) {
            throw new NullPointerException("department cannot be null");
        }

        System.out.print("Id :          ");
        System.out.println(d.getId());
        System.out.print("Department name:   ");
        System.out.println(d.getName());
        System.out.print("Manager:    ");
        try{
            System.out.println(EmployeeFinder.getInstance().findById(d.getManager_id()).getFirst_name() + " " +
                    EmployeeFinder.getInstance().findById(d.getManager_id()).getLast_name());
            System.out.println();
        } catch (NullPointerException ex){
            System.out.println("department without manager");
        }

    }

}
