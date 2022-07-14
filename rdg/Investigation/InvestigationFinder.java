package rdg.Investigation;

import DbConnect.DbContext;
import Transaction.CaseClosing;
import rdg.Person.Person;
import rdg.Person.PersonFinder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvestigationFinder {

    private static final InvestigationFinder INSTANCE = new InvestigationFinder();

    public static InvestigationFinder getInstance() {
        return INSTANCE;
    }

    private InvestigationFinder(){

    }

    public Investigation findById(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM investigation WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    Investigation i = new Investigation();
                    i.setId(r.getInt("id"));
                    i.setLeader_id(r.getInt("leader_id"));
                    i.setCategory_id(r.getInt("category_id"));
                    i.setResolved(r.getBoolean("resolved"));
                    i.setDescription(r.getString("description"));
                    i.setAct_date(r.getDate("act_date"));


                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return i;

                } else {
                    return null;
                }
            }
        }
    }

    public Investigation findRandom(int id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM investigation " +
                "TABLESAMPLE SYSTEM_ROWS(50) WHERE resolved = false AND id != ? ORDER BY random() LIMIT 1")) {

            s.setInt(1,id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {

                    Investigation i = new Investigation();
                    i.setId(r.getInt("id"));
                    i.setLeader_id(r.getInt("leader_id"));
                    i.setCategory_id(r.getInt("category_id"));
                    i.setResolved(r.getBoolean("resolved"));
                    i.setDescription(r.getString("description"));
                    i.setAct_date(r.getDate("act_date"));


                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return i;

                } else {
                    return null;
                }
            }
        }
    }

    public static void notConfirmedAndPunishedPerson(int investigationId) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT t.person_id, confirmed, role_id FROM acts AS a JOIN\n" +
                "(SELECT person_id FROM acts\n" +
                "WHERE (role_id = 2 OR role_id = 3) AND investigation_id = ?) AS t\n" +
                "ON t.person_id = a.person_id\n" +
                "EXCEPT\n" +
                "SELECT person_id, true AS confirmed, 3 AS role_id FROM impose_punishments \n" +
                "WHERE investigation_id = ?")) {

            s.setInt(1, investigationId);
            s.setInt(2, investigationId);

            try (ResultSet r = s.executeQuery()) {

                while (r.next()) {
                    Person p = PersonFinder.getInstance().findById(r.getInt("person_id"));
                    if(!r.getBoolean("confirmed")){
                        CaseClosing.allnotConfirmed.add(p);
                    }
                    else{
                        CaseClosing.allnotPunished.add(p);
                    }
                }
                System.out.println("koniec");
            }
        }
    }



}
