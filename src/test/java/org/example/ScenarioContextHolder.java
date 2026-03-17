package org.example;

package framework.context;

public class ScenarioContextHolder {

    // Keeps one ScenarioContext per running thread (important for parallel tests)
    private static final ThreadLocal<ScenarioContext> CONTEXT =
            ThreadLocal.withInitial(ScenarioContext::new);

    // Get current scenario context
    public static ScenarioContext get() {
        return CONTEXT.get();
    }

    // Clear after scenario finishes
    public static void clear() {
        CONTEXT.remove();
    }
}
