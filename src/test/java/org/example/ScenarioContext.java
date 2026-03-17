package org.example;

package framework.context;

import java.util.ArrayList;
import java.util.List;
import framework.reporting.StepResult;

public class ScenarioContext {

    private String scenarioName;
    private long startTime;
    private long endTime;

    private List<StepResult> steps = new ArrayList<>();

    public void addStep(StepResult step) {
        steps.add(step);
    }

    public List<StepResult> getSteps() {
        return steps;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}