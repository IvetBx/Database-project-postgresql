package ui.Print;

import rdg.Acts.Act;
import rdg.Acts.ActFinder;
import rdg.Employee.EmployeeFinder;
import rdg.Investigation.CategoryFinder;
import rdg.Investigation.Investigation;
import rdg.Person.Person;
import rdg.Person.PersonFinder;
import rdg.PlacesInCity.Address;
import rdg.PlacesInCity.AddressFinder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrintInvestigation {


    private static final PrintInvestigation INSTANCE = new PrintInvestigation();

    public static PrintInvestigation getInstance() { return INSTANCE; }

    private PrintInvestigation() { }

    public void print(Investigation i) throws SQLException {
        if (i == null) {
            throw new NullPointerException("investigation cannot be null");
        }

        System.out.print("Id : ");
        System.out.print(i.getId());
        System.out.print("   Date: ");
        System.out.print(i.getAct_date());
        System.out.print("   Description: ");
        System.out.print(i.getDescription());
        System.out.print("   Leader: ");
        try{
            System.out.print(EmployeeFinder.getInstance().findById(i.getLeader_id()).getFirst_name() + " " +
                    EmployeeFinder.getInstance().findById(i.getLeader_id()).getLast_name());
        } catch (NullPointerException ex){
            System.out.print("case without leader");
        }
        System.out.print("   Resolved: ");
        System.out.print(i.isResolved());
        System.out.print("   Category: ");
        try{
            System.out.println(CategoryFinder.getInstance().findById(i.getCategory_id()).getType_act());
        } catch (NullPointerException ex){
            System.out.println("case without type");
        }

        List<Act> acts = ActFinder.getInstance().findByInvestigationIdAddress(i.getId());
        System.out.println("Addresses: ");
        if(acts.size() != 0){
            for (int j = 0; j < acts.size(); j++){
                Address a = AddressFinder.getInstance().findById(acts.get(j).getAddress_id());
                PrintAddress.getInstance().print(a);
            }
        }

        List<Act> persons = ActFinder.getInstance().findByInvestigationIdPerson(i.getId());
        System.out.println("Persons connected with case: ");
        if(persons.size() != 0){
            for (int j = 0; j < persons.size(); j++){
                Person p = PersonFinder.getInstance().findById(persons.get(j).getPerson_id());
                PrintPerson.getInstance().print(p);
            }
        }
        System.out.println();
    }

}
