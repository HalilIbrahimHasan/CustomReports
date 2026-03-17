package org.example;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotUtil {

    public static String capture(WebDriver driver, String fileName) {
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            Path dir = Paths.get("test-output", "screenshots");
            Files.createDirectories(dir);

            Path filePath = dir.resolve(fileName + ".png");
            Files.write(filePath, bytes);

            return filePath.toString().replace("\\", "/");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}