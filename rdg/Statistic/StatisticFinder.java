package rdg.Statistic;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticFinder {

    private static final StatisticFinder INSTANCE = new StatisticFinder();

    public static StatisticFinder getInstance() { return INSTANCE; }

    private StatisticFinder(){ }

    public List<Statistic> bestPolicemans(int year) throws SQLException {
        try(PreparedStatement s = DbContext.getConnection().prepareStatement("WITH best AS(\n" +
                "SELECT t.leader_id, t.month, t.year, t.rownum FROM\n" +
                "(SELECT leader_id , (extract(month from act_date))::int AS month, (extract(year from act_date))::int AS year, count(*)::int AS numberOf,\n" +
                "row_number() over (partition by (((extract(month from act_date)))) order by (extract(month from act_date)), count(*) DESC) as rownum\n" +
                "FROM investigation AS i\n" +
                "WHERE i.resolved AND (extract(year from act_date)) = ? AND i.leader_id IS NOT NULL\n" +
                "GROUP BY  GROUPING SETS ((leader_id, (extract(month from act_date)), (extract(year from act_date))))) AS t\n" +
                "WHERE t.rownum <= 2\n" +
                "),\n" +
                "            \n" +
                "best1 AS(\n" +
                "SELECT t.employee_id, t.month, t.year, t.rownum FROM\n" +
                "(SELECT employee_id, (extract(month from act_date))::int AS month, (extract(year from act_date))::int AS year, count(*)::int AS numberOf,\n" +
                "row_number() over (partition by (((extract(month from act_date)))) order by (extract(month from act_date)), count(*) DESC) as rownum\n" +
                "FROM investigation AS i JOIN impose_punishments AS ip\n" +
                "ON i.id = ip.investigation_id AND i.resolved AND (extract(year from act_date)) = ?\n" +
                "GROUP BY  GROUPING SETS ((employee_id, (extract(month from act_date)), (extract(year from act_date))))) AS t\n" +
                "WHERE t.rownum <= 2\n" +
                ")\n" +
                "              \n" +
                "SELECT best.leader_id AS bestInResulted, best1.employee_id AS bestInPunishment,\n" +
                "CASE WHEN (best.month IS NULL) THEN best1.month\n" +
                "ELSE best.month\n" +
                "END AS month, best.rownum AS best, best1.rownum AS best1\n" +
                "FROM best FULL JOIN best1 ON best.month = best1.month AND best.year = best1.year \n" +
                "AND (best.rownum = best1.rownum)\n" +
                "ORDER BY month, best.rownum")) {

            s.setInt(1, year);
            s.setInt(2, year);

            try (ResultSet r = s.executeQuery()) {
                List<Statistic> stat = new ArrayList<>();
                while (r.next()) {
                    Statistic st = new Statistic();
                    st.setYear(year);
                    st.setMonth(r.getInt("month"));
                    try{
                        st.setOrder(r.getInt("best"));
                    } catch(NullPointerException ex){
                        st.setOrder(r.getInt("best1"));
                    }
                    st.setBestInResulted(r.getInt("bestInResulted"));
                    st.setBestInPunishment(r.getInt("bestInPunishment"));
                    stat.add(st);
                }
                return stat;
            }
        }
    }

}
