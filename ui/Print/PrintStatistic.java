package ui.Print;

import rdg.Employee.EmployeeFinder;
import rdg.Statistic.Statistic;
import rdg.Employee.Employee;
import rdg.Statistic.Statistic2;
import rdg.Statistic.StatisticFinder;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintStatistic {

    private static final PrintStatistic INSTANCE = new PrintStatistic();

    public static PrintStatistic getInstance() { return INSTANCE; }

    private PrintStatistic() {

    }

    public void print(Statistic s) throws SQLException {
        if (s == null) {
            throw new NullPointerException("statistic cannot be null");
        }
            String a,b;
            try {
                Employee e = EmployeeFinder.getInstance().findById(s.getBestInResulted());
                b = String.format("* %-55s*", e.getFirst_name() + " " + e.getLast_name() + "(id: " + e.getId() + ")");
                System.out.print(b);
            } catch (NullPointerException ex){
                b = String.format("* %-55s*", " ");
                System.out.print(b);
            }
            try{
                Employee e1 = EmployeeFinder.getInstance().findById(s.getBestInPunishment());
                a = String.format(" %-60s *", e1.getFirst_name() + " " + e1.getLast_name() + "(id: " + e1.getId() + ")");
                System.out.println(a);
            } catch (NullPointerException ex){
                a = String.format(" %-60s *", " ");
                System.out.println(a);
    }
    }

    public void print2(Statistic2 s) throws SQLException {
        if (s == null) {
            throw new NullPointerException("statistic cannot be null");
        }
        System.out.print(String.format("* %-40s", s.getDistrict()));
        System.out.print(String.format("*      %-35s", s.getCount()));
        System.out.print(String.format("*      %-35s", s.getCrime()));
        System.out.println(String.format("*      %-35s  *", s.getMisdemeanor()));
    }

}
