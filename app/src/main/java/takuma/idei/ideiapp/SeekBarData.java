package takuma.idei.ideiapp;

import java.io.Serializable;

public class SeekBarData implements Serializable {

    private int totalTime;
    private String elapsedTimeLabel;
    private String remainingTimeLabel;

    public SeekBarData(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getElapsedTimeLabel() {
        return elapsedTimeLabel;
    }

    public void setElapsedTimeLabel(String elapsedTimeLabel) {
        this.elapsedTimeLabel = elapsedTimeLabel;
    }

    public String getRemainingTimeLabel() {
        return remainingTimeLabel;
    }

    public void setRemainingTimeLabel(String remainingTimeLabel) {
        this.remainingTimeLabel = remainingTimeLabel;
    }
}
