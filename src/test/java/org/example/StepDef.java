package org.example;

import io.cucumber.java.en.When;

public class LoginSteps {

    LoginPage loginPage = new LoginPage();

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

    @When("user enters password")
    public void user_enters_password() {
        StepTracker.recordStep(
                "Enter Password",
                "User enters valid password into login field",
                () -> loginPage.enterPassword("demoPass"),
                DriverManager.getDriver(),
                ScenarioContextHolder.get()
        );
    }
}