package Model;

public class Clock {
    private static Clock instance;
    private int currentTime;

    private Clock() {
        currentTime = 0;
    }

    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void tick(int time) {
        for (int i = 0; i < time; i++) {
            currentTime++;
        }
    }
}
