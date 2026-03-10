package Controller;

import Model.*;

import java.util.LinkedList;

import java.util.Random;

public class Controller extends Thread{
    Random random = new Random();

    LinkedList<Customer> queue;
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

    public void run(){
        if (Clock.getInstance().getCurrentTime() == previousTime) {
            Event event = eventList.remove();
            String type = String.valueOf(event.getType());
            int time = (int) event.getTime();
            try {
                if (Clock.getInstance().getCurrentTime() == time) {
                    for (Customer i : queue) {
                        if (type == "START_VIP_SECURITY") {
                            if (vipSecurity.isAvailable()) {
                                Clock.getInstance().tick(1 + random.nextInt(3));
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
                        } else if (type == "START_VIP_NARIKKA") {
                            if (vipNarikka.isAvailable()) {
                                Clock.getInstance().tick(1 + random.nextInt(3));
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_VIP_CLOAKROOM));
                                System.out.println("VIP Narikka käsittelee asiakkaan, siinä kesti " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                                if (queue.getFirst().isOstaako()) {
                                    eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH));
                                } else if (!i.isOstaako() && !i.isKäyNarikassa()) {
                                    eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL));
                                }
                            }
                        } else if (type == "START_GA_SECURITY") {
                            if (gaSecurity.isAvailable()) {
                                Clock.getInstance().tick(1 + random.nextInt(3));
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_GA_SECURITY));
                                System.out.println("GA Security tarkastaa asiakkaan, tarkastuksessa kuluu " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                                if (queue.getFirst().isKäyNarikassa()) {
                                    eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_GA_CLOAKROOM));
                                } else if (queue.getFirst().isOstaako()) {
                                    eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH));
                                } else if (!i.isOstaako() && !i.isKäyNarikassa()) {
                                    eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL));
                                }
                            }
                        } else if (type == "START_GA_NARIKKA") {
                            if (gaNarikka.isAvailable()) {
                                Clock.getInstance().tick(1 + random.nextInt(3));
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_GA_CLOAKROOM));
                                System.out.println("GA Narikka käsittelee asiakkaan, siinä kesti " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                                if (queue.getFirst().isOstaako()) {
                                    eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_MERCH));
                                } else if (!i.isOstaako() && !i.isKäyNarikassa()) {
                                    eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL));
                                }
                            }
                        } else if (type == "START_MERCH") {
                            if (merch.isAvailable()) {
                                Clock.getInstance().tick(1 + random.nextInt(3));
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_MERCH));
                                eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL));
                                System.out.println("Oheistuotemyyntipiste palvelee asiakkaan, mihin kului " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                            }

                        }
                    }
                } else {
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            previousTime = time;
        }
        Clock.getInstance().tick(1);
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
