package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

public class MainController {
    
    @FXML private Label totalCarsLabel;
    @FXML private Label activeRentalsLabel;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    
    // ตารางฝั่ง Admin
    @FXML private TableView<Car> tableView;
    @FXML private TableColumn<Car, String> colId, colModel, colPlate, colType, colPrice, colStatus, colCustomer;
    
    // ตารางฝั่ง User (ใช้ร่วมกันทั้งหน้า Rent และหน้า My Bookings)
    @FXML private TableView<Car> userCarTable;
    @FXML private TableColumn<Car, String> uColBrand, uColPlate, uColType, uColPrice;

    // 🔥 โค้ดที่ทำให้ระบบ "แน่น" ขึ้น: ใช้คำว่า static เพื่อให้ข้อมูลคงอยู่ตลอดการเปิดโปรแกรม ไม่รีเซ็ตตอนสลับหน้า
    private static ObservableList<Car> globalCarList = FXCollections.observableArrayList(
        new Car("1", "Toyota Camry", "กข-123", "Sedan", "1,500", "Rented", "Suthema Pasa"),
        new Car("2", "Honda Civic", "ชล-999", "Sedan", "1,200", "Rented", "Suriya Nakkhasen"),
        new Car("3", "Tesla Model 3", "EV-001", "Electric", "3,000", "Available", "-"),
        new Car("4", "Ford Ranger", "ขก-56", "Pickup", "2,000", "Available", "-"),
        new Car("5", "BMW Series 5", "กทม-111", "Sedan", "2,500", "Available", "-")
    );
    
