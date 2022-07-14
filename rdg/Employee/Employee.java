package rdg.Employee;

import DbConnect.DbContext;
import rdg.Department.DepartmentFinder;
import rdg.Investigation.Investigation;
import rdg.Investigation.InvestigationFinder;
import rdg.Investigation.WorkOn;
import rdg.Investigation.WorkOnFinder;
import rdg.Job.JobFinder;
import ui.Print.PrintEmployee;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Employee {

    private Integer id;
    private String first_name;
    private String last_name;
    private int job_id;
    private int department_id;
    private Integer rank;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                "employees (first_name, last_name, job_id, department_id, rank) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, first_name);
            s.setString(2, last_name);
            s.setInt(3, job_id);
            s.setInt(4, department_id);
            s.setInt(5, rank);

            s.executeUpdate();
            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {

        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE " +
                "employees SET first_name = ?, last_name = ?, job_id = ?, department_id = ?, rank = ? WHERE id = ?")) {
            s.setString(1, first_name);
            s.setString(2, last_name);
            s.setInt(3, job_id);
            s.setInt(4, department_id);
            s.setInt(5, rank);
            s.setInt(6, id);

            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {

        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM employees WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

    public static void editEmployee(Employee e, String first_name, String last_name, String job, String department, int rank){
        e.setFirst_name(first_name);
        e.setLast_name(last_name);

        try{
            e.setJob_id(JobFinder.getInstance().findByType(job).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to change employee's job to job which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try{
            e.setDepartment_id(DepartmentFinder.getInstance().findByName(department).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to change employee's department to department which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        e.setRank(rank);
        try{
            e.update();
        } catch (SQLException ex){
            throw new IllegalStateException("Try to change employee to employee who already exists");
        }
    }

    public static Employee addEmployee(String first_name, String last_name, String job, String department, int rank){

        Employee e = new Employee();
        e.setFirst_name(first_name);
        e.setLast_name(last_name);

        try{
            e.setJob_id(JobFinder.getInstance().findByType(job).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to add employee with job which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try{
            e.setDepartment_id(DepartmentFinder.getInstance().findByName(department).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to add employee with department which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        e.setRank(rank);
        try {
            e.insert();
            return e;

        } catch (SQLException ex){
            throw new IllegalStateException("Try to add employee who already exists");
        }
    }


    public static List<Investigation> controlEmployee(int emplId) throws IOException, SQLException {
        List<Investigation> allInvestigation;
        try {
            allInvestigation = WorkOnFinder.getInstance().findByEmployeeId(emplId);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Employee doesn't exist");
        }

        if (allInvestigation == null) {
            throw new IllegalStateException("This employee doesn't work on any cases");
        }

        try {
            PrintEmployee.getInstance().print(EmployeeFinder.getInstance().findById(emplId));
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Employee doesn't exist");
        }
        return allInvestigation;
    }

    public static void deleteCase(int emplId, int investId){

        try{
            WorkOn w = WorkOnFinder.getInstance().findTupleEmployeeInvestigation(emplId, investId);
            w.delete();
        } catch(NullPointerException ex){
            throw new IllegalArgumentException("Case's id was wrong");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void addCase(int emplId, int investId){

        try{
            InvestigationFinder.getInstance().findById(investId);
        } catch(NullPointerException ex){
            throw new IllegalArgumentException("Case's id is wrong");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        WorkOn w = new WorkOn();
        try{
            w.setInvestigation_id(investId);
            w.setEmployee_id(emplId);
            w.insert();
        }
        catch (SQLException ex){
            throw new IllegalStateException("This tuple of employee and investigation already exist");
        }

    }


}
