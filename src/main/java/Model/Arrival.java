package Model;

import java.util.LinkedList;
import java.util.Random;

public class Arrival extends Thread{
    private Random random = new Random();

    public Arrival() {
    }

    public Customer insertCustomerToQueue(boolean isVip) {
        Customer customer = new Customer(isVip, random.nextInt(3), random.nextBoolean(), random.nextBoolean());
        if (customer.isLippu()) {
            customer.setLippu(true);
        }
        return customer;
    }
}
