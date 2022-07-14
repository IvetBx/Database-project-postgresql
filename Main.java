import DbConnect.DbContext;
import org.postgresql.ds.PGSimpleDataSource;
import ui.Menu.MainMenu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, IOException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setServerName("db.dai.fmph.uniba.sk");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("playground");
        dataSource.setUser("balintova37@uniba.sk");
        dataSource.setPassword("");

        try (Connection connection = dataSource.getConnection()) {
            DbContext.setConnection(connection);
            MainMenu mainMenu = new MainMenu();
            mainMenu.run();

        } finally {
            DbContext.clear();
        }
    }
}
