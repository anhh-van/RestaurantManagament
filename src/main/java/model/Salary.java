package model;

public class Salary {
    private int id;
    private int employeeId;
    private double baseSalary;
    private double bonus;
    private double total;
    private int month;
    private int year;

    public Salary(int id, int employeeId, double baseSalary, double bonus, double total, int month, int year) {
        this.id = id;
        this.employeeId = employeeId;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.total = total;
        this.month = month;
        this.year = year;
    }

    public int getId() { return id; }
    public int getEmployeeId() { return employeeId; }
    public double getBaseSalary() { return baseSalary; }
    public double getBonus() { return bonus; }
    public double getTotal() { return total; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
}
