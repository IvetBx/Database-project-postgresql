package ui.Print;

import rdg.Punishment.Punishment;

import java.sql.SQLException;

public class PrintPunishment {

    private static final PrintPunishment INSTANCE = new PrintPunishment();

    public static PrintPunishment getInstance() { return INSTANCE; }

    private PrintPunishment() { }

    public void print(Punishment p) throws SQLException {
        if (p == null) {
            throw new NullPointerException("punishment cannot be null");
        }

        System.out.print("Id :  ");
        System.out.print(p.getId());
        if(p.isIs_fine()){
            System.out.println("  Type: fine " + "Range: " + p.getMinimal() + " - " + p.getMaximal() + "€");
        }
        else {
            System.out.println("  Type: warrant");
        }
    }

}
