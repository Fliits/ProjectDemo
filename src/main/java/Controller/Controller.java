package Controller;

import Model.*;

import java.util.LinkedList;

import java.util.Random;

public class Controller extends Thread{
    Random random = new Random();
    LinkedList<Customer> queue;
    /*LinkedList<Customer> vipSecurityQueue;
    LinkedList<Customer> gaSecurityQueue;
    LinkedList<Customer> vipCloakroomQueue;
    LinkedList<Customer> gaCloakroomQueue;
    LinkedList<Customer> merchQueue;
     */
    Arrival entry;
    EventList eventList;
    private int työntekijäMäärä;
    private int gaAsiakasmäärä = 0;
    private int gaKävijämäärä;
    private int vipAsiakasmäärä = 0;
    private int vipKävijämäärä;
    private int simulaationKesto;
    private ServicePoint vipSecurity;
    private ServicePoint gaSecurity;
    private ServicePoint vipNarikka;
    private ServicePoint gaNarikka;
    private ServicePoint merch;
    private int previousTime;

    public Controller(int simulaationKesto, int työntekijäMäärä, int gaKävijämäärä, int vipKävijämäärä, int vipSecurityTyöntekijäMäärä, int gaSecurityTyöntekijäMäärä, int vipNarikkaTyöntekijäMäärä, int gaNarikkaTyöntekijäMäärä, int merchTyöntekijäMäärä) {
        queue = new LinkedList<>();
        /*vipSecurityQueue = new LinkedList<>();
        gaSecurityQueue = new LinkedList<>();
        vipCloakroomQueue = new LinkedList<>();
        gaCloakroomQueue = new LinkedList<>();
        merchQueue = new LinkedList<>();
         */
        eventList = new EventList();
        entry = new Arrival();
        this.simulaationKesto = simulaationKesto;
        this.työntekijäMäärä = työntekijäMäärä;
        this.gaKävijämäärä = gaKävijämäärä;
        this.vipKävijämäärä = vipKävijämäärä;
        this.vipSecurity = new ServicePoint(vipSecurityTyöntekijäMäärä);
        this.gaSecurity = new ServicePoint(gaSecurityTyöntekijäMäärä);
        this.vipNarikka = new ServicePoint(vipNarikkaTyöntekijäMäärä);
        this.gaNarikka = new ServicePoint(gaNarikkaTyöntekijäMäärä);
        this.merch = new ServicePoint(merchTyöntekijäMäärä);
    }

    public void moveQueue(){
        if (vipAsiakasmäärä < vipKävijämäärä) {
            queue.add(entry.insertCustomerToQueue(true));
            eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_VIP_SECURITY));
            vipAsiakasmäärä++;
        }

        if (gaAsiakasmäärä < gaKävijämäärä) {
            queue.add(entry.insertCustomerToQueue(false));
            eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_GA_SECURITY));
            gaAsiakasmäärä++;
        }
        System.out.println("Asiakas saapuu, jonossa " + queue.size() + " asiakasta.");
    }

    public void handleEvent(Event event){
        EventType type = event.getType();
        int time = (int) event.getTime();
        try {
            if (Clock.getInstance().getCurrentTime() == time) {
                for (Customer i : queue) {
                    if (type == EventType.START_VIP_SECURITY) {
                        if (vipSecurity.isAvailable()) {
                            Clock.getInstance().tick(100 + random.nextInt(3) * 100);
                            eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_VIP_SECURITY));
                            System.out.println("VIP Security tarkastaa asiakkaan, tarkastuksessa kuluu " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                            if (i.isKäyNarikassa()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_VIP_CLOAKROOM));
                            } else if (i.isOstaako()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH));
                            } else if (!i.isOstaako() && !i.isKäyNarikassa()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL));
                            }
                        }
                    } else if (type == EventType.START_VIP_CLOAKROOM) {
                        if (vipNarikka.isAvailable()) {
                            Clock.getInstance().tick(100 + random.nextInt(3) * 100);
                            eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_VIP_CLOAKROOM));
                            System.out.println("VIP Narikka käsittelee asiakkaan, siinä kesti " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                            if (i.isOstaako()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH));
                            } else if (!i.isOstaako() && !i.isKäyNarikassa()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL));
                            }
                        }
                    } else if (type == EventType.START_GA_SECURITY) {
                        if (gaSecurity.isAvailable()) {
                            Clock.getInstance().tick(100 + random.nextInt(3) * 100);
                            eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_GA_SECURITY));
                            System.out.println("GA Security tarkastaa asiakkaan, tarkastuksessa kuluu " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                            if (i.isKäyNarikassa()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_GA_CLOAKROOM));
                            } else if (i.isOstaako()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH));
                            } else if (!i.isOstaako() && !i.isKäyNarikassa()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL));
                            }
                        }
                    } else if (type == EventType.START_GA_CLOAKROOM) {
                        if (gaNarikka.isAvailable()) {
                            Clock.getInstance().tick(100 + random.nextInt(3) * 100);
                            eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_GA_CLOAKROOM));
                            System.out.println("GA Narikka käsittelee asiakkaan, siinä kesti " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                            if (i.isOstaako()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH));
                            } else if (!i.isOstaako() && !i.isKäyNarikassa()) {
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL));
                            }
                        }
                    } else if (type == EventType.START_MERCH) {
                        if (merch.isAvailable()) {
                            Clock.getInstance().tick(100 + random.nextInt(3) * 100);
                            eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_MERCH));
                            eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL));
                            queue.remove(i);
                            System.out.println("Oheistuotemyyntipiste palvelee asiakkaan, mihin kului " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                        }

                    }
                }
            } else {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        previousTime = time;
    }

    public void run() {
        while (!eventList.isEmpty()) {
                Event event = eventList.remove();
                if (event == null) {
                    break;
                }
                handleEvent(event);

            Clock.getInstance().tick(1);
        }
    }
    public static void main(String[] args) {
        Controller controller = new Controller(20000, 10, 100, 10, 2, 3, 1, 2, 2);
        for (int i = 0; i < (controller.vipKävijämäärä+ controller.gaKävijämäärä); i++) {
            if (Clock.getInstance().getCurrentTime() < controller.simulaationKesto) {
                controller.moveQueue();
                controller.run();
            } else {
                System.out.println("Simulaatio on päättynyt.");
                break;
            }
        }
    }
}
