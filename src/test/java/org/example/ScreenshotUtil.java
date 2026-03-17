package org.example;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotUtil {

    public static String capture(WebDriver driver, String fileName) {
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path dir = Paths.get("test-output", "screenshots");
            Files.createDirectories(dir);

            Path path = dir.resolve(fileName + ".png");
            Files.write(path, bytes);

            return path.toString().replace("\\", "/");
        } catch (IOException e) {
            throw new RuntimeException("Unable to capture screenshot", e);
        }
    }
}
