package Controller;

import Model.*;

import java.util.LinkedList;

import java.util.Random;

public class Controller extends Thread{
    static Random random = new Random();
    /*LinkedList<Customer> arrivalQueue;
    LinkedList<Customer> vipSecurityQueue;
    LinkedList<Customer> gaSecurityQueue;
    LinkedList<Customer> vipCloakroomQueue;
    LinkedList<Customer> gaCloakroomQueue;
    LinkedList<Customer> merchQueue;*/
    Arrival entry;
    static EventList eventList;
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
        /*arrivalQueue = new LinkedList<>();
        vipSecurityQueue = new LinkedList<>();
        gaSecurityQueue = new LinkedList<>();
        vipCloakroomQueue = new LinkedList<>();
        gaCloakroomQueue = new LinkedList<>();
        merchQueue = new LinkedList<>();*/
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

    public void moveQueue(boolean isVIP){
        Customer customer = new Customer(isVIP, 1+random.nextInt(3), random.nextBoolean(), random.nextBoolean());
        if (isVIP && vipAsiakasmäärä < vipKävijämäärä) {
             eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_VIP_SECURITY, customer));
             vipAsiakasmäärä++;
        } else if (!isVIP && gaAsiakasmäärä < gaKävijämäärä) {
             eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_GA_SECURITY, customer));
             gaAsiakasmäärä++;
        }
    }

    public void handleEvent(Event event) {
        EventType type = event.getType();
        Customer customer = event.getCustomer();
        int time = (int) event.getTime();
        try {
            if (Clock.getInstance().getCurrentTime() == time) {
                if (type == EventType.START_VIP_SECURITY) {
                    if (vipSecurity.isAvailable()) {
                        vipSecurity.setAvailable(false);
                        System.out.println("VIP Security tarkastaa asiakkaan, tarkastuksessa kuluu " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                        eventList.add(new Event((Clock.getInstance().getCurrentTime() + 100 + random.nextInt(3) * 100), EventType.FINISH_VIP_SECURITY, customer));
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
                        eventList.add(new Event((Clock.getInstance().getCurrentTime() + 100 + random.nextInt(3) * 100), EventType.FINISH_VIP_CLOAKROOM, customer));
                        System.out.println("VIP Narikka käsittelee asiakkaan, siinä kesti " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
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
                        eventList.add(new Event((Clock.getInstance().getCurrentTime() + 100 + random.nextInt(3) * 100), EventType.FINISH_GA_SECURITY, customer));
                        System.out.println("GA Security tarkastaa asiakkaan, tarkastuksessa kuluu " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
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
                        eventList.add(new Event((Clock.getInstance().getCurrentTime() + 100 + random.nextInt(3) * 100), EventType.FINISH_GA_CLOAKROOM, customer));
                        System.out.println("GA Narikka käsittelee asiakkaan, siinä kesti " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
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
                        eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.FINISH_MERCH, customer));

                        System.out.println("Oheistuotemyyntipiste palvelee asiakkaan, mihin kului " + (Clock.getInstance().getCurrentTime() - time) + " yksikköä aikaa.");
                    }
                } else if (type == EventType.FINISH_MERCH) {
                    merch.setAvailable(true);
                    eventList.add(new Event(Clock.getInstance().getCurrentTime(), EventType.START_ENTER_CONCERT_HALL, customer));
                } else {
                    Thread.sleep(100);
                }
            }
        }catch (InterruptedException e) {
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

            Clock.getInstance().tick(100);
        }
    }
    public static void main(String[] args) {
        Controller controller = new Controller(20000, 10, 100, 10, 2, 3, 1, 2, 2);
        controller.moveQueue(random.nextBoolean());
        while (!eventList.isEmpty()) {
            controller.run();
            for (int i = 0; i < (controller.vipKävijämäärä + controller.gaKävijämäärä); i++) {
                if (Clock.getInstance().getCurrentTime() < controller.simulaationKesto) {
                    controller.moveQueue(random.nextBoolean());
                    controller.run();
                } else {
                    System.out.println("Simulaatio on päättynyt.");
                    break;
                }
            }
        }
    }
}
