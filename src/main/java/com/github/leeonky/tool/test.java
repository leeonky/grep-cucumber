package com.github.leeonky.tool;

import io.cucumber.gherkin.Gherkin;
import io.cucumber.messages.Messages;
import lombok.SneakyThrows;

import java.util.Collections;

public class test {
    @SneakyThrows
    public static void main(String[] args) {

        Messages.GherkinDocument gherkinDoc = Gherkin.fromPaths(Collections.singletonList("/opt/share/beifang-huachuang/e2e-tests/pvd-scope-300-std/src/test/resources/ui/alarm-ch1.feature"),
                false, true, false, () -> "").findFirst().get().getGherkinDocument();

        Messages.GherkinDocument.Feature feature = gherkinDoc.getFeature().toBuilder()
//                .removeChildren(0)
                .build();

        Messages.GherkinDocument.Feature.Step step = feature.getChildren(0)
                .getRule().getChildren(0)
                .getScenario().getSteps(0)
                .toBuilder().setId("").clearLocation().build();
//        System.out.println("step.getKeyword() = " + step.getKeyword());
//        System.out.println("step.getText() = " + step.getText());
//        step.
//        System.out.println(new String(step.toByteArray()));
    }
}
