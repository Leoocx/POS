import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection conectar(){
        File dbFile = new File("database.db");
        boolean bancoExiste = dbFile.exists();
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            if (!bancoExiste){
                System.out.println("BANCO DE DADOS CRIADO!");
            }
            return conn;
        } catch (ClassNotFoundException e){
            throw new RuntimeException("Driver SQLite não encontrado!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados");
        }
    }
}
