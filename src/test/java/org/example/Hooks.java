package org.example;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.ArrayList;
import java.util.List;

public class Hooks {

    private static final List<ScenarioResult> ALL_SCENARIOS = new ArrayList<>();

    @Before
    public void beforeScenario(Scenario scenario) {
        ScenarioContext context = ScenarioContextHolder.get();
        ScenarioResult scenarioResult = context.getScenarioResult();

        scenarioResult.setScenarioName(scenario.getName());
        scenarioResult.setStartTime(System.currentTimeMillis());
        scenarioResult.setSteps(new ArrayList<>());
    }

    @After
    public void afterScenario(Scenario scenario) {
        ScenarioContext context = ScenarioContextHolder.get();
        ScenarioResult scenarioResult = context.getScenarioResult();

        scenarioResult.setEndTime(System.currentTimeMillis());
        scenarioResult.setDurationMs(
                scenarioResult.getEndTime() - scenarioResult.getStartTime()
        );
        scenarioResult.setStatus(scenario.isFailed() ? "FAILED" : "PASSED");

        ALL_SCENARIOS.add(scenarioResult);
        ScenarioContextHolder.clear();
    }

    @AfterAll
    public static void generateReport() throws Exception {
        CustomHtmlReportGenerator.generate(
                ALL_SCENARIOS,
                "test-output/custom-report.html"
        );
    }
}