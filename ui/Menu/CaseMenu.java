package ui.Menu;


import DbConnect.DbContext;
import Transaction.AssignInvestigation;
import Transaction.CaseClosing;
import Transaction.ImposePunishment;
import rdg.Acts.Act;
import rdg.Acts.ActFinder;
import rdg.Employee.Employee;
import rdg.Investigation.Investigation;
import rdg.Investigation.InvestigationFinder;
import rdg.Investigation.WorkOn;
import rdg.Investigation.WorkOnFinder;
import rdg.Person.Person;
import rdg.PlacesInCity.AddressFinder;
import ui.Print.PrintAddress;
import ui.Print.PrintEmployee;
import ui.Print.PrintInvestigation;
import ui.Print.PrintPerson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CaseMenu extends Menu {

    @Override
    public void print() {
        System.out.println("************************************************");
        System.out.println("* 1. details about case                        *");
        System.out.println("* 2. add new case                              *");
        System.out.println("* 3. edit case (edit employee)                 *");
        System.out.println("* 4. edit case (edit persons)                  *");
        System.out.println("* 5. edit case (edit places)                   *");
        System.out.println("* 6. close case                                *");
        System.out.println("* 7. assign Investigation                      *");
        System.out.println("* 8. exit                                      *");
        System.out.println("************************************************");
    }

    @Override
    public void handle(String option) throws IOException, SQLException {
        switch (option) {
            case "1":   detailsCase(); break;
            case "2":   addNewCase(); break;
            case "3":   editEmployee(); break;
            case "4":   editPersons(); break;
            case "5":   editPlaces(); break;
            case "6":   closeCase(); break;
            case "7":   assignInvestigation(); break;
            case "8":   exit(); MainMenu m = new MainMenu(); m.run(); break;
            default:    System.out.println("Unknown option"); break;
        }
    }


    private void detailsCase() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter investigation id:");
        int id = Integer.parseInt(br.readLine());
        Investigation i;
        try{
            i = Investigation.controlInvestigation(id);
            PrintInvestigation.getInstance().print(i);
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addNewCase() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter act date:");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        String temp  = br.readLine();
        java.sql.Date act_date;
        try {
            java.util.Date date = formatter.parse(temp);
            act_date = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            System.out.println("Wrong format of date");
            return;
        }
        System.out.println("Enter description:");
        String description = br.readLine();
        System.out.println("Enter true if case is resolved and false if it's not:");
        System.setProperty("true","true");
        System.setProperty("false","false");
        temp = br.readLine();
        boolean resolved = Boolean.getBoolean(temp);
        System.out.println("Enter leader id:");
        Integer leader_id = Integer.parseInt(br.readLine());
        System.out.println("Enter category id:");
        Integer category_id = Integer.parseInt(br.readLine());
        try {
            Investigation i = Investigation.addInvestigation(act_date, description, resolved, leader_id, category_id);
            System.out.println("The case has been sucessfully added");
            System.out.print("The case's id is: ");
            System.out.println(i.getId());

        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        } catch (IllegalStateException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void editEmployee() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter investigation id:");
        int id = Integer.parseInt(br.readLine());
        Investigation i = new Investigation();
        try {
            i = InvestigationFinder.getInstance().findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){
            throw new IllegalArgumentException("Investigation with this id doesn't exist");
        }

        List<Employee> employees = WorkOnFinder.getInstance().findByInvestigationId(i.getId());
        System.out.println("Employees working at this case:");
        if(employees.size() != 0){
            for(int j = 0; j < employees.size(); j++){
                PrintEmployee.getInstance().print(employees.get(j));
            }
        }
        else{
            System.out.println("Nobody working at this case");
        }

        System.out.println("Delete one of employees from case press 1 or add new employee on case press 2");
        int emplId;

        switch (br.readLine()) {
            case "1":
                System.out.println("Delete employee with id: ");
                emplId = Integer.parseInt(br.readLine());
                try {
                    WorkOn wo = WorkOnFinder.getInstance().findTupleEmployeeInvestigation(emplId, i.getId());
                    wo.delete();
                    System.out.println("The employee has been successfully deleted");
                } catch (NullPointerException ex){
                    System.out.println("The employee doesn't exist or doesn't work on this case");
                }
                break;

            case "2":
                System.out.println("Insert employee with id: ");
                emplId = Integer.parseInt(br.readLine());
                try {
                    WorkOn.addTuple(emplId, i.getId());
                    System.out.println("The employee has been successfully added");
                    break;
                } catch (IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            default:    System.out.println("Unknown option");
        }
    }

    private void editPersons() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter investigation id:");
        int id = Integer.parseInt(br.readLine());
        Investigation i = new Investigation();
        try {
            i = InvestigationFinder.getInstance().findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){
            throw new IllegalArgumentException("Investigation with this id doesn't exist");
        }
        System.out.println("Delete one of employees from case press 1 or add new employee on case press 2 or 3 to change " +
                "persons role/confirmed person: ");
        int perId;

        switch (br.readLine()) {
            case "1":
                System.out.println("Delete person with id: ");
                perId = Integer.parseInt(br.readLine());
                List<Act> acts = ActFinder.getInstance().findByPersonIdAndInvestigationId(perId, id);
                if(acts.size() == 0){
                    System.out.println("This person doesn't appear in this case");
                    return;
                }
                for(Act a: acts){
                    a.delete();
                }
                System.out.println("All of occurrences was deleted");
                break;

            case "2":
                System.out.println("Insert person with id: ");
                perId = Integer.parseInt(br.readLine());
                System.out.println("Enter address id: ");
                int addressId = Integer.parseInt(br.readLine());
                System.out.println("Enter role: ");
                String role = br.readLine();
                System.out.println("Enter if person is confirmed: (Y/N)");
                boolean confirmed = false;
                if("Y".equals(br.readLine())){
                    confirmed = true;
                }
                try {
                    Act.add(addressId, id, perId, role, confirmed);
                    System.out.println("The person has been successfully added");
                    break;
                } catch (IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                } catch (IllegalStateException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case "3":
                System.out.println("Update person with id: ");
                perId = Integer.parseInt(br.readLine());
                System.out.println("Enter address id: ");
                addressId = Integer.parseInt(br.readLine());
                System.out.println("Enter role : ");
                role = br.readLine();
                System.out.println("Enter if person is confirmed: (Y/N)");
                confirmed = false;
                if("Y".equals(br.readLine())){
                    System.out.println();
                    confirmed = true;
                }
                try {
                    Act.updatePerson(addressId, id, perId,role, confirmed);
                    System.out.println("The person has been successfully updated");
                } catch (IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                } catch (NullPointerException ex){
                    System.out.println(ex.getMessage());
                }
                break;
                default:    System.out.println("Unknown option");
        }
    }


    private void editPlaces() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter investigation id:");
        int id = Integer.parseInt(br.readLine());
        Investigation i = new Investigation();
        try {
            i = InvestigationFinder.getInstance().findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){
            throw new IllegalArgumentException("Investigation with this id doesn't exist");
        }

        List<Act> addresses = ActFinder.getInstance().findByInvestigationIdAddress(i.getId());
        System.out.println("Adresses connected with case:");
        if(addresses.size() != 0){
            for(int j = 0; j < addresses.size(); j++){
                PrintAddress.getInstance().print(AddressFinder.getInstance().findById(addresses.get(j).getAddress_id()));
            }
        }
        else{
            System.out.println("No address is connected");
        }

        System.out.println("Delete one of addresses from case press 1 or add new address on case press 2");
        int addressId;

        switch (br.readLine()) {
            case "1":
                System.out.println("Delete address with id: ");
                addressId = Integer.parseInt(br.readLine());
                try {
                    Act.deleteAdresses(addressId, i.getId());
                    System.out.println("The employee has been successfully deleted");
                } catch (NullPointerException ex){
                    System.out.println("The employee doesn't exist or doesn't work on this case");
                }
                break;

            case "2":
                System.out.println("Insert address with id: ");
                addressId = Integer.parseInt(br.readLine());
                System.out.println("Id of investigation connected with address: ");
                int investigationId = Integer.parseInt(br.readLine());
                System.out.println("Id of person connected with this case and place: ");
                int personId = Integer.parseInt(br.readLine());
                System.out.println("Person's role in case: ");
                String role = br.readLine();
                System.out.println("Person is confirmed in case: (Y/N)");
                boolean confirmed = false;
                if("Y".equals(br.readLine())){
                    confirmed = true;
                }
                try {
                    Act.add(addressId, investigationId, personId, role, confirmed);
                    System.out.println("The employee has been successfully added");
                    break;
                } catch (IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                }  catch (IllegalStateException ex){
                    System.out.println(ex.getMessage());
                }
                break;

            default:    System.out.println("Unknown option");
        }
    }

    private static void closeCase() throws IOException, SQLException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter investigation id:");
        int id = Integer.parseInt(br.readLine());
        Investigation i = InvestigationFinder.getInstance().findById(id);
        if(i == null){
            System.out.println("Case with this id doesn't exist.");
            return;
        } else if(i.isResolved()){
            System.out.println("Case with this id has been already closed.");
            return;
        }
        try{
            CaseClosing.closeCaseMenu(id);
            System.out.println("Case has been successfully closed.");
        } catch(IllegalStateException ex){
            System.out.println(ex.getMessage());
            for(Person p : CaseClosing.allnotPunished){
                try{
                    ImposePunishment.imposePunishment(p.getId(), id);
                } catch (IllegalStateException e){
                    continue;
                }
            }
            Investigation.changeResult(false, id);
            System.out.println("Case has been successfully closed.");
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            for(Person p : CaseClosing.allnotConfirmed){
                PrintPerson.getInstance().print(p);
            }
        } catch(RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void assignInvestigation() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter investigation id:");
        int id = Integer.parseInt(br.readLine());
        try {
            List<Employee> all = AssignInvestigation.assignInvestigation(id);
            for(Employee e : all){
                PrintEmployee.getInstance().print(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
