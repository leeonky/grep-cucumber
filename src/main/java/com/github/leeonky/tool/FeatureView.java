package com.github.leeonky.tool;

import io.cucumber.messages.Messages.GherkinDocument.Feature;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeatureView implements View {
    private final Feature feature;
    private final TagGroups tagGroups;

    public FeatureView(Feature feature, TagGroups tagGroups) {
        this.feature = feature;
        this.tagGroups = tagGroups;
    }

    private Stream<View> getChildren() {
        return feature.getChildrenList().stream().map(featureChild -> {
            switch (featureChild.getValueCase()) {
                case RULE:
                    return new RuleView(featureChild.getRule(), this, tagGroups);
                case SCENARIO:
                    return new ScenarioView(featureChild.getScenario(), this);
                case BACKGROUND:
                case VALUE_NOT_SET:
                    return null;
            }
            return null;
        }).filter(view -> view.matches(tagGroups));
    }

    public List<Feature.Tag> getTagsList() {
        return feature.getTagsList();
    }

    @Override
    public void output(List<String> lines, int intentLevel) {
        lines.add("# language: " + feature.getLanguage());

        if (feature.getTagsCount() > 0)
            lines.add(feature.getTagsList().stream().map(Feature.Tag::getName).collect(Collectors.joining(" ")));

        lines.add(feature.getKeyword() + ": " + feature.getName());
        if (!feature.getDescription().isBlank())
            feature.getDescription().lines().forEach(description -> lines.add("  " + description));

        getChildren().forEach(ob -> {
            lines.add("");
            ob.output(lines, 1);
        });
    }

    @Override
    public boolean matches(TagGroups tagGroups) {
        return tagGroups.tagMatches(feature.getTagsList()) || getChildren().findAny().isPresent();
    }
}
