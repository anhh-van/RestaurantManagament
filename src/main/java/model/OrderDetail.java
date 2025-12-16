package model;

public class OrderDetail {

    private int id;
    private int orderId;
    private int foodId;
    private int qty;
    private double price;

    public OrderDetail() {}

    public OrderDetail(int foodId, int qty, double price) {
        this.foodId = foodId;
        this.qty = qty;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getFoodId() { return foodId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
