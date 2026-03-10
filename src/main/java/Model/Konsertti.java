package Model;

import java.util.ArrayList;

public class Konsertti {
    private ArrayList<Customer> customers;

    public Konsertti() {
        customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}
