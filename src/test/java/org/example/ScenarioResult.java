package org.example;

public class ScenarioResult {
    private String scenarioName;
    private String featureName;
    private String status;
    private long startTime;
    private long endTime;
    private long durationMs;

    private List<StepResult> steps = new ArrayList<>();

}
