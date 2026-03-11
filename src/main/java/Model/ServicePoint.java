package Model;

public class ServicePoint extends Thread{
    private int työntekijäMäärä;
    private boolean isAvailable = true;
    //private String ppID;

    public ServicePoint (int työntekijäMäärä) {
        this.työntekijäMäärä = työntekijäMäärä;
        isAvailable = true;
        //this.ppID = ppID;
    }

    public void setTyöntekijäMäärä(int työntekijäMäärä) {
        this.työntekijäMäärä = työntekijäMäärä;
    }

    public int getTyöntekijäMäärä() {
        return työntekijäMäärä;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void handleCustomer(){}

    /*public void setPpID(String ppID) {
        this.ppID = ppID;
    }

    public String getPpID() {
        return ppID;
    }
     */
}
