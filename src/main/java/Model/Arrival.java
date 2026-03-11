package Model;

import java.util.LinkedList;
import java.util.Random;

public class Arrival extends Thread{
    private Random random = new Random();

    public Arrival() {
    }

    public Event moveQueue(boolean isVIP) {
        Customer customer = new Customer(isVIP, 1 + random.nextInt(3), random.nextBoolean(), random.nextBoolean());
        Event event = null;
        if (isVIP) {
            event = new Event(Clock.getInstance().getCurrentTime(), EventType.START_VIP_SECURITY, customer);

        } else if (!isVIP) {
            event = new Event(Clock.getInstance().getCurrentTime(), EventType.START_GA_SECURITY, customer);
        } return event;
    }

}
