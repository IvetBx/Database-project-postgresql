package rdg.Acts;

import DbConnect.DbContext;
import rdg.Investigation.InvestigationFinder;
import rdg.Person.PersonFinder;
import rdg.Person.RoleFinder;
import rdg.PlacesInCity.AddressFinder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Act {

    private int person_id;
    private int address_id;
    private int investigation_id;
    private int role_id;
    private boolean confirmed;

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getInvestigation_id() {
        return investigation_id;
    }

    public void setInvestigation_id(int investigation_id) {
        this.investigation_id = investigation_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                        "acts (person_id, address_id, investigation_id, role_id, confirmed) VALUES (?,?,?,?,?)")) {
            s.setInt(1, person_id);
            s.setInt(2, address_id);
            s.setInt(3, investigation_id);
            s.setInt(4, role_id);
            s.setBoolean(5, false);
            s.executeUpdate();
        }
    }

    public void update() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE " +
                "acts SET role_id = ? , confirmed = ? WHERE person_id = ? AND address_id = ? AND investigation_id = ?")) {
            s.setInt(1, role_id);
            s.setBoolean(2, confirmed);
            s.setInt(3, person_id);
            s.setInt(4, address_id);
            s.setInt(5, investigation_id);
            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM acts WHERE person_id = ? AND address_id = ? AND investigation_id = ?")) {
            s.setInt(1, person_id);
            s.setInt(2, address_id);
            s.setInt(3, investigation_id);
            s.executeUpdate();
        }

    }

    public static void deleteAdresses(int address_id, int investigation_id) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM acts WHERE address_id = ? AND investigation_id = ?")) {
            s.setInt(1, address_id);
            s.setInt(2, investigation_id);
            s.executeUpdate();
        }

    }

    public static Act add(int address_id, int investigation_id ,int person_id , String role, boolean confirmed){

        Act a = new Act();

        try{
            a.setAddress_id(AddressFinder.getInstance().findById(address_id).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to add act with address which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try{
            a.setInvestigation_id(InvestigationFinder.getInstance().findById(investigation_id).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to add act with investigation which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try{
            a.setPerson_id(PersonFinder.getInstance().findById(person_id).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to add act with person which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try{
            a.setRole_id(RoleFinder.getInstance().findByType(role).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to add act role which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        a.setConfirmed(confirmed);
        try {
            a.insert();
            return a;

        } catch (SQLException ex){
            throw new IllegalStateException("Try to add act who already exists");
        }
    }

    public static Act updatePerson(int address_id, int investigation_id ,int person_id , String role, boolean confirmed) throws SQLException {
        Act a = ActFinder.getInstance().findByPersonIdAndInvestigationIdAndAddressId(person_id, investigation_id, address_id);
        try {
            a.setRole_id(RoleFinder.getInstance().findByType(role).getId());
            a.setConfirmed(confirmed);
            a.update();
        } catch (NullPointerException ex){
            throw new IllegalArgumentException("This person isn't connected with this investigation on this address");
        }
        return a;
    }

    public static void createPlace(int address_id, int investigation_id, List<String[]> people) throws SQLException {
        Act a = new Act();

        try{
            a.setAddress_id(AddressFinder.getInstance().findById(address_id).getId());
        }
        catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to add act with address which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try{
            a.setInvestigation_id(InvestigationFinder.getInstance().findById(investigation_id).getId());
        }  catch(NullPointerException ex){
            throw new IllegalArgumentException("Try to add act with investigation which doesnt't exist");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (String[] p : people){
            int id = Integer.parseInt(p[0]);
            String role = RoleFinder.getInstance().findById(Integer.parseInt(p[1])).getType();
            boolean confirmed = false;
            if("Y".equals(p[2])){
                confirmed = true;
            }
            try {
                add(address_id, investigation_id, id, role, confirmed);
            } catch (IllegalArgumentException ex){
                throw new IllegalArgumentException(ex.getMessage());
            }
        }
    }
}
