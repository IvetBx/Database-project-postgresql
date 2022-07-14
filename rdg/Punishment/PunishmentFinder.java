package rdg.Punishment;

import DbConnect.DbContext;
import rdg.Employee.EmployeeFinder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PunishmentFinder {

    private static final PunishmentFinder INSTANCE = new PunishmentFinder();

    public static PunishmentFinder getInstance() {
        return INSTANCE;
    }

    private PunishmentFinder(){

    }

    public List<Punishment> findAll() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM punishments")) {
            try (ResultSet r = s.executeQuery()) {

                List<Punishment> allPunishments = new ArrayList<>();

                while (r.next()) {
                    Punishment p = new Punishment();
                    p.setId(r.getInt("id"));
                    p.setIs_fine(r.getBoolean("is_fine"));
                    p.setMinimal(r.getInt("minimal"));
                    p.setMaximal(r.getInt("maximal"));
                    p.setIs_warrant(r.getBoolean("is_warrant"));
                    allPunishments.add(p);
                }
                return allPunishments;
            }
        }
    }


    public Punishment findWarrant() throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM punishments WHERE is_warrant = true")) {

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Punishment p = new Punishment();

                    p.setId(r.getInt("id"));
                    p.setIs_warrant(r.getBoolean("is_warrant"));
                    p.setIs_fine(r.getBoolean("is_fine"));
                    p.setMinimal(r.getInt("minimal"));
                    p.setMaximal(r.getInt("maximal"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return p;

                } else {
                    return null;
                }
            }
        }
    }

    public Punishment findById(Integer id) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM punishments WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Punishment p = new Punishment();

                    p.setId(r.getInt("id"));
                    p.setIs_warrant(r.getBoolean("is_warrant"));
                    p.setIs_fine(r.getBoolean("is_fine"));
                    p.setMinimal(r.getInt("minimal"));
                    p.setMaximal(r.getInt("maximal"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return p;

                } else {
                    return null;
                }
            }
        }
    }


    public Punishment findByRange(Integer minimal, Integer maximal) throws SQLException {

        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM punishments WHERE minimal = ? AND maximal = ?")) {
            s.setInt(1, minimal);
            s.setInt(2, maximal);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Punishment p = new Punishment();

                    p.setId(r.getInt("id"));
                    p.setIs_warrant(r.getBoolean("is_warrant"));
                    p.setIs_fine(r.getBoolean("is_fine"));
                    p.setMinimal(r.getInt("minimal"));
                    p.setMaximal(r.getInt("maximal"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return p;

                } else {
                    return null;
                }
            }
        }
    }

    public static Integer imposePunishment(int personId, int investigationId) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT CASE WHEN t.is_crime THEN (SELECT id FROM punishments WHERE is_warrant)\n" +
                "\t\tWHEN t.is_protecting THEN null\n" +
                "\t\tELSE (SELECT id FROM punishments WHERE (serious * 200) BETWEEN minimal AND maximal ORDER BY random() LIMIT 1)\n" +
                "\t\tEND AS p\n" +
                "\t\t\n" +
                "\t\tFROM(\n" +
                "\n" +
                "SELECT category_id, is_crime, is_misdemeanor, is_protecting, serious FROM acts JOIN investigation AS i \n" +
                "ON acts.investigation_id = i.id\n" +
                "JOIN categories AS c ON c.id = i.category_id\n" +
                "WHERE confirmed AND investigation_id = ? AND role_id = 3 AND person_id = ? AND i.resolved = false AND c.is_protecting = false\n" +
                ") AS t")) {

            s.setInt(1, investigationId);
            s.setInt(2, personId);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Impose_punishmentTable ip = new Impose_punishmentTable();
                    ip.setPerson_id(personId);
                    ip.setInvestigation_id(investigationId);
                    ip.setPunishmentId(r.getInt("p"));
                    if(PunishmentFinder.getInstance().findWarrant().getId() == ip.getPunishmentId()){
                        ip.setEmployee_id(EmployeeFinder.getInstance().findEmployeeToPunishing(true));
                    } else{
                        ip.setEmployee_id(EmployeeFinder.getInstance().findEmployeeToPunishing(false));
                    }
                    try{
                        ip.insert();
                    } catch (SQLException ex){
                        throw new SQLException(ex.getMessage());
                    }
                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return ip.getPunishmentId();
                }
                else{
                    throw new IllegalStateException("This person wasn't accused of this investigation or case has been closed or it " +
                            "is protecting.");
                }
            }
        }
    }

}
