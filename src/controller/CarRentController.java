/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import Model.CarRentModel;
import Model.CarRentModel.CarDetails;
import Model.CarRentModel.CustomerDetails;
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
import View.CarRent;
import View.CustomerRegistration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CarRentController {
    private CarRentModel model;
    private CarRent view;

    public CarRentController(CarRentModel model, CarRent view) {
        this.model = model;
        this.view = view;
        initView();
    }

    private void initView() {
        view.setRentID(model.getNextRentID());
        view.addTableRows(model.getAllRentDetails());
    }

    public void loadCustomerName(String regID) {
        CustomerDetails customerDetails = model.getCustomerDetailsByRegistrationID(regID);
        if (customerDetails != null) {
            view.setCustomerName(customerDetails.customerName);
        } else {
            view.setCustomerName("");
        }
    }

    public void loadCarDetails(String carID) {
        CarDetails carDetails = model.getCarDetailsByID(carID);
        if (carDetails != null) {
            view.setCarDetails(carDetails.carModel, carDetails.availability, carDetails.price);
        } else {
            view.setCarDetails("", "", 0.0);
        }
    }

    public void addRent(String rentID, String regID, String customerName, String carID, String carModel, String rentDate, String dueDate, double price) {
        model.addRent(rentID, regID, customerName, carID, carModel, rentDate, dueDate, price);
        view.showMessage("Successfully added the data");
        view.addTableRows(model.getAllRentDetails());
        view.clearFields();
    }

    public void deleteRent(String rentID) {
        model.deleteRent(rentID);
        view.showMessage("Row deleted successfully.");
        view.addTableRows(model.getAllRentDetails());
        view.clearFields();
    }

    public void updateRent(String rentID, String regID, String customerName, String carID, String carModel, String rentDate, String dueDate) {
        model.updateRent(rentID, regID, customerName, carID, carModel, rentDate, dueDate);
        view.showMessage("Record updated successfully.");
        view.addTableRows(model.getAllRentDetails());
        view.clearFields();
    }
     public int getNextRentID() {
        return model.getNextRentID();
    }
}
