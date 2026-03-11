package application;

public class Car {
    private String id;
    private String model;
    private String plate;
    private String type;
    private String price;
    private String status;
    private String customer;

    // Constructor (สร้างออบเจ็กต์)
    public Car(String id, String model, String plate, String type, String price, String status, String customer) {
        this.id = id;
        this.model = model;
        this.plate = plate;
        this.type = type;
        this.price = price;
        this.status = status;
        this.customer = customer;
    }

    // =====================================
    // Getters (สำหรับดึงข้อมูลไปโชว์ในตาราง)
    // =====================================
    public String getId() { return id; }
    public String getModel() { return model; }
    public String getPlate() { return plate; }
    public String getType() { return type; }
    public String getPrice() { return price; }
    public String getStatus() { return status; }
    public String getCustomer() { return customer; }

    // =====================================
    // Setters (สำหรับแก้ไขข้อมูลตอนจอง/คืนรถ) 🔥 ส่วนที่เพิ่มเข้ามา
    // =====================================
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public void setCustomer(String customer) { 
        this.customer = customer; 
    }
    
    // (เพิ่มเผื่อไว้ใช้อัปเดตข้อมูลรถในอนาคต)
    public void setPrice(String price) { this.price = price; }
    public void setPlate(String plate) { this.plate = plate; }
}