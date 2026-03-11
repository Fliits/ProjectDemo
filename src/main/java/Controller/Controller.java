package Controller;

import Model.*;

import java.util.LinkedList;

import java.util.Random;

public class Controller extends Thread {
    static Random random = new Random();
    Arrival entry;
    static EventList eventList;
    private int työntekijäMäärä;
    private int gaAsiakasmäärä = 0;
    private int gaKävijämäärä;
    private int vipAsiakasmäärä = 0;
    private int vipKävijämäärä;
    public int simulaationKesto;
    private ServicePoint vipSecurity;
    private ServicePoint gaSecurity;
    private ServicePoint vipNarikka;
    private ServicePoint gaNarikka;
    private ServicePoint merch;

    public Controller(int simulaationKesto, int työntekijäMäärä, int gaKävijämäärä, int vipKävijämäärä) {
        eventList = new EventList();
        entry = new Arrival();
        this.simulaationKesto = simulaationKesto;
        this.työntekijäMäärä = työntekijäMäärä;
        this.gaKävijämäärä = gaKävijämäärä;
        this.vipKävijämäärä = vipKävijämäärä;
        vipSecurity = new ServicePoint(10);
        gaSecurity = new ServicePoint(30);
        vipNarikka = new ServicePoint(10);
        gaNarikka = new ServicePoint(20);
        merch = new ServicePoint(30);
    }

    public void handleEvent(Event event) {
            EventType type = event.getType();
            Customer customer = event.getCustomer();
            int time = (int) event.getTime();
            if (Clock.getInstance().getCurrentTime() == time) {
                if (type == EventType.START_VIP_SECURITY) {
                    if (vipSecurity.isAvailable()) {
                        vipSecurity.setAvailable(false);
                        eventList.add(new Event((Clock.getInstance().getCurrentTime() + ((työntekijäMäärä - vipSecurity.getTyöntekijäMäärä()) * 50)), EventType.FINISH_VIP_SECURITY, customer));
                        System.out.println("VIP Security tarkastaa asiakkaan, tarkastuksessa kuluu " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                    } else {
                        eventList.add(event);
                    }
                } else if (type == EventType.FINISH_VIP_SECURITY) {
                    vipSecurity.setAvailable(true);
                    if (event.getCustomer().isKäyNarikassa()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_VIP_CLOAKROOM, customer));
                    } else if (event.getCustomer().isOstaako()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH, customer));
                    } else if (!event.getCustomer().isOstaako() && !event.getCustomer().isKäyNarikassa()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL, customer));
                    }
                } else if (type == EventType.START_VIP_CLOAKROOM) {
                    if (vipNarikka.isAvailable()) {
                        vipNarikka.setAvailable(false);
                        eventList.add(new Event((Clock.getInstance().getCurrentTime() + ((työntekijäMäärä - vipNarikka.getTyöntekijäMäärä()) * 50)), EventType.FINISH_VIP_CLOAKROOM, customer));
                        System.out.println("VIP Narikka käsittelee asiakkaan, siinä kesti " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                    } else {
                        eventList.add(event);
                    }
                } else if (type == EventType.FINISH_VIP_CLOAKROOM) {
                    vipNarikka.setAvailable(true);
                    if (event.getCustomer().isOstaako()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH, customer));
                    } else if (!event.getCustomer().isOstaako() && !event.getCustomer().isKäyNarikassa()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL, customer));
                    }
                } else if (type == EventType.START_GA_SECURITY) {
                    if (gaSecurity.isAvailable()) {
                        gaSecurity.setAvailable(false);
                        eventList.add(new Event((Clock.getInstance().getCurrentTime() + (työntekijäMäärä - gaSecurity.getTyöntekijäMäärä()) * 50), EventType.FINISH_GA_SECURITY, customer));
                        System.out.println("GA Security tarkastaa asiakkaan, tarkastuksessa kuluu " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                    } else {
                        eventList.add(event);
                    }
                } else if (type == EventType.FINISH_GA_SECURITY) {
                    gaSecurity.setAvailable(true);
                    if (event.getCustomer().isKäyNarikassa()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_GA_CLOAKROOM, customer));
                    } else if (event.getCustomer().isOstaako()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH, customer));
                    } else if (!event.getCustomer().isOstaako() && !event.getCustomer().isKäyNarikassa()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL, customer));
                    }
                } else if (type == EventType.START_GA_CLOAKROOM) {
                    if (gaNarikka.isAvailable()) {
                        gaNarikka.setAvailable(false);
                        eventList.add(new Event((Clock.getInstance().getCurrentTime() + (työntekijäMäärä - gaNarikka.getTyöntekijäMäärä()) * 50), EventType.FINISH_GA_CLOAKROOM, customer));
                        System.out.println("GA Narikka käsittelee asiakkaan, siinä kesti " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                    } else {
                        eventList.add(event);
                    }
                } else if (type == EventType.FINISH_GA_CLOAKROOM) {
                    gaNarikka.setAvailable(true);
                    if (event.getCustomer().isOstaako()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH, customer));
                    } else if (!event.getCustomer().isOstaako() && !event.getCustomer().isKäyNarikassa()) {
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL, customer));
                    }
                } else if (type == EventType.START_MERCH) {
                    if (merch.isAvailable()) {
                        merch.setAvailable(false);
                        eventList.add(new Event((Clock.getInstance().getCurrentTime() + (työntekijäMäärä - merch.getTyöntekijäMäärä()) * 50), EventType.FINISH_MERCH, customer));
                        System.out.println("Oheistuotemyyntipiste palvelee asiakkaan, mihin kului " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                    } else {
                        eventList.add(event);
                    }
                } else if (type == EventType.FINISH_MERCH) {
                    merch.setAvailable(true);
                    eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL, customer));
                }
            } else {
                eventList.add(event);
            }
    }
    public void run() {
        while (!eventList.isEmpty()) {
            if (Clock.getInstance().getCurrentTime() < simulaationKesto) {
                System.out.println(eventList.size());
                if (vipAsiakasmäärä < vipKävijämäärä) {
                    eventList.add(entry.moveQueue(true));
                    vipAsiakasmäärä++;
                }
                if (gaAsiakasmäärä < gaKävijämäärä) {
                    eventList.add(entry.moveQueue(false));
                    gaAsiakasmäärä++;
                }
                Event event = eventList.remove();
                if (event == null) {
                    break;
                }
                handleEvent(event);
                Clock.getInstance().tick(10);
                System.out.println("Restarting loop...");
            }
        }
    }
    public static void main(String[] args) {
        Controller controller = new Controller(20000, 10, 100, 10);
        eventList.add(controller.entry.moveQueue(random.nextBoolean()));
        controller.run();
        System.out.println("Simulaatio on päättynyt.");
    }
}
