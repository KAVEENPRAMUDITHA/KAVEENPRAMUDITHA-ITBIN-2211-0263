/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asus
 */
public class CarDAO {
    private final String url = "jdbc:mysql://localhost/car_rental";
    private final String user = "root";
    private final String password = "";

    public List<Car> getAllCars() throws SQLException {
        List<Car> cars = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM cars";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Car car = new Car();
                car.setCarID(resultSet.getInt("carID"));
                car.setCarModel(resultSet.getString("carModel"));
                car.setVehicleNo(resultSet.getString("vehicleNo"));
                car.setPrice(resultSet.getDouble("price"));
                car.setAvailability(resultSet.getString("availability"));
                car.setFuel(resultSet.getString("fuel"));
                cars.add(car);
            }
        }
        return cars;
    }

    public int getNextCarID() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT MAX(carID) FROM cars";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) + 1;
            } else {
                return 1;
            }
        }
    }

    public boolean addCar(Car car) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO cars (carModel, vehicleNo, price, fuel, availability) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, car.getCarModel());
            statement.setString(2, car.getVehicleNo());
            statement.setDouble(3, car.getPrice());
            statement.setString(4, car.getFuel());
            statement.setString(5, car.getAvailability());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    car.setCarID(generatedKeys.getInt(1));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean updateCar(Car car) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE cars SET carModel=?, vehicleNo=?, price=?, availability=?, fuel=? WHERE carID=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, car.getCarModel());
            statement.setString(2, car.getVehicleNo());
            statement.setDouble(3, car.getPrice());
            statement.setString(4, car.getAvailability());
            statement.setString(5, car.getFuel());
            statement.setInt(6, car.getCarID());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteCar(int carID) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM cars WHERE carID=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, carID);
            return statement.executeUpdate() > 0;
        }
    }
}
