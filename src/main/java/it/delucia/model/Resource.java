package it.delucia.model;

public class Resource {
    private String name;
    private int quantity;
    private int id;

    public Resource(int id, String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
