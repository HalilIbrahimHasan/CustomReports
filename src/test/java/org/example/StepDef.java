package org.example;

public class StepDef {

    @When("user enters username")
    public void user_enters_username() {
        StepTracker.recordStep(
                "Enter Username",
                "User enters valid username into login field",
                () -> loginPage.enterUsername("demoUser"),
                DriverManager.getDriver(),
                ScenarioContextHolder.get()
        );
    }
}
