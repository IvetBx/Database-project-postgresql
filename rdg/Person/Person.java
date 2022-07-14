package rdg.Person;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class Person {

    Integer id;
    String first_name;
    String last_name;
    Date date_of_birth;
    String tel_number;
    int address_id;

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

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getTel_number() {
        return tel_number;
    }

    public void setTel_number(String tel_number) {
        this.tel_number = tel_number;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public void insert() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO " +
                        "persons (first_name, last_name, date_of_birth, tel_number, address_id) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, first_name);
            s.setString(2, last_name);
            s.setDate(3, date_of_birth);
            s.setString(4, tel_number);
            s.setInt(5, address_id);

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
                "persons SET first_name = ?, last_name = ?, date_of_birth = ?, tel_number = ?, address_id = ? WHERE id = ?")) {
            s.setString(1, first_name);
            s.setString(2, last_name);
            s.setDate(3, date_of_birth);
            s.setString(4, tel_number);
            s.setInt(5, address_id);
            s.setInt(6, id);

            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {

        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM persons WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }

    }

}
