package ui.Menu;

import DbConnect.DbContext;
import Transaction.CaseClosing;
import Transaction.ImposePunishment;
import rdg.Punishment.Punishment;
import rdg.Punishment.PunishmentFinder;
import ui.Print.PrintPunishment;

import java.awt.image.renderable.RenderableImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public class PunishmentsMenu extends Menu {
    @Override
    public void print() {
        System.out.println("************************************************");
        System.out.println("* 1. list of punishments                       *");
        System.out.println("* 2. add punishment                            *");
        System.out.println("* 3. impose punishment                         *");
        System.out.println("* 4. exit                                      *");
        System.out.println("************************************************");
    }

    @Override
    public void handle(String option) throws IOException, SQLException {
        switch (option) {
            case "1":   listAllPunishments(); break;
            case "2":   addPunishment(); break;
            case "3":   imposePunishment(); break;
            case "4":   exit(); MainMenu m = new MainMenu(); m.run(); break;
            default:    System.out.println("Unknown option"); break;
        }
    }

    private void listAllPunishments() throws SQLException{
        for (Punishment p : PunishmentFinder.getInstance().findAll()) {
            PrintPunishment.getInstance().print(p);
        }
    }

    private void addPunishment() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("To adding warrant press 1, to adding new fine press 2");
        switch (br.readLine()) {

            case "1":
                try {
                    Punishment.addWarrant();
                } catch (IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                    return;
                }
                System.out.println("Succesfully added");
                break;

            case "2":
                System.out.println("Enter minimal amount:");
                Integer minimal = Integer.parseInt(br.readLine());
                System.out.println("Enter maximal amount:");
                Integer maximal = Integer.parseInt(br.readLine());
                try {
                    Punishment.addFine(minimal, maximal);
                } catch (IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                    return;
                }
                System.out.println("Succesfully added");
                break;

            default:    System.out.println("Unknown option"); break;
        }
    }

    private void imposePunishment() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter investigation id: ");
        int investigationId = Integer.parseInt(br.readLine());
        System.out.println("Enter person id: ");
        int personId = Integer.parseInt(br.readLine());
        try {

            int id = ImposePunishment.imposePunishment(personId, investigationId);
            System.out.println("Person's punishment: ");
            PrintPunishment.getInstance().print(PunishmentFinder.getInstance().findById(id));
        }  catch(IllegalStateException e){
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

}
