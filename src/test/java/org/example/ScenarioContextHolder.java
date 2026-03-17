package org.example;

public class ScenarioContextHolder {

    private static final ThreadLocal<ScenarioContext> CONTEXT =
            ThreadLocal.withInitial(ScenarioContext::new);

    public static ScenarioContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}