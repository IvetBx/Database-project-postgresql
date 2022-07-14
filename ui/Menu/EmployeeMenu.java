package ui.Menu;

import DbConnect.DbContext;
import Transaction.ChangingInvestigators;
import Transaction.Evaluation;
import rdg.Statistic.Statistic;
import rdg.Department.Department;
import rdg.Department.DepartmentFinder;
import rdg.Employee.Employee;
import rdg.Employee.EmployeeFinder;
import rdg.Investigation.Investigation;
import rdg.Job.JobFinder;
import rdg.Statistic.StatisticFinder;
import ui.Print.PrintDepartment;
import ui.Print.PrintEmployee;
import ui.Print.PrintInvestigation;
import ui.Print.PrintStatistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeMenu extends Menu {
    @Override
    public void print() {
        System.out.println("************************************************");
        System.out.println("* 1. list of all employees                     *");
        System.out.println("* 2. add new employee                          *");
        System.out.println("* 3. edit employee                             *");
        System.out.println("* 4. edit employee's cases                     *");
        System.out.println("* 5. delete employee                           *");
        System.out.println("* 6. evaluation                                *");
        System.out.println("* 7. show statistic (best police-men)          *");
        System.out.println("* 8. optimalized cases                         *");
        System.out.println("* 9. exit                                      *");
        System.out.println("************************************************");
    }

    @Override
    public void handle(String option){
        try{
            switch (option) {
                case "1":   listAllEmployees(); break;
                case "2":   addEmployee(); break;
                case "3":   editEmployee(); break;
                case "4":   editEmployeeCases(); break;
                case "5":   deleteEmployee(); break;
                case "6":   evaluate(); break;
                case "7":   showStatistic(); break;
                case "8":   changeEmployee(); break;
                case "9":   exit(); MainMenu m = new MainMenu(); m.run(); break;
                default:    System.out.println("Unknown option"); break;
            }
        } catch(SQLException | IOException e) {
        throw new RuntimeException(e);
        }
    }


    private void listAllEmployees() throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter number of page:");
        int page = Integer.parseInt(br.readLine());

        for (Employee e : EmployeeFinder.getInstance().findAll(page)) {
            PrintEmployee.getInstance().print(e);
        }
    }


    private void addEmployee() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter first name:");
        String first_name = br.readLine();
        System.out.println("Enter last name:");
        String last_name = br.readLine();
        System.out.println("Enter job:");
        String job = br.readLine();
        System.out.println("Enter department:");
        String department = br.readLine();
        System.out.println("Enter rank:");
        Integer rank = Integer.parseInt(br.readLine());
        try {
            Employee e = Employee.addEmployee(first_name, last_name, job, department, rank);
            System.out.println("The employee has been sucessfully added");
            System.out.print("The employee's id is: ");
            System.out.println(e.getId());
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        } catch (IllegalStateException ex){
            System.out.println(ex.getMessage());
        }
    }


    private void editEmployee() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a employee's id:");
        int emplId = Integer.parseInt(br.readLine());

        Employee e = EmployeeFinder.getInstance().findById(emplId);

        if (e == null) {
            System.out.println("No such employee exists");
        } else {
            PrintEmployee.getInstance().print(e);

            System.out.println("Enter first name:");
            String first_name = br.readLine();
            System.out.println("Enter last name:");
            String last_name = br.readLine();
            System.out.println("Enter job:");
            String job = br.readLine();
            System.out.println("Enter department:");
            String department = br.readLine();
            System.out.println("Enter rank:");
            int rank = Integer.parseInt(br.readLine());
            try{
                Employee.editEmployee(e, first_name, last_name, job, department, rank);
                System.out.println("The employee has been successfully updated");

            }  catch (IllegalArgumentException ex){
                System.out.println(ex.getMessage());
            }  catch (IllegalStateException ex){
                System.out.println(ex.getMessage());
            }

        }
    }

    private void editEmployeeCases() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a employee's id:");
        List<Investigation> allInvestigation = new ArrayList<>();
        int emplId = Integer.parseInt(br.readLine());

        try{
            allInvestigation = Employee.controlEmployee(emplId);
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        } catch (IllegalStateException ex){
            System.out.println(ex.getMessage());
        }

        System.out.println("WORK ON THESE CASES: ");
        for (Investigation i : allInvestigation) {
            PrintInvestigation.getInstance().print(i);
        }

        System.out.println("Delete one of cases press 1 or add new case press 2");
        switch (br.readLine()) {

                case "1":
                    System.out.println("Delete case with id: ");
                    int invest_id = Integer.parseInt(br.readLine());
                    try{
                        Employee.deleteCase(emplId, invest_id);
                        System.out.println("The case has been successfully deleted");
                        break;
                    } catch (IllegalArgumentException ex){
                        System.out.println(ex.getMessage());
                    }

                case "2":
                    System.out.println("Insert case with id: ");
                    invest_id = Integer.parseInt(br.readLine());
                    try{
                        Employee.addCase(emplId, invest_id);
                        System.out.println("The case has been successfully added");
                        break;
                    } catch(IllegalArgumentException ex){
                        System.out.println(ex.getMessage());
                    }  catch(IllegalStateException ex){
                        System.out.println(ex.getMessage());
                    }

                default:    System.out.println("Unknown option"); break;
            }
    }


    private void deleteEmployee() throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a employee's id:");
        int emplId = Integer.parseInt(br.readLine());

        Employee e = EmployeeFinder.getInstance().findById(emplId);

        if (e == null) {
            System.out.println("No such employee exists");
        } else {
            e.delete();
            System.out.println("The employee has been successfully deleted");
        }
    }



    private void evaluate() throws SQLException {
        Connection connection = DbContext.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(connection.TRANSACTION_REPEATABLE_READ);
        try{

            Evaluation.findPeopleToEvaluation();
            if(Evaluation.all.size() == 0){
                System.out.println("Nobody can be evaluated");
                return;
            }
            for(List<Employee> tuple : Evaluation.all){
                if(Evaluation.changeRank(tuple.get(1))){
                    if("police officer".equals(JobFinder.getInstance().findById(tuple.get(1).getJob_id()).getType())){
                        System.out.println("Employee's rank is higher " + tuple.get(1).getFirst_name() + " " + tuple.get(1).getLast_name() + "(to rank " + tuple.get(1).getRank() + ")");
                    }
                }
                try{
                    Department d = DepartmentFinder.getInstance().findByManagerId(tuple.get(0).getId());
                    System.out.println("Old manager: ");
                    PrintEmployee.getInstance().print(tuple.get(0));
                    Evaluation.evaluate(tuple.get(0), tuple.get(1));
                    System.out.println("Changes at department: ");
                    PrintDepartment.getInstance().print(d);
                } catch (SQLException ex){
                    System.out.println(ex.getMessage());
                } catch(NullPointerException ex){
                    continue;
                } catch(IllegalStateException ex){
                    System.out.println(ex.getMessage());
                }
            }

            connection.commit();

        } catch (SQLException ex){
            connection.rollback();
            System.out.println("There are some problems, you should run it again");

        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void changeEmployee() {
        try {
            ChangingInvestigators.changing();
            if(ChangingInvestigators.moreEmployees.size() == 0){
                System.out.println("There is nothing to optimalized.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        System.out.println("All of cases have been optimalized");
    }

    private void showStatistic() throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Map<Integer, String> months = new HashMap<>();
        months.put(1, "January"); months.put(2, "February");
        months.put(3, "March"); months.put(4, "April");
        months.put(5, "May"); months.put(6, "June");
        months.put(7, "July"); months.put(8, "August");
        months.put(9, "September"); months.put(10, "October");
        months.put(11, "November"); months.put(12, "December");

        System.out.println("Enter a year (data from this year will be used in statistic):");
        int year = Integer.parseInt(br.readLine());

        List<Statistic> st = new ArrayList<>();
        try{
            st = Statistic.getStatistic(year);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("BEST POLICE MEN IN YEAR "+ year);
        String a = String.format(" %-60s *", "BEST IN PUNISHMENT");
        String b = String.format("* %-55s*", "BEST IN RESOLVED CASE");
        System.out.print(b);
        System.out.println(a);
        System.out.println("*****************************************************************************************************");

        int month = 0;
        for(Statistic s : st){
            if(s.getMonth() != month){
                month = s.getMonth();
                String m = String.format("* %-55s ", "MONTH: " + months.get(month));
                System.out.print(m);
                m = String.format(" %60s *", " ");
                System.out.println(m);
            }
            PrintStatistic.getInstance().print(s);
        }
        System.out.println("*****************************************************************************************************");
    }

    public static void main(String[] args) throws IOException, SQLException {
        EmployeeMenu emp = new EmployeeMenu();
        emp.run();
    }
}
