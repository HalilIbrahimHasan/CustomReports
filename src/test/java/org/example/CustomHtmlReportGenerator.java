package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CustomHtmlReportGenerator {

    public static void generate(List<ScenarioResult> scenarios, String outputPath) throws Exception {
        StringBuilder html = new StringBuilder();

        int totalScenarios = scenarios.size();
        int passedScenarios = 0;
        int failedScenarios = 0;
        int skippedScenarios = 0;
        long totalDuration = 0;

        for (ScenarioResult sc : scenarios) {
            totalDuration += sc.getDurationMs();

            if ("PASSED".equalsIgnoreCase(sc.getStatus())) {
                passedScenarios++;
            } else if ("FAILED".equalsIgnoreCase(sc.getStatus())) {
                failedScenarios++;
            } else {
                skippedScenarios++;
            }
        }

        html.append("""
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Automation Execution Report</title>
            <style>
                * {
                    box-sizing: border-box;
                    margin: 0;
                    padding: 0;
                }

                body {
                    font-family: 'Segoe UI', Tahoma, Arial, sans-serif;
                    background: linear-gradient(180deg, #f4f7fb 0%, #eef2f9 100%);
                    color: #1f2937;
                    padding: 24px;
                }

                .container {
                    max-width: 1450px;
                    margin: 0 auto;
                }

                .top-banner {
                    background: linear-gradient(135deg, #1e3c72, #2a5298, #4e73df);
                    color: white;
                    border-radius: 20px;
                    padding: 28px 32px;
                    box-shadow: 0 12px 32px rgba(31, 60, 114, 0.25);
                    margin-bottom: 24px;
                }

                .top-banner h1 {
                    font-size: 32px;
                    margin-bottom: 8px;
                    letter-spacing: 0.3px;
                }

                .top-banner p {
                    opacity: 0.92;
                    font-size: 15px;
                }

                .summary-sticky {
                    position: sticky;
                    top: 16px;
                    z-index: 999;
                    margin-bottom: 24px;
                }

                .summary-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
                    gap: 16px;
                }

                .summary-card {
                    background: white;
                    border-radius: 18px;
                    padding: 20px;
                    box-shadow: 0 8px 22px rgba(0,0,0,0.08);
                    border: 1px solid rgba(0,0,0,0.04);
                    transition: transform 0.2s ease, box-shadow 0.2s ease;
                }

                .summary-card:hover {
                    transform: translateY(-3px);
                    box-shadow: 0 12px 28px rgba(0,0,0,0.12);
                }

                .summary-label {
                    font-size: 13px;
                    color: #6b7280;
                    margin-bottom: 8px;
                    text-transform: uppercase;
                    letter-spacing: 0.8px;
                }

                .summary-value {
                    font-size: 30px;
                    font-weight: 700;
                }

                .summary-total { border-left: 6px solid #4e73df; }
                .summary-pass  { border-left: 6px solid #16a34a; }
                .summary-fail  { border-left: 6px solid #dc2626; }
                .summary-skip  { border-left: 6px solid #f59e0b; }
                .summary-time  { border-left: 6px solid #7c3aed; }

                .section-title {
                    margin: 26px 0 14px 4px;
                    font-size: 22px;
                    color: #1f2937;
                    font-weight: 700;
                }

                .scenario {
                    background: white;
                    border-radius: 18px;
                    margin-bottom: 18px;
                    box-shadow: 0 8px 24px rgba(0,0,0,0.08);
                    overflow: hidden;
                    border: 1px solid rgba(0,0,0,0.04);
                }

                .scenario-header {
                    padding: 18px 22px;
                    cursor: pointer;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    gap: 16px;
                    background: linear-gradient(90deg, #ffffff 0%, #f8fbff 100%);
                    transition: background 0.2s ease;
                }

                .scenario-header:hover {
                    background: linear-gradient(90deg, #f8fbff 0%, #eef5ff 100%);
                }

                .scenario-left {
                    display: flex;
                    align-items: center;
                    gap: 16px;
                    min-width: 0;
                }

                .scenario-index {
                    width: 42px;
                    height: 42px;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-weight: 700;
                    color: white;
                    background: linear-gradient(135deg, #4e73df, #1e40af);
                    flex-shrink: 0;
                    box-shadow: 0 6px 16px rgba(78,115,223,0.35);
                }

                .scenario-title-block {
                    min-width: 0;
                }

                .scenario-title {
                    font-size: 18px;
                    font-weight: 700;
                    color: #111827;
                    margin-bottom: 5px;
                    word-break: break-word;
                }

                .scenario-meta {
                    font-size: 13px;
                    color: #6b7280;
                    display: flex;
                    flex-wrap: wrap;
                    gap: 14px;
                }

                .scenario-right {
                    display: flex;
                    align-items: center;
                    gap: 12px;
                    flex-wrap: wrap;
                    justify-content: flex-end;
                }

                .badge {
                    color: white;
                    padding: 7px 14px;
                    border-radius: 999px;
                    font-size: 12px;
                    font-weight: 700;
                    letter-spacing: 0.4px;
                    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
                }

                .passed {
                    background: linear-gradient(135deg, #16a34a, #22c55e);
                }

                .failed {
                    background: linear-gradient(135deg, #dc2626, #ef4444);
                }

                .skipped {
                    background: linear-gradient(135deg, #f59e0b, #fbbf24);
                    color: #1f2937;
                }

                .toggle-icon {
                    font-size: 20px;
                    color: #4b5563;
                    font-weight: bold;
                    width: 24px;
                    text-align: center;
                }

                .scenario-content {
                    display: none;
                    padding: 22px;
                    border-top: 1px solid #e5e7eb;
                    background: #fcfdff;
                }

                .timeline {
                    position: relative;
                    margin-left: 14px;
                    padding-left: 22px;
                }

                .timeline::before {
                    content: "";
                    position: absolute;
                    left: 8px;
                    top: 0;
                    bottom: 0;
                    width: 3px;
                    background: linear-gradient(180deg, #4e73df, #c7d2fe);
                    border-radius: 4px;
                }

                .step {
                    position: relative;
                    margin-bottom: 22px;
                    background: white;
                    border-radius: 16px;
                    padding: 18px;
                    box-shadow: 0 6px 18px rgba(0,0,0,0.07);
                    border: 1px solid rgba(0,0,0,0.04);
                }

                .step::before {
                    content: "";
                    position: absolute;
                    left: -26px;
                    top: 24px;
                    width: 16px;
                    height: 16px;
                    border-radius: 50%;
                    border: 4px solid white;
                    box-shadow: 0 0 0 2px #dbeafe;
                }

                .step.passed-step::before {
                    background: #22c55e;
                }

                .step.failed-step::before {
                    background: #ef4444;
                }

                .step.skipped-step::before {
                    background: #f59e0b;
                }

                .step-top {
                    display: flex;
                    justify-content: space-between;
                    align-items: flex-start;
                    gap: 12px;
                    margin-bottom: 10px;
                    flex-wrap: wrap;
                }

                .step-title {
                    font-size: 16px;
                    font-weight: 700;
                    color: #111827;
                }

                .step-duration {
                    background: #eef4ff;
                    color: #1d4ed8;
                    padding: 6px 10px;
                    border-radius: 10px;
                    font-size: 12px;
                    font-weight: 700;
                    white-space: nowrap;
                }

                .step-description {
                    color: #374151;
                    font-size: 14px;
                    line-height: 1.6;
                    margin: 10px 0 14px 0;
                    background: #f8fafc;
                    border-left: 4px solid #cbd5e1;
                    padding: 12px 14px;
                    border-radius: 10px;
                }

                .step-screenshot-wrapper {
                    margin-top: 12px;
                }

                .step-screenshot-title {
                    font-size: 12px;
                    font-weight: 700;
                    color: #6b7280;
                    margin-bottom: 10px;
                    letter-spacing: 0.5px;
                    text-transform: uppercase;
                }

                .step img {
                    max-width: 100%;
                    width: 760px;
                    border-radius: 14px;
                    border: 1px solid #d1d5db;
                    box-shadow: 0 8px 20px rgba(0,0,0,0.12);
                    transition: transform 0.25s ease, box-shadow 0.25s ease;
                    cursor: zoom-in;
                }

                .step img:hover {
                    transform: scale(1.015);
                    box-shadow: 0 14px 30px rgba(0,0,0,0.18);
                }

                .empty-shot {
                    display: inline-block;
                    background: #f3f4f6;
                    color: #6b7280;
                    padding: 10px 14px;
                    border-radius: 10px;
                    font-size: 13px;
                    border: 1px dashed #cbd5e1;
                }

                .footer {
                    text-align: center;
                    color: #6b7280;
                    font-size: 13px;
                    margin-top: 28px;
                    padding: 20px;
                }

                .status-pill {
                    display: inline-block;
                    font-size: 11px;
                    font-weight: 700;
                    padding: 5px 10px;
                    border-radius: 999px;
                    margin-bottom: 8px;
                    letter-spacing: 0.4px;
                }

                .status-pill.passed-pill {
                    background: #dcfce7;
                    color: #166534;
                }

                .status-pill.failed-pill {
                    background: #fee2e2;
                    color: #991b1b;
                }

                .status-pill.skipped-pill {
                    background: #fef3c7;
                    color: #92400e;
                }

                @media (max-width: 900px) {
                    .scenario-header {
                        flex-direction: column;
                        align-items: stretch;
                    }

                    .scenario-right {
                        justify-content: flex-start;
                    }

                    .summary-grid {
                        grid-template-columns: 1fr 1fr;
                    }
                }

                @media (max-width: 640px) {
                    body {
                        padding: 14px;
                    }

                    .summary-grid {
                        grid-template-columns: 1fr;
                    }

                    .top-banner h1 {
                        font-size: 25px;
                    }

                    .scenario-left {
                        align-items: flex-start;
                    }

                    .scenario-index {
                        width: 36px;
                        height: 36px;
                        font-size: 14px;
                    }
                }
            </style>
            <script>
                function toggle(id, iconId) {
                    const el = document.getElementById(id);
                    const icon = document.getElementById(iconId);

                    if (el.style.display === 'block') {
                        el.style.display = 'none';
                        if (icon) icon.innerHTML = '+';
                    } else {
                        el.style.display = 'block';
                        if (icon) icon.innerHTML = '−';
                    }
                }

                function expandAll() {
                    const contents = document.querySelectorAll('.scenario-content');
                    const icons = document.querySelectorAll('.toggle-icon');
                    contents.forEach(c => c.style.display = 'block');
                    icons.forEach(i => i.innerHTML = '−');
                }

                function collapseAll() {
                    const contents = document.querySelectorAll('.scenario-content');
                    const icons = document.querySelectorAll('.toggle-icon');
                    contents.forEach(c => c.style.display = 'none');
                    icons.forEach(i => i.innerHTML = '+');
                }
            </script>
        </head>
        <body>
            <div class="container">
                <div class="top-banner">
                    <h1>Automation Execution Report</h1>
                    <p>Interactive timeline report with scenario status, duration, descriptions, and screenshots.</p>
                </div>

                <div class="summary-sticky">
                    <div class="summary-grid">
        """);

        html.append("<div class='summary-card summary-total'><div class='summary-label'>Total Scenarios</div><div class='summary-value'>")
                .append(totalScenarios).append("</div></div>");

        html.append("<div class='summary-card summary-pass'><div class='summary-label'>Passed</div><div class='summary-value'>")
                .append(passedScenarios).append("</div></div>");

        html.append("<div class='summary-card summary-fail'><div class='summary-label'>Failed</div><div class='summary-value'>")
                .append(failedScenarios).append("</div></div>");

        html.append("<div class='summary-card summary-skip'><div class='summary-label'>Skipped</div><div class='summary-value'>")
                .append(skippedScenarios).append("</div></div>");

        html.append("<div class='summary-card summary-time'><div class='summary-label'>Total Duration</div><div class='summary-value'>")
                .append(totalDuration).append(" ms</div></div>");

        html.append("""
                    </div>
                </div>

                <div style="display:flex; justify-content:flex-end; gap:10px; margin-bottom:16px;">
                    <button onclick="expandAll()" style="border:none; background:#1d4ed8; color:white; padding:10px 16px; border-radius:10px; font-weight:700; cursor:pointer; box-shadow:0 6px 16px rgba(29,78,216,0.25);">Expand All</button>
                    <button onclick="collapseAll()" style="border:none; background:#374151; color:white; padding:10px 16px; border-radius:10px; font-weight:700; cursor:pointer; box-shadow:0 6px 16px rgba(55,65,81,0.25);">Collapse All</button>
                </div>

                <div class="section-title">Scenario Details</div>
        """);

        int i = 0;
        for (ScenarioResult sc : scenarios) {
            String scenarioStatus = sc.getStatus() == null ? "SKIPPED" : sc.getStatus().toUpperCase();
            String badgeClass = scenarioStatus.equals("PASSED") ? "passed"
                    : scenarioStatus.equals("FAILED") ? "failed"
                    : "skipped";

            int stepCount = sc.getSteps() != null ? sc.getSteps().size() : 0;

            html.append("<div class='scenario'>");

            html.append("<div class='scenario-header' onclick=\"toggle('sc")
                    .append(i)
                    .append("','icon")
                    .append(i)
                    .append("')\">");

            html.append("<div class='scenario-left'>");
            html.append("<div class='scenario-index'>").append(i + 1).append("</div>");
            html.append("<div class='scenario-title-block'>");
            html.append("<div class='scenario-title'>").append(escapeHtml(sc.getScenarioName())).append("</div>");
            html.append("<div class='scenario-meta'>");
            html.append("<span><b>Duration:</b> ").append(sc.getDurationMs()).append(" ms</span>");
            html.append("<span><b>Steps:</b> ").append(stepCount).append("</span>");
            html.append("</div>");
            html.append("</div>");
            html.append("</div>");

            html.append("<div class='scenario-right'>");
            html.append("<span class='badge ").append(badgeClass).append("'>").append(scenarioStatus).append("</span>");
            html.append("<span id='icon").append(i).append("' class='toggle-icon'>+</span>");
            html.append("</div>");

            html.append("</div>");

            html.append("<div class='scenario-content' id='sc").append(i).append("'>");
            html.append("<div class='timeline'>");

            if (sc.getSteps() != null && !sc.getSteps().isEmpty()) {
                for (StepResult step : sc.getSteps()) {
                    String stepStatus = step.getStatus() == null ? "SKIPPED" : step.getStatus().toUpperCase();
                    String stepBadgeClass = stepStatus.equals("PASSED") ? "passed"
                            : stepStatus.equals("FAILED") ? "failed"
                            : "skipped";

                    String stepCardClass = stepStatus.equals("PASSED") ? "passed-step"
                            : stepStatus.equals("FAILED") ? "failed-step"
                            : "skipped-step";

                    String pillClass = stepStatus.equals("PASSED") ? "passed-pill"
                            : stepStatus.equals("FAILED") ? "failed-pill"
                            : "skipped-pill";

                    html.append("<div class='step ").append(stepCardClass).append("'>");

                    html.append("<div class='status-pill ").append(pillClass).append("'>")
                            .append(stepStatus).append("</div>");

                    html.append("<div class='step-top'>");
                    html.append("<div class='step-title'>").append(escapeHtml(step.getStepName())).append("</div>");
                    html.append("<div class='step-duration'>").append(step.getDurationMs()).append(" ms</div>");
                    html.append("</div>");

                    html.append("<div class='step-description'>")
                            .append(escapeHtml(step.getDescription() == null ? "" : step.getDescription()))
                            .append("</div>");

                    html.append("<div class='step-screenshot-wrapper'>");
                    html.append("<div class='step-screenshot-title'>Screenshot</div>");

                    if (step.getScreenshotPath() != null && !step.getScreenshotPath().isEmpty()) {
                        html.append("<img src='")
                                .append(step.getScreenshotPath().replace("\\", "/"))
                                .append("' alt='screenshot'/>");
                    } else {
                        html.append("<div class='empty-shot'>No screenshot captured</div>");
                    }

                    html.append("</div>");
                    html.append("</div>");
                }
            } else {
                html.append("<div class='step skipped-step'>");
                html.append("<div class='status-pill skipped-pill'>NO STEPS</div>");
                html.append("<div class='step-title'>No step data available for this scenario.</div>");
                html.append("</div>");
            }

            html.append("</div>");
            html.append("</div>");
            html.append("</div>");

            i++;
        }

        html.append("""
                <div class="footer">
                    Generated by Custom Cucumber Selenium Java Report
                </div>
            </div>
        </body>
        </html>
        """);

        Files.createDirectories(Paths.get("test-output"));
        Files.writeString(Paths.get(outputPath), html.toString());
    }

    private static String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}