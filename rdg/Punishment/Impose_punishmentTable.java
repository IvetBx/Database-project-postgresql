package rdg.Punishment;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Impose_punishmentTable {

    int person_id;
    int punishmentId;
    int employee_id;
    int investigation_id;

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public int getPunishmentId() {
        return punishmentId;
    }

    public void setPunishmentId(int punishmentId) {
        this.punishmentId = punishmentId;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getInvestigation_id() {
        return investigation_id;
    }

    public void setInvestigation_id(int investigation_id) {
        this.investigation_id = investigation_id;
    }

    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                        "impose_punishments (person_id, punishment_id, employee_id, investigation_id) VALUES (?,?,?,?) ON CONFLICT do nothing")) {

            s.setInt(1, person_id);
            s.setInt(2, punishmentId);
            s.setInt(3, employee_id);
            s.setInt(4, investigation_id);
            s.executeUpdate();

        }
    }

}
