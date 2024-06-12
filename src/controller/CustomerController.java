/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import Model.CustomerModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
/**
 *
 * @author Asus
 */
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Model.CustomerModel;
import View.CustomerRegistration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class CustomerController {
    private CustomerRegistration view;
    private CustomerModel model;
    private final String DB_URL = "jdbc:mysql://localhost:3306/car_rental"; 
    private final String USER = "root"; 
    private final String PASS = ""; 
    private CustomerController controller;
    
    public Connection getConnections() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
    public CustomerController() {
        CustomerModel customerModel = new CustomerModel();
    }

    public CustomerController(CustomerRegistration view) {
        this.view = view;
        this.model = new CustomerModel();
    }

    public void autoIncrementRegistrationID() {
        int maxID = model.getMaxRegistrationID();
        view.setRegistrationID(String.valueOf(maxID + 1));
    }

    public void addCustomer() {
        /**if (view.getRegistrationID().isEmpty() || view.getCustomerName().isEmpty() || view.getCustomerID().isEmpty() || view.getLicence().isEmpty() ||
            view.getTel().isEmpty() || view.getAddress().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Enter all the data", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }**/

        String registrationNo = view.getRegistrationID();
        String customerName = view.getCustomerName();
        String idNo = view.getCustomerID();
        String drivingLicenceNo = view.getLicence();
        String telephone = view.getTel();
        String addressText = view.getAddress();

        model.addCustomer(registrationNo,customerName, idNo, drivingLicenceNo, telephone, addressText, view.getCustomerTableModel());
        view.clearField();
    }

    public void loadCustomerData() {
        model.loadCustomerData(view.getCustomerTableModel());
    }

    public void updateCustomer() {
        /**if (view.getCustomerName().isEmpty() || view.getCustomerID().isEmpty() || view.getLicence().isEmpty() ||
            view.getTel().isEmpty() || view.getAddress().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Enter all the data", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }**/
        
        int registrationNo = Integer.parseInt(view.getRegistrationID());
        String customerName = view.getCustomerName();
        String idNo = view.getCustomerID();
        String drivingLicenceNo = view.getLicence();
        String telephone = view.getTel();
        String addressText = view.getAddress();

        model.updateCustomer(registrationNo, customerName, idNo, drivingLicenceNo, telephone, addressText);
        loadCustomerData();
        view.clearField();
    }

    public void deleteCustomer() {
        int selectedRow = view.getCustomerTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a row to delete");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this record?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            int registrationNo = Integer.parseInt(view.getRegistrationID());
            model.deleteCustomer(registrationNo, view.getCustomerTableModel(), selectedRow);
            view.clearField();
        }
    }
}
