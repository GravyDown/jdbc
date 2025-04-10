import java.sql.*;

public class testing {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/crime_system";
        String user = "root";
        String pass = "garvit27";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Add this
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connection successful!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
