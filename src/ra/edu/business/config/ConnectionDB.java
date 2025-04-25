package ra.edu.business.config;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionDB {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/managerCandidate";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "123456$";
    public static Connection openConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.out.println("Xay ra loi trong qua trinh ket noi voi csdl "+e.getMessage());
        } catch (Exception ex) {
            System.out.println("Xay ra loi khong xac dinh "+ex.getMessage());
        }
        return conn;
    }
    public static void closeConnection(Connection conn, CallableStatement cs) {
        if (cs != null) {
            try {
                cs.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}