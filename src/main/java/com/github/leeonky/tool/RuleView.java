package com.github.leeonky.tool;

import io.cucumber.messages.Messages.GherkinDocument.Feature.FeatureChild.Rule;

import java.util.stream.Stream;

public class RuleView extends View {
    private final Rule rule;
    private final FeatureView featureView;

    public RuleView(Rule rule, FeatureView featureView, TagGroups tagGroups) {
        super(tagGroups);
        this.rule = rule;
        this.featureView = featureView;
    }

    @Override
    protected Stream<View> getChildren() {
        return rule.getChildrenList().stream().map(featureChild -> {
            switch (featureChild.getValueCase()) {
                case SCENARIO:
                    return new ScenarioView(featureChild.getScenario(), featureView, tagGroups);
                case BACKGROUND:
                    return new BackgroundView(featureChild.getBackground(), tagGroups);
                case VALUE_NOT_SET:
                default:
                    return null;
            }
        });
    }

    @Override
    protected String getDescription() {
        return rule.getDescription();
    }

    @Override
    protected String getName() {
        return rule.getName();
    }

    @Override
    protected String getKeyword() {
        return rule.getKeyword();
    }

    @Override
    public boolean matches() {
        return filterChildren().anyMatch(ScenarioView.class::isInstance);
    }
}
