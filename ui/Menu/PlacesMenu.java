package ui.Menu;

import DbConnect.DbContext;
import rdg.Acts.Act;
import rdg.Acts.ActFinder;
import rdg.Investigation.CategoryFinder;
import rdg.Investigation.Investigation;
import rdg.Investigation.InvestigationFinder;
import rdg.PlacesInCity.Address;
import rdg.PlacesInCity.AddressFinder;
import rdg.Statistic.Statistic;
import rdg.Statistic.Statistic2;
import rdg.Statistic.Statistic2Finder;
import rdg.Statistic.StatisticFinder;
import ui.Menu.Menu;
import ui.Print.PrintInvestigation;
import ui.Print.PrintStatistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlacesMenu extends Menu {

    @Override
    public void print() {
        System.out.println("************************************************");
        System.out.println("* 1. places of criminal acts                   *");
        System.out.println("* 2. add place of criminal act                 *");
        System.out.println("* 3. show statistics (most dangerous places)   *");
        System.out.println("* 4. exit                                      *");
        System.out.println("************************************************");
    }

    @Override
    public void handle(String option) throws IOException, SQLException {
        switch (option) {
            case "1":   showPlaces(); break;
            case "2":   createPlace();break;
            case "3":   showStatistic2(); break;
            case "4":   exit(); MainMenu m = new MainMenu(); m.run(); break;
            default:    System.out.println("Unknown option"); break;
        }
    }

    private void showPlaces() throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter address id: ");
        int adddressId = Integer.parseInt(br.readLine());
        List<Act> acts = ActFinder.getInstance().findByPlacesAll(adddressId);
        if(acts.size() == 0){
            System.out.println("No crimes was happened");
        }

        for(Act a: acts){
            Investigation i = InvestigationFinder.getInstance().findById(a.getInvestigation_id());
            System.out.println("INVESTIGATION WITH ID: " + i.getId() + " DATE: " + i.getAct_date() + " TYPE: " + CategoryFinder.getInstance().findById(i.getCategory_id()) + " IS RESOLVED : " + i.isResolved());
        }
    }

    private void createPlace() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter address id: ");
        int adddressId = Integer.parseInt(br.readLine());
        System.out.println("Enter investigation id: ");
        int investigationId = Integer.parseInt(br.readLine());
        System.out.println("Enter person connected with this place and case (personId roleId confirmed(Y/N); )");
        List<String[]> temp = new ArrayList<>();
        while(!"KONIEC".equals(br.readLine())){
            String[] temp1 = br.readLine().split(" ");
            temp.add(temp1);
        }
        try {
            Act.createPlace(adddressId, investigationId, temp);
        } catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void showStatistic2() throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Map<Integer, String> months = new HashMap<>();
        months.put(1, "January"); months.put(2, "February");
        months.put(3, "March"); months.put(4, "April");
        months.put(5, "May"); months.put(6, "June");
        months.put(7, "July"); months.put(8, "August");
        months.put(9, "September"); months.put(10, "October");
        months.put(11, "November"); months.put(12, "December");

        System.out.println("Enter a year (data from this year will be used in statistic):");
        int year = Integer.parseInt(br.readLine());

        List<Statistic2> st = new ArrayList<>();
        try{
            st = Statistic2.getStatistic2(year);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("MOST DANGEROUS DISTRICT OF CITY IN "+ year);
        System.out.print(String.format("* %-40s", "DISTRICT"));
        System.out.print(String.format("* %-40s", "NUMBER OF CASES"));
        System.out.print(String.format("* %-40s", "NUMBER OF CRIMES"));
        System.out.println(String.format("* %-40s *", "NUMBER OF MISDEMEANOR"));
        System.out.println("***********************************************************************************************************************************************************************");
        int month = 0;
        int quarter = 0;
        int yearTemp = 0;
        for(Statistic2 s : st){
            if(s.getQuarter() == 0 && s.getYear() == 0 && s.getMonth() != month){
                month = s.getMonth();
                System.out.println(String.format("* %-168s*", "MONTH: " + months.get(month)));
            } else if(s.getMonth() == 0 && s.getYear() == 0 && s.getQuarter() != quarter){
                quarter = s.getQuarter();
                System.out.println(String.format("* %-168s*", "QUARTER: " + s.getQuarter()));
            } else if((s.getMonth() == 0 && s.getQuarter() == 0 && yearTemp == 0)){
                yearTemp = s.getYear();
                System.out.println(String.format("* %-168s*", "YEAR: " + s.getYear()));
            }
            PrintStatistic.getInstance().print2(s);
        }
        System.out.println("***********************************************************************************************************************************************************************");
    }




}
