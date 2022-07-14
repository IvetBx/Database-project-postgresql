package rdg.Statistic;

import DbConnect.DbContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Statistic2 {

    String district;
    Integer month;
    Integer quarter;
    Integer year;
    int count;
    int crime;
    int misdemeanor;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCrime() {
        return crime;
    }

    public void setCrime(int crime) {
        this.crime = crime;
    }

    public int getMisdemeanor() {
        return misdemeanor;
    }

    public void setMisdemeanor(int misdemeanor) {
        this.misdemeanor = misdemeanor;
    }

    public static List<Statistic2> getStatistic2(int id) throws SQLException {
        Connection connection = DbContext.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(connection.TRANSACTION_SERIALIZABLE);
        try{

            return Statistic2Finder.getInstance().theMostDangerousPlaces(id);
        }
        catch (SQLException ex){
            connection.rollback();
            throw new RuntimeException("There are some problems, you should run it again");

        } finally {
            connection.setAutoCommit(true);
        }
    }

}
