/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Asus
 */
public class CarRentModel {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");
    }

    public int getNextRentID() {
        int nextRentID = 1; // Default value if no records exist
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "")) {
            String query = "SELECT MAX(rentID) AS max_rentID FROM rentcar";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                nextRentID = rs.getInt("max_rentID") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextRentID;
    }

    public CustomerDetails getCustomerDetailsByRegistrationID(String regID) {
        CustomerDetails customerDetails = null;
        try (Connection con = getConnection()) {
            String query = "SELECT customerName FROM customers WHERE registrationID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, regID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                customerDetails = new CustomerDetails(rs.getString("customerName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerDetails;
    }

    public CarDetails getCarDetailsByID(String carID) {
        CarDetails carDetails = null;
        try (Connection con = getConnection()) {
            String query = "SELECT carModel, availability, price FROM cars WHERE carID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, carID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                carDetails = new CarDetails(rs.getString("carModel"), rs.getString("availability"), rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carDetails;
    }

    public void addRent(String rentID, String regID, String customerName, String carID, String carModel, String rentDate, String dueDate, double price) {
        try (Connection con = getConnection()) {
            String query = "INSERT INTO rentcar (rentID, registationID, customerName, carID, carModel, rentDate, dueDate, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, rentID);
            pst.setString(2, regID);
            pst.setString(3, customerName);
            pst.setString(4, carID);
            pst.setString(5, carModel);
            pst.setString(6, rentDate);
            pst.setString(7, dueDate);
            pst.setDouble(8, price);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRent(String rentID) {
        try (Connection con = getConnection()) {
            String query = "DELETE FROM rentcar WHERE rentID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, rentID);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRent(String rentID, String regID, String customerName, String carID, String carModel, String rentDate, String dueDate) {
        /**try (Connection con = getConnection()) {
            String query = "UPDATE rentcar SET registationID=?, customerName=?, carID=?, carModel=?, rentDate=?, dueDate=? WHERE rentID=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, regID);
            pst.setString(2, customerName);
            pst.setString(3, carID);
            pst.setString(4, carModel);
            pst.setString(5, rentDate);
            pst.setString(6, dueDate);
            pst.setString(7, rentID);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } **/
    }

    public List<RentDetails> getAllRentDetails() {
        List<RentDetails> rentDetailsList = new ArrayList<>();
        try (Connection con = getConnection()) {
            String query = "SELECT rentID, registationID, customerName, carID, carModel, rentDate, dueDate FROM rentcar";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                rentDetailsList.add(new RentDetails(rs.getInt("rentID"), rs.getInt("registationID"), rs.getString("customerName"),
                        rs.getInt("carID"), rs.getString("carModel"), rs.getDate("rentDate"), rs.getDate("dueDate")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentDetailsList;
    }

    public static class CustomerDetails {
        public String customerName;

        public CustomerDetails(String customerName) {
            this.customerName = customerName;
        }
    }

    public static class CarDetails {
        public String carModel;
        public String availability;
        public double price;

        public CarDetails(String carModel, String availability, double price) {
            this.carModel = carModel;
            this.availability = availability;
            this.price = price;
        }
    }

    public static class RentDetails {
        public int rentID;
        public int regID;
        public String customerName;
        public int carID;
        public String carModel;
        public java.util.Date rentDate;
        public java.util.Date dueDate;

        public RentDetails(int rentID, int regID, String customerName, int carID, String carModel, java.util.Date rentDate, java.util.Date dueDate) {
            this.rentID = rentID;
            this.regID = regID;
            this.customerName = customerName;
            this.carID = carID;
            this.carModel = carModel;
            this.rentDate = rentDate;
            this.dueDate = dueDate;
        }
    }
}
