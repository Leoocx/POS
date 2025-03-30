import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login{
    private String username;
    private String password;

    private Connection connection;

    public Login(Connection connection){
        this.connection=connection;
    }
    public void loginButton() throws SQLException{
        if (!username.isBlank() && !password.isBlank()){
            validarLogin();
        }
        else{
            System.out.println("Entre com usuario e senha.");
        }
    }

    public void validarLogin() throws SQLException {
        String sql="SELECT FROM admin WHERE username = ? AND password = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs= ps.executeQuery()){
            if(rs.next()) {
                rs.getString("username");
                rs.getString("password");
            }
        }
    }
}
