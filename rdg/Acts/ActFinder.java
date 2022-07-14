package rdg.Acts;

import DbConnect.DbContext;
import rdg.Department.Department;
import rdg.Department.DepartmentFinder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActFinder {

    private static final ActFinder INSTANCE = new ActFinder();

    public static ActFinder getInstance() { return INSTANCE; }

    private ActFinder(){

    }

    public List<Act> findByPlacesAll(int addressId) throws SQLException {
        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT DISTINCT ON (investigation_id, address_id) * FROM acts WHERE address_id = ?\n")) {

            s.setInt(1,addressId);

            try (ResultSet r = s.executeQuery()) {
                List<Act> acts = new ArrayList<>();
                while (r.next()) {
                    Act a = new Act();
                    a.setAddress_id(r.getInt("address_id"));
                    a.setInvestigation_id(r.getInt("investigation_id"));
                    a.setPerson_id(r.getInt("person_id"));
                    a.setRole_id(r.getInt("role_id"));
                    a.setConfirmed(r.getBoolean("confirmed"));
                    acts.add(a);
                }
                return acts;
            }
        }
    }

    public List<Act> findByInvestigationId(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM acts WHERE investigation_id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                List<Act> acts = new ArrayList<>();
                while (r.next()) {

                    Act a = new Act();
                    a.setAddress_id(r.getInt("address_id"));
                    a.setInvestigation_id(r.getInt("investigation_id"));
                    a.setPerson_id(r.getInt("person_id"));
                    a.setRole_id(r.getInt("role_id"));
                    a.setConfirmed(r.getBoolean("confirmed"));
                }
                return acts;

            }
        }
    }

    public List<Act> findByInvestigationIdAddress(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT DISTINCT ON(address_id) * FROM acts WHERE investigation_id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                List<Act> acts = new ArrayList<>();
                while (r.next()) {

                    Act a = new Act();
                    a.setAddress_id(r.getInt("address_id"));
                    a.setInvestigation_id(r.getInt("investigation_id"));
                    a.setPerson_id(r.getInt("person_id"));
                    a.setRole_id(r.getInt("role_id"));
                    a.setConfirmed(r.getBoolean("confirmed"));
                    acts.add(a);
                }
                return acts;
            }
        }
    }

    public List<Act> findByInvestigationIdPerson(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM acts WHERE investigation_id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                List<Act> acts = new ArrayList<>();
                while (r.next()) {

                    Act a = new Act();
                    a.setAddress_id(r.getInt("address_id"));
                    a.setInvestigation_id(r.getInt("investigation_id"));
                    a.setPerson_id(r.getInt("person_id"));
                    a.setRole_id(r.getInt("role_id"));
                    a.setConfirmed(r.getBoolean("confirmed"));
                    acts.add(a);
                }
                return acts;

            }
        }
    }

    public List<Act> findByPersonIdAndInvestigationId(int id, int investId) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM acts WHERE person_id = ? AND investigation_id = ?")) {
            s.setInt(1, id);
            s.setInt(2, investId);

            try (ResultSet r = s.executeQuery()) {
                List<Act> acts = new ArrayList<>();
                while (r.next()) {

                    Act a = new Act();
                    a.setAddress_id(r.getInt("address_id"));
                    a.setInvestigation_id(r.getInt("investigation_id"));
                    a.setPerson_id(r.getInt("person_id"));
                    a.setRole_id(r.getInt("role_id"));
                    a.setConfirmed(r.getBoolean("confirmed"));
                    acts.add(a);
                }
                return acts;
            }
        }
    }

    public Act findByPersonIdAndInvestigationIdAndAddressId(int id, int investId, int addressId) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM acts WHERE person_id = ? AND investigation_id = ? AND address_id = ?")) {
            s.setInt(1, id);
            s.setInt(2, investId);
            s.setInt(3, addressId);

            try (ResultSet r = s.executeQuery()) {
                List<Act> acts = new ArrayList<>();
                if (r.next()) {
                    Act a = new Act();
                    a.setAddress_id(r.getInt("address_id"));
                    a.setInvestigation_id(r.getInt("investigation_id"));
                    a.setPerson_id(r.getInt("person_id"));
                    a.setRole_id(r.getInt("role_id"));
                    a.setConfirmed(r.getBoolean("confirmed"));
                    acts.add(a);
                    if(r.next()){
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return a;
                } else{
                    return null;
                }
            }
        }
    }

}
