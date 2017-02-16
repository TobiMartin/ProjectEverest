package com.linxu.mounteverest;

/**
 * Created by Vanessa on 16.02.2017.
 */

import android.graphics.Rect;

public class EventMarker {

    private Rect rect;
    private LearningStep learningStep;
    private int day;

    public EventMarker(Rect rect, LearningStep learningStep) {
        this.rect = rect;
        this.learningStep = learningStep;
    }

    public LearningStep getLearningStep() {
        return learningStep;
    }

    public void setLearningStep(LearningStep learningStep) {
        this.learningStep = learningStep;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }
}
