/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import Model.Car;
import Model.CarDAO;
import View.CarRecords;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Asus
 */
public class CarController {
    private CarRecords view;
    private CarDAO carDAO;

    public CarController(CarRecords view) {
        this.view = view;
        this.carDAO = new CarDAO();
    }

    public void setCarIDAutomatically() {
        try {
            int nextCarID = carDAO.getNextCarID();
            view.setCarID(String.valueOf(nextCarID));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void displayDataFromDatabase() {
        try {
            view.clearTable();
            for (Car car : carDAO.getAllCars()) {
                Object[] row = new Object[]{
                    car.getCarID(),
                    car.getCarModel(),
                    car.getVehicleNo(),
                    car.getPrice(),
                    car.getAvailability(),
                    car.getFuel()
                };
                view.addRowToTable(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addCar() {
        try {
            Car car = new Car();
            car.setCarModel(view.getCarModel());
            car.setVehicleNo(view.getVehicleNo());
            car.setPrice(Double.parseDouble(view.getPrice()));
            car.setAvailability(view.getAvailability());
            car.setFuel(view.getFuel());

            if (carDAO.addCar(car)) {
                JOptionPane.showMessageDialog(view, "Car record added successfully!");
                Object[] row = new Object[]{
                    car.getCarID(),
                    car.getCarModel(),
                    car.getVehicleNo(),
                    car.getPrice(),
                    car.getAvailability(),
                    car.getFuel()
                };
                view.addRowToTable(row);
                resetFields();
                setCarIDAutomatically();
            } else {
                JOptionPane.showMessageDialog(view, "Failed to add car record!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateCar() {
        try {
            Car car = new Car();
            car.setCarID(Integer.parseInt(view.getCarID()));
            car.setCarModel(view.getCarModel());
            car.setVehicleNo(view.getVehicleNo());
            car.setPrice(Double.parseDouble(view.getPrice()));
            car.setAvailability(view.getAvailability());
            car.setFuel(view.getFuel());

            if (carDAO.updateCar(car)) {
                JOptionPane.showMessageDialog(view, "Car record updated successfully!");
                displayDataFromDatabase();
                resetFields();
                setCarIDAutomatically();
            } else {
                JOptionPane.showMessageDialog(view, "Failed to update car record!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteCar() {
        try {
            int carID = Integer.parseInt(view.getCarID());
            if (carDAO.deleteCar(carID)) {
                JOptionPane.showMessageDialog(view, "Car record deleted successfully!");
                displayDataFromDatabase();
                resetFields();
                setCarIDAutomatically();
            } else {
                JOptionPane.showMessageDialog(view, "Failed to delete car record!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void resetFields() {
        view.setCarModel("");
        view.setVehicleNo("");
        view.setPrice("");
        view.setAvailability("Select");
        view.setFuel("Select");
    }
}
