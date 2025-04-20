package dao;

import model.Officer;
import java.sql.*;

public class OfficerDAO {
    public static Officer getOfficerByUserId(int userId) {
        Officer officer = null;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cras", "root", "garvit27");
            String sql = "SELECT * FROM officer WHERE officer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                officer = new Officer();
                officer.setOfficerId(rs.getLong("officer_id"));
                officer.setOfficerKey(rs.getString("officer_key"));
                officer.setOfficerName(rs.getString("officer_name"));
                officer.setOfficerPhone(rs.getString("officer_phone"));
                officer.setOfficerRegion(rs.getString("officer_region"));
                officer.setStarRating(rs.getInt("star_rating"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return officer;
    }
}
