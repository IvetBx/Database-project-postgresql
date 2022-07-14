package rdg.Punishment;

import DbConnect.DbContext;
import ui.Print.PrintPunishment;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Punishment {

    int id;
    boolean is_fine;
    boolean is_warrant;
    Integer maximal;
    Integer minimal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_fine() {
        return is_fine;
    }

    public void setIs_fine(boolean is_fine) {
        this.is_fine = is_fine;
    }

    public boolean isIs_warrant() {
        return is_warrant;
    }

    public void setIs_warrant(boolean is_warrant) {
        this.is_warrant = is_warrant;
    }

    public int getMaximal() {
        return maximal;
    }

    public void setMaximal(Integer maximal) {
        this.maximal = maximal;
    }

    public int getMinimal() {
        return minimal;
    }

    public void setMinimal(Integer minimal) {
        this.minimal = minimal;
    }

    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                        "punishments (is_fine, minimal, maximal, is_warrant) VALUES (?,?,?,?) ON CONFLICT do nothing",
                Statement.RETURN_GENERATED_KEYS)) {

            s.setBoolean(1, is_fine);
            if(minimal == null && maximal == null){
                s.setNull(2, 0);
                s.setNull(3, 0);
            }
            else{
                s.setInt(2, minimal);
                s.setInt(3, maximal);
            }
            s.setBoolean(4, is_warrant);

            s.executeUpdate();
            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public static Punishment addFine(Integer minimal, Integer maximal) throws IOException, SQLException {

        Punishment p = new Punishment();

        try {
            p.setId(PunishmentFinder.getInstance().findByRange(minimal, maximal).getId());
        } catch (NullPointerException ex){
            p.setIs_fine(true);
            p.setIs_warrant(false);
            p.setMaximal(maximal);
            p.setMinimal(minimal);
            try {
                p.insert();
            } catch (SQLException ex1){
                throw new IllegalArgumentException("minimal > maximal");
            }
            return p;
        }
        throw new IllegalArgumentException("Fine with this range already exists");
    }

    public static Punishment addWarrant() throws SQLException {
        Punishment p = new Punishment();
        try {
            p.setId(PunishmentFinder.getInstance().findWarrant().getId());
        } catch (NullPointerException ex){
            p.setIs_warrant(true);
            p.setMinimal(null);
            p.setMaximal(null);
            p.setIs_fine(false);
            p.insert();
            return p;
        }

        throw new IllegalArgumentException("One warrant is already in table");
    }

}
