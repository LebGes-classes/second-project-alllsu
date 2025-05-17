package org.example;

import java.util.List;

public class Employee {
    private int id;
    private String name;
    private String role;
    private double salary;
    private String status;
    private static int nextId = 1;
    private transient ExcelManager excelManager = new ExcelManager();

    public Employee(int id, String name, String role, double salary, String status) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSalary() {
        return salary;
    }

    public String getStatus() {
        return status;
    }

    public static void updateNextId(int id) {
        if (id >= nextId) nextId = id + 1;
    }

    //Нанять сотрудника
    public static Employee hire(String name, String role) {
        return new Employee(nextId++, name, role, 0.0, "Действующий сотрудник");
    }

    //Уволить сотрудника
    public void fire(List<Warehouse> warehouses) {
        this.status = "Уволен";
        warehouses.forEach(w -> {
            if (w.getResponsibleEmployeeId() != -1 && w.getResponsibleEmployeeId() == this.id) {
                w.assignResponsibleEmployee(-1);
            }
        });
        excelManager.removeRow("Employees", this.id);
    }
}