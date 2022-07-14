package rdg.Statistic;

import DbConnect.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Statistic2Finder {

    private static final Statistic2Finder INSTANCE = new Statistic2Finder();

    public static Statistic2Finder getInstance() { return INSTANCE; }

    private Statistic2Finder(){ }

    public List<Statistic2> theMostDangerousPlaces(int year) throws SQLException {
        try(PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT name_city_district, month, quarter, year, count, crime, misdemeanor FROM (\n" +
                "SELECT name_city_district, extract(month from act_date) AS month,\n" +
                "extract(quarter from act_date) AS quarter, extract(year from act_date) AS year,\n" +
                "row_number() over (partition by (((extract(month from act_date)), (extract(quarter from act_date)), (extract(year from act_date)))) order by  extract(month from act_date),\n" +
                "(extract(quarter from act_date)), (extract(year from act_date)), count(*) DESC) AS rn, count(*) AS count, count(CASE WHEN x.is_crime THEN 1 END) AS crime,\n" +
                "count(CASE WHEN x.is_misdemeanor THEN 1 END) AS misdemeanor FROM\n" +
                "((SELECT DISTINCT ON(ac.address_id, ac.investigation_id) * FROM (acts AS ac JOIN (investigation AS i JOIN categories AS c ON i.category_id = c.id)\n" +
                "ON ac.investigation_id = i.id AND extract(year from act_date) = ? AND c.is_protecting = false)) AS x\n" +
                "JOIN\n" +
                "(addresses AS a JOIN places AS p ON a.place_id = p.id)\n" +
                "ON a.id = x.address_id)\n" +
                "GROUP BY GROUPING SETS ((name_city_district, (extract(year from act_date))),\n" +
                "(name_city_district, (extract(quarter from act_date))),\n" +
                "(name_city_district, (extract(month from act_date)))\n" +
                ")   \n" +
                ") AS t\n" +
                "WHERE t.rn <= 3")) {
            s.setInt(1, year);
            try (ResultSet r = s.executeQuery()) {
                List<Statistic2> stat = new ArrayList<>();
                while (r.next()) {
                    Statistic2 st = new Statistic2();
                    st.setDistrict(r.getString("name_city_district"));
                    st.setMonth(r.getInt("month"));
                    st.setQuarter(r.getInt("quarter"));
                    st.setYear(r.getInt("year"));
                    st.setCount(r.getInt("count"));
                    st.setCrime(r.getInt("crime"));
                    st.setMisdemeanor(r.getInt("misdemeanor"));
                    stat.add(st);
                }
                return stat;
            }
        }
    }
}
