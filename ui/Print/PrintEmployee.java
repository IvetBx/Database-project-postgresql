package ui.Print;

import rdg.Department.DepartmentFinder;
import rdg.Employee.Employee;
import rdg.Job.JobFinder;

import java.sql.SQLException;

public class PrintEmployee {

    private static final PrintEmployee INSTANCE = new PrintEmployee();

    public static PrintEmployee getInstance() { return INSTANCE; }

    private PrintEmployee() { }

    public void print(Employee employee) throws SQLException {
        if (employee == null) {
            throw new NullPointerException("employee cannot be null");
        }

        System.out.print("Id : ");
        System.out.print(employee.getId());
        System.out.print(" First name: ");
        System.out.print(employee.getFirst_name());
        System.out.print(" Last name: ");
        System.out.print(employee.getLast_name());
        System.out.print( "Job: ");
        System.out.print(JobFinder.getInstance().findById(employee.getJob_id()).getType());
        System.out.print(" Work in: ");
        System.out.print(DepartmentFinder.getInstance().findById(employee.getDepartment_id()).getName());
        System.out.print(" Rank: ");
        System.out.println(employee.getRank());
    }

}
