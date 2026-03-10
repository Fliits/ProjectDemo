package Model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private boolean lippu; //VIP tai GA
    private int tavaraMäärä; //1-4, enemmän tavaraa kestää pitempään
    private boolean ostaako;
    private boolean ostiko;
    private boolean käyNarikassa;
    private List<Integer> kestot;
    private double saapumisaika;

    public Customer(boolean lippu, int tavaraMäärä, boolean ostaako, boolean käyNarikassa) {
        this.lippu = lippu;
        this.tavaraMäärä = tavaraMäärä;
        this.ostaako = ostaako;
        this.käyNarikassa = käyNarikassa;

        kestot = new ArrayList<>();
    }

    public void setLippu(boolean lippu) {
        this.lippu = lippu;
    }

    public boolean isLippu() {
        return lippu;
    }

    public void setOstaako(boolean ostaako) {
        this.ostaako = ostaako;
    }

    public boolean isOstaako() {
        return ostaako;
    }

    public void setTavaraMäärä(int tavaraMäärä) {
        this.tavaraMäärä = tavaraMäärä;
    }

    public int getTavaraMäärä() {
        return tavaraMäärä;
    }

    public void setOstiko(boolean ostiko) {
        this.ostiko = ostiko;
    }

    public boolean isOstiko() {
        return ostiko;
    }

    public void addKesto(int kesto) {
        kestot.add(kesto);
    }
    public int getKesto(int index) {
        return kestot.get(index);
    }

    public boolean isKäyNarikassa() {
        return käyNarikassa;
    }

    public void setKäyNarikassa(boolean käyNarikassa) {
        this.käyNarikassa = käyNarikassa;
    }
}
