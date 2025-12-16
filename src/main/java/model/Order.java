package model;

import java.util.Date;

public class Order {
    private int id;
    private int employeeId;
    private double total;
    private Date createdAt;

    public Order() {}

    public Order(int id, int employeeId, double total, Date createdAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.total = total;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
