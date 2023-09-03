package com.github.leeonky.tool;

import io.cucumber.messages.Messages.GherkinDocument.Feature.FeatureChild.Rule;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class RuleView implements View {
    private final Rule rule;
    private final FeatureView featureView;
    private final TagGroups tagGroups;

    public RuleView(Rule rule, FeatureView featureView, TagGroups tagGroups) {
        this.rule = rule;
        this.featureView = featureView;
        this.tagGroups = tagGroups;
    }

    private Stream<View> getChildren() {
        return rule.getChildrenList().stream().map(featureChild -> {
            switch (featureChild.getValueCase()) {
                case SCENARIO:
                    return (View) new ScenarioView(featureChild.getScenario(), featureView);
                case BACKGROUND:
                case VALUE_NOT_SET:
                    return null;
            }
            return null;
        }).filter(view -> view.matches(tagGroups));
    }

    @Override
    public void output(List<String> lines, int intentLevel) {
        String intent = String.join("", Collections.nCopies(intentLevel, "  "));
        lines.add(intent + rule.getKeyword() + ": " + rule.getName());
        getChildren().forEach(ob -> {
            lines.add("");
            ob.output(lines, intentLevel + 1);
        });
    }

    @Override
    public boolean matches(TagGroups tagGroups) {
        return getChildren().findAny().isPresent();
    }
}
