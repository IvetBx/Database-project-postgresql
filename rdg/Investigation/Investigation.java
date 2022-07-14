package rdg.Investigation;

import DbConnect.DbContext;
import rdg.Employee.EmployeeFinder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Investigation {

    private Integer id;
    private java.sql.Date act_date;
    private String description;
    private boolean resolved;
    private Integer leader_id;
    private Integer category_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getAct_date() {
        return act_date;
    }

    public void setAct_date(java.sql.Date act_date) {
        this.act_date = act_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public Integer getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(Integer leader_id) {
        this.leader_id = leader_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }


    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                        "investigation (act_date, description, resolved, leader_id, category_id) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            s.setDate(1, act_date);
            s.setString(2, description);
            s.setBoolean(3, resolved);
            System.out.println(leader_id);
            s.setInt(4, leader_id);
            s.setInt(5, category_id);

            s.executeUpdate();
            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }


    public void update() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement( "UPDATE investigation SET \n" +
                        "                act_date = ?,\n" +
                        "                     description = ?,\n" +
                        "                     resolved = ?,\n" +
                        "                     leader_id = ?,\n" +
                        "                     category_id = ?")){
            s.setDate(1, act_date);
            s.setString(2, description);
            s.setBoolean(3, resolved);
            s.setInt(4, leader_id);
            s.setInt(5, category_id);

            s.executeUpdate();
        }
    }

    public static Investigation controlInvestigation(int id) throws SQLException {
        try {
            Investigation i = InvestigationFinder.getInstance().findById(id);
            return i;
        } catch (NullPointerException ex){
            throw new IllegalArgumentException("Investigation with this id doesn't exist");
        }
    }

    public static Investigation addInvestigation(java.sql.Date date, String description, boolean resolved, Integer leader_id, Integer category_id){

        Investigation i = new Investigation();
        i.setAct_date(date);
        i.setDescription(description);
        i.setResolved(resolved);
        try{
            i.setLeader_id(EmployeeFinder.getInstance().findById(leader_id).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Leader id doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try{
            i.setCategory_id(CategoryFinder.getInstance().findById(category_id).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Category id doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            i.insert();
            return i;

        } catch (SQLException ex){
            throw new IllegalStateException("You cannot add investigation");
        }
    }

    public static void changeResult(boolean actual, int id) throws SQLException {
        Investigation i = InvestigationFinder.getInstance().findById(id);
        i.setResolved(!actual);
        i.update();
    }



}
