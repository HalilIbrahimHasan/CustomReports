package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CustomHtmlReportGenerator {

    public static void generate(List<ScenarioResult> scenarios, String outputPath) throws Exception {
        StringBuilder html = new StringBuilder();

        html.append("""
        <html>
        <head>
            <title>Automation Timeline Report</title>
            <style>
                body { font-family: Arial, sans-serif; background:#f5f7fb; margin:20px; }
                .scenario { background:white; border-radius:12px; margin-bottom:16px; box-shadow:0 2px 8px rgba(0,0,0,0.08); }
                .scenario-header { padding:16px; cursor:pointer; display:flex; justify-content:space-between; align-items:center; }
                .scenario-content { display:none; padding:16px; border-top:1px solid #eee; }
                .passed { color:white; background:#28a745; padding:4px 10px; border-radius:20px; }
                .failed { color:white; background:#dc3545; padding:4px 10px; border-radius:20px; }
                .skipped { color:white; background:#ffc107; padding:4px 10px; border-radius:20px; }
                .step { border-left:4px solid #4e73df; margin:14px 0; padding:10px 14px; background:#fafafa; border-radius:8px; }
                .step img { max-width:500px; display:block; margin-top:10px; border-radius:8px; border:1px solid #ddd; }
                .meta { color:#555; font-size:13px; margin-top:6px; }
            </style>
            <script>
                function toggle(id){
                    var el=document.getElementById(id);
                    el.style.display = el.style.display === 'block' ? 'none' : 'block';
                }
            </script>
        </head>
        <body>
        <h1>Execution Timeline Report</h1>
        """);

        int i = 0;
        for (ScenarioResult sc : scenarios) {
            String badgeClass = sc.getStatus() != null ? sc.getStatus().toLowerCase() : "skipped";

            html.append("<div class='scenario'>");
            html.append("<div class='scenario-header' onclick=\"toggle('sc").append(i).append("')\">");
            html.append("<div><b>").append(sc.getScenarioName()).append("</b><div class='meta'>Duration: ")
                    .append(sc.getDurationMs()).append(" ms</div></div>");
            html.append("<span class='").append(badgeClass).append("'>").append(sc.getStatus()).append("</span>");
            html.append("</div>");

            html.append("<div class='scenario-content' id='sc").append(i).append("'>");
            for (StepResult step : sc.getSteps()) {
                String stepBadge = step.getStatus().equalsIgnoreCase("PASSED") ? "passed"
                        : step.getStatus().equalsIgnoreCase("FAILED") ? "failed"
                        : "skipped";

                html.append("<div class='step'>");
                html.append("<div><b>").append(step.getStepName()).append("</b> ")
                        .append("<span class='").append(stepBadge).append("'>").append(step.getStatus()).append("</span></div>");
                html.append("<div class='meta'>Step Duration: ").append(step.getDurationMs()).append(" ms</div>");
                html.append("<div>").append(step.getDescription()).append("</div>");

                if (step.getScreenshotPath() != null && !step.getScreenshotPath().isEmpty()) {
                    html.append("<img src='").append(step.getScreenshotPath()).append("' alt='screenshot'/>");
                }

                html.append("</div>");
            }
            html.append("</div></div>");
            i++;
        }

        html.append("</body></html>");

        Files.createDirectories(Paths.get("test-output"));
        Files.writeString(Paths.get(outputPath), html.toString());
    }
}