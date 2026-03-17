package org.example;

import framework.context.ScenarioContext;
import framework.context.ScenarioContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        ScenarioContext context = ScenarioContextHolder.get();
        context.setScenarioName(scenario.getName());
        context.setStartTime(System.currentTimeMillis());
    }

    @After
    public void afterScenario() {
        ScenarioContext context = ScenarioContextHolder.get();
        context.setEndTime(System.currentTimeMillis());

        // later you will send this to ReportManager
        ScenarioContextHolder.clear();
    }
}