package com.github.leeonky.tool;

import io.cucumber.messages.Messages.GherkinDocument.Feature;

import java.util.List;
import java.util.stream.Stream;

public class FeatureView extends View {
    private final Feature feature;

    public FeatureView(Feature feature, TagGroups tagGroups) {
        super(tagGroups);
        this.feature = feature;
    }

    @Override
    public void output(List<String> lines, int intentLevel) {
        lines.add("# language: " + feature.getLanguage());
        super.output(lines, intentLevel);
    }

    @Override
    protected Stream<View> getChildren() {
        return feature.getChildrenList().stream().map(featureChild -> {
            switch (featureChild.getValueCase()) {
                case RULE:
                    return new RuleView(featureChild.getRule(), this, tagGroups);
                case SCENARIO:
                    return new ScenarioView(featureChild.getScenario(), this, tagGroups);
                case BACKGROUND:
                    return new BackgroundView(featureChild.getBackground(), tagGroups);
                case VALUE_NOT_SET:
                    return null;
            }
            return null;
        });
    }

    @Override
    protected String getDescription() {
        return feature.getDescription();
    }

    @Override
    protected String getName() {
        return feature.getName();
    }

    @Override
    protected String getKeyword() {
        return feature.getKeyword();
    }

    @Override
    public List<Feature.Tag> getOwnTags() {
        return feature.getTagsList();
    }

    @Override
    public boolean matches() {
        return tagGroups.tagMatches(feature.getTagsList()) || filterChildren().findAny().isPresent();
    }
}
