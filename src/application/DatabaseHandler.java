package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    public static int getTotalMembers() {
        int totalMembers = 0;
        try (Connection connection = MysqlConnection.getDBConnection()) {
            System.out.println("Connection established successfully for getTotalMembers");
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM members where status = 'paid' ");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalMembers = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMembers;
    }

    public static int getTotalRevenue() {
        int totalRevenue = 0;
        try (Connection connection = MysqlConnection.getDBConnection()) {
            System.out.println("Connection established successfully for getTotalRevenue");
            PreparedStatement statement = connection.prepareStatement("SELECT SUM(amount) FROM members WHERE members.status = 'paid'");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalRevenue = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    public static List<Members> getNotPaidMembers() {
        List<Members> notPaidMembers = new ArrayList<>();

        try (Connection connection = MysqlConnection.getDBConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM members WHERE status = 'not paid'")) {

            while (resultSet.next()) {
                // Extract data from the result set and create Members objects
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String gender = resultSet.getString("gender");
                String status = resultSet.getString("status");
                int numPhone = resultSet.getInt("numPhone");
                int amount = resultSet.getInt("amount");
                int duree = resultSet.getInt("duree");
                int age = resultSet.getInt("age");

                LocalDate endDate = null;
                java.sql.Date sqlEndDate = resultSet.getDate("endDate");
                if (sqlEndDate != null) {
                    endDate = sqlEndDate.toLocalDate();
                }
                
                LocalDate startDate = null;
                java.sql.Date sqlstartDate = resultSet.getDate("startDate");
                if (sqlEndDate != null) {
                    startDate = sqlstartDate.toLocalDate();
                }

                Members member = new Members(id, nom, prenom, gender, status, numPhone, amount, duree, age, endDate, startDate);
                notPaidMembers.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notPaidMembers;
    }
    
    public static void updateEndDateForAllMembers() {
        try (Connection connection = MysqlConnection.getDBConnection()) {
            System.out.println("Connection established successfully for updateEndDateForAllMembers");

            try (CallableStatement callableStatement = connection.prepareCall("{call updateEndDateForAllMembersProcedure()}")) {
                // Execute the stored procedure
                callableStatement.execute();
                System.out.println("endDate updated for all members.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMemberStatus() {
        try (Connection connection = MysqlConnection.getDBConnection()) {
            System.out.println("Connection established successfully for updateMemberStatus");

            try (CallableStatement callableStatement = connection.prepareCall("{call updateMemberStatusProcedure()}")) {
                // Execute the stored procedure
                callableStatement.execute();
                System.out.println("Member statuses updated based on endDate.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
