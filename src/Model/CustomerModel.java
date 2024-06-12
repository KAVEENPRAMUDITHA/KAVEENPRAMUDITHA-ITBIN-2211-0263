/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

/**
 *
 * @author Asus
 */
public class CustomerModel {
  
    private final String DB_URL = "jdbc:mysql://localhost:3306/car_rental";
    private final String USER = "root";
    private final String PASS = "";

    public int getMaxRegistrationID() {
        int maxID = 0;
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT MAX(registrationID) FROM customers";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                maxID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxID;
    }
    public CustomerModel() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(String registrationID, String customerName, String idNo, String drivingLicenceNo, String telephone, String addressText, DefaultTableModel model) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "INSERT INTO customers (registrationID, customerName, idNo, drivingLicenceNo, telephone, address) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, registrationID);
            pst.setString(2, customerName);
            pst.setString(3, idNo);
            pst.setString(4, drivingLicenceNo);
            pst.setString(5, telephone);
            pst.setString(6, addressText);
            pst.executeUpdate();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedID = generatedKeys.getInt(1);
                model.addRow(new Object[]{generatedID, customerName, idNo, drivingLicenceNo, telephone, addressText});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadCustomerData(DefaultTableModel model) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT registrationID, customerName, idNo, drivingLicenceNo, telephone, address FROM customers";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("registrationID"),
                    rs.getString("customerName"),
                    rs.getString("idNo"),
                    rs.getString("drivingLicenceNo"),
                    rs.getString("telephone"),
                    rs.getString("address")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomer(int registrationNo, String customerName, String idNo, String drivingLicenceNo, String telephone, String addressText) {
        String query = "UPDATE customers SET customerName = ?, idNo = ?, drivingLicenceNo = ?, telephone = ?, address = ? WHERE registrationID = ?";
    
    try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement pst = con.prepareStatement(query)) {
         
        pst.setString(1, customerName);
        pst.setString(2, idNo);
        pst.setString(3, drivingLicenceNo);
        pst.setString(4, telephone);
        pst.setString(5, addressText);
        pst.setInt(6, registrationNo);
        
        int affectedRows = pst.executeUpdate();
        
        if (affectedRows == 0) {
            throw new SQLException("Updating customer failed, no rows affected.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    public void deleteCustomer(int registrationNo, DefaultTableModel model, int selectedRow) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "DELETE FROM customers WHERE registrationID=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, registrationNo);
            pst.executeUpdate();
            model.removeRow(selectedRow);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


