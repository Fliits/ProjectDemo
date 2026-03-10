package Model;

import java.util.PriorityQueue;

public class EventList {

    private PriorityQueue<Event> events;

    public EventList() {
        events = new PriorityQueue<>();
    }

    public void add(Event event) {
        events.add(event);
    }

    public Event remove() {
        return events.poll();
    }

    public Event peek() {
        return events.peek();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public int size() {
        return events.size();
    }
}