    // ============================================
    // ระบบ Login
    // ============================================
    @FXML void onLoginClick(ActionEvent event) {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();
        try {
            if (user.equals("admin") && pass.equals("123")) {
                switchScene(event, "/application/admin/AdminDashboard.fxml", "RentGo - Admin");
            } else if (user.equals("user") && pass.equals("123")) {
                switchScene(event, "/application/user/UserBooking.fxml", "RentGo - Customer");
            } else {
                new Alert(AlertType.ERROR, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchScene(ActionEvent event, String fxmlFile, String title) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
    
    // ============================================
    // เมนูฝั่ง Admin & User
    // ============================================
    @FXML public void switchToDashboard(ActionEvent event) throws Exception { switchScene(event, "/application/admin/AdminDashboard.fxml", "Admin - Dashboard"); }
    @FXML public void switchToInventory(ActionEvent event) throws Exception { switchScene(event, "/application/admin/AdminInventory.fxml", "Admin - Inventory"); }
    @FXML public void switchToBookingRental(ActionEvent event) throws Exception { switchScene(event, "/application/admin/AdminBookingRental.fxml", "Admin - Booking"); }
    @FXML public void switchToCustomers(ActionEvent event) throws Exception { switchScene(event, "/application/admin/AdminCustomers.fxml", "Admin - Customers"); }
    @FXML public void switchToReports(ActionEvent event) throws Exception { switchScene(event, "/application/admin/AdminReports.fxml", "Admin - Reports"); }

    @FXML public void switchToUserDashboard(ActionEvent event) throws Exception { switchScene(event, "/application/user/UserBooking.fxml", "User - Dashboard"); }
    @FXML public void switchToUserRentCar(ActionEvent event) throws Exception { switchScene(event, "/application/user/UserRentCar.fxml", "User - Rent a Car"); }
    @FXML public void switchToUserBookings(ActionEvent event) throws Exception { switchScene(event, "/application/user/UserMyBookings.fxml", "User - My Bookings"); }
    @FXML public void switchToUserHistory(ActionEvent event) throws Exception { switchScene(event, "/application/user/UserHistory.fxml", "User - History"); }
    @FXML public void switchToUserPayments(ActionEvent event) throws Exception { switchScene(event, "/application/user/UserPayments.fxml", "User - Payments"); }
    @FXML public void switchToUserProfile(ActionEvent event) throws Exception { switchScene(event, "/application/user/UserProfile.fxml", "User - Profile"); }

    @FXML void onLogoutClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600)); 
            stage.setTitle("Login - RentGo");
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ============================================
    // 🔥 ลอจิกการ จองรถ (ดึงออกสต็อก)
    // ============================================
 // ============================================
    // 🔥 ลอจิกการ จองรถ (แก้ไข Alert ให้ดึง CSS มาใช้)
    // ============================================
    @FXML void onBookCarClick(ActionEvent event) {
        Car selectedCar = userCarTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            selectedCar.setStatus("Rented");
            selectedCar.setCustomer("Customer User"); 
            refreshUserRentTable();
            
            // สร้างหน้าต่างแจ้งเตือน
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Booking Success");
            alert.setHeaderText("Success!");
            alert.setContentText("Booking Confirmed!\nYou have booked: " + selectedCar.getModel());
            
            // สั่งให้ดึงไฟล์ CSS มาแต่งหน้าต่าง (ให้เป็น Dark Mode)
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            alert.showAndWait();
            
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("No Car Selected");
            alert.setContentText("Please select a car from the table first.");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            alert.show();
        }
    }

    // ============================================
    // 🔥 ลอจิกการ คืนรถ (แก้ไข Alert ให้ดึง CSS มาใช้)
    // ============================================
    @FXML void onReturnCarClick(ActionEvent event) {
        Car selectedCar = userCarTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            selectedCar.setStatus("Available");
            selectedCar.setCustomer("-"); 
            refreshUserBookingsTable();
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Return Success");
            alert.setHeaderText("Returned!");
            alert.setContentText("Car Returned Successfully!\nThank you for using RentGo.");
            
            // สั่งให้ดึงไฟล์ CSS มาแต่งหน้าต่าง
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            alert.showAndWait();
            
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("No Car Selected");
            alert.setContentText("Please select a car you want to return.");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            alert.show();
        }
    }
    // ============================================
    // การตั้งค่าตอนเปิดหน้าจอ (Initialize)
    // ============================================
    public void initialize() {
        // 1. โหลดข้อมูลฝั่ง Admin
        if (tableView != null) { 
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
            colPlate.setCellValueFactory(new PropertyValueFactory<>("plate"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
            
            colStatus.setCellFactory(column -> new TableCell<Car, String>() { 
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) { setText(null); setStyle(""); } 
                    else {
                        setText(item);
                        if (item.equalsIgnoreCase("Available")) setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                        else if (item.equalsIgnoreCase("Rented")) setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                        else setStyle("-fx-text-fill: white;");
                    }
                }
            });
            tableView.setItems(globalCarList); // ใช้ข้อมูลส่วนกลาง
            
            if (totalCarsLabel != null) totalCarsLabel.setText(String.valueOf(globalCarList.size()));
            if (activeRentalsLabel != null) {
                long rentedCount = globalCarList.stream().filter(car -> "Rented".equalsIgnoreCase(car.getStatus())).count();
                activeRentalsLabel.setText(String.valueOf(rentedCount));
            }
        }

        // 2. โหลดข้อมูลฝั่ง User (เช็คว่าเป็นหน้า Rent Car หรือหน้า My Bookings)
        if (userCarTable != null) {
            uColBrand.setCellValueFactory(new PropertyValueFactory<>("model"));
            
            // เช็คจากชื่อคอลัมน์สุดท้าย ว่าเป็นหน้าจองรถ หรือหน้าดูประวัติที่จองไว้
            if (uColPrice.getText().contains("Price")) {
                // ---> หน้า Rent a Car (ดึงเฉพาะรถว่าง)
                uColPlate.setCellValueFactory(new PropertyValueFactory<>("plate"));
                uColType.setCellValueFactory(new PropertyValueFactory<>("type"));
                uColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
                refreshUserRentTable();
            } else {
                // ---> หน้า My Bookings (จัดโครงสร้างตารางใหม่ด้วยโค้ด และดึงเฉพาะรถตัวเอง)
                uColPlate.setText("License Plate");
                uColPlate.setCellValueFactory(new PropertyValueFactory<>("plate"));
                
                uColType.setText("Price/Day");
                uColType.setCellValueFactory(new PropertyValueFactory<>("price"));
                
                uColPrice.setText("Status");
                uColPrice.setCellValueFactory(new PropertyValueFactory<>("status"));
                uColPrice.setCellFactory(column -> new TableCell<Car, String>() {
                    @Override protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) { setText(null); setStyle(""); } 
                        else {
                            setText(item);
                            setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;"); // สีแดงโชว์ว่ากำลังเช่าอยู่
                        }
                    }
                });
                refreshUserBookingsTable();
            }
        }
    }

    // ฟังก์ชันช่วยรีเฟรชตารางหน้า เลือกรถ
    private void refreshUserRentTable() {
        ObservableList<Car> availableCars = FXCollections.observableArrayList();
        for (Car c : globalCarList) {
            if (c.getStatus().equalsIgnoreCase("Available")) availableCars.add(c);
        }
        userCarTable.setItems(availableCars);
    }

    // ฟังก์ชันช่วยรีเฟรชตารางหน้า รถของฉัน
    private void refreshUserBookingsTable() {
        ObservableList<Car> myCars = FXCollections.observableArrayList();
        for (Car c : globalCarList) {
            if (c.getStatus().equalsIgnoreCase("Rented") && "Customer User".equals(c.getCustomer())) myCars.add(c);
        }
        userCarTable.setItems(myCars);
    }
}