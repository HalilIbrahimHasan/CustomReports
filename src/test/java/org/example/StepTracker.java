package org.example;

import org.openqa.selenium.WebDriver;

public class StepTracker {

    public static void recordStep(
            String stepName,
            String description,
            Runnable action,
            WebDriver driver,
            ScenarioContext scenarioContext) {

        long start = System.currentTimeMillis();

        StepResult step = new StepResult();
        step.setStepName(stepName);
        step.setDescription(description);
        step.setStartTime(start);

        try {
            action.run();
            step.setStatus("PASSED");
        } catch (Exception e) {
            step.setStatus("FAILED");
            step.setDescription(description + " | Error: " + e.getMessage());
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            step.setEndTime(end);
            step.setDurationMs(end - start);

            String screenshotPath = ScreenshotUtil.capture(
                    driver,
                    stepName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + end
            );
            step.setScreenshotPath(screenshotPath);

            scenarioContext.getScenarioResult().getSteps().add(step);
        }
    }
}