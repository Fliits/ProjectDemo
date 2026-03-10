package Model;

public class Event implements Comparable<Event> {

    private double time;
    private EventType type;
    private Customer customer;

    public Event(double time, EventType type, Customer customer) {
        this.time = time;
        this.type = type;
        this.customer = customer;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }

    public Customer getCustomer() {
        return customer;
    }
}