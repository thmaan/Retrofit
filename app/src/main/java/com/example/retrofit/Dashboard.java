package com.example.retrofit;

public class Dashboard {
    private int total_customers;
    private int total_orders;
    private int delivered;
    private int pending;

    public Dashboard(int total_customers, int total_orders, int delivered, int pending) {
        this.total_customers = total_customers;
        this.total_orders = total_orders;
        this.delivered = delivered;
        this.pending = pending;
    }

    public int getTotal_customers() {
        return total_customers;
    }

    public int getTotal_orders() {
        return total_orders;
    }

    public int getDelivered() {
        return delivered;
    }

    public int getPending() {
        return pending;
    }
}
