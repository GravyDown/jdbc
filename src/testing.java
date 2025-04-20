import java.sql.*;

public class testing{
    public static void main(String[] args) throws ClassNotFoundException,SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gravity","root","garvit27");
        System.out.println("Connection Successful!");

        Statement st = con.createStatement();
        int rs = st.executeUpdate("alter table gravity rename to testing");

        if(rs>0){
            System.out.println("Rename successful...");
        }

    }
}