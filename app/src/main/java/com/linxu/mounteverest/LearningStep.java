package com.linxu.mounteverest;

/**
 * Created by lin xu on 19.01.2017.
 */

public class LearningStep {
    private String date;
    private String title;
    private String note;

    public LearningStep(String date, String title, String note){
        this.date = date;
        this.title = title;
        this.note = note;
    }

    public LearningStep() {
    }

    @Override
    public String toString() {
        return "LearningStep{"  +
                "date = "       +
                date            +
                "titel= "       +
                title           +
                "note = "       +
                note            +
                "}";
    }

    public String getDate(){
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
