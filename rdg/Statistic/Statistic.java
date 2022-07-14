package rdg.Statistic;

import DbConnect.DbContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Statistic {

    int year;
    int month;
    Integer bestInResulted;
    Integer bestInPunishment;
    int order;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Integer getBestInResulted() {
        return bestInResulted;
    }

    public void setBestInResulted(Integer bestInResulted) {
        this.bestInResulted = bestInResulted;
    }

    public Integer getBestInPunishment() {
        return bestInPunishment;
    }

    public void setBestInPunishment(Integer bestInPunishment) {
        this.bestInPunishment = bestInPunishment;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    public static List<Statistic> getStatistic(int id) throws SQLException {
        Connection connection = DbContext.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(connection.TRANSACTION_SERIALIZABLE);
        try{
            return StatisticFinder.getInstance().bestPolicemans(id);
        }
        catch (SQLException ex){
            connection.rollback();
            throw new RuntimeException("There are some problems, you should run it again");

        } finally {
            connection.setAutoCommit(true);
        }
    }


}
