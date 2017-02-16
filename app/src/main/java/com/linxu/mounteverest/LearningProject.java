package com.linxu.mounteverest;

import java.util.List;

/**
 * Created by lin xu on 16.02.2017.
 */

public class LearningProject {

    private List<LearningStep> learningSteps;
    private String name;

    public LearningProject(List<LearningStep> learningSteps, String name) {
        this.learningSteps = learningSteps;
        this.name = name;
    }

    public void setLearningSteps(List<LearningStep> learningSteps) {
        this.learningSteps = learningSteps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LearningStep> getLearningSteps() {
        return learningSteps;
    }

    public String getName() {
        return name;
    }

}
