package com.github.leeonky.tool;

import io.cucumber.messages.Messages;
import io.cucumber.messages.Messages.GherkinDocument.Feature.Scenario;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ScenarioView implements View {
    private final Scenario scenario;
    private final FeatureView featureView;

    public ScenarioView(Scenario scenario, FeatureView featureView) {
        this.scenario = scenario;
        this.featureView = featureView;
    }

    @Override
    public void output(List<String> lines, int intentLevel) {
        String intent = String.join("", Collections.nCopies(intentLevel, "  "));
        if (scenario.getTagsCount() > 0)
            lines.add(intent + scenario.getTagsList().stream().map(Messages.GherkinDocument.Feature.Tag::getName).collect(Collectors.joining(" ")));
        lines.add(intent + scenario.getKeyword() + ": " + scenario.getName());
    }

    @Override
    public boolean matches(TagGroups tagGroups) {
        return tagGroups.tagMatches(getAllTagsList());
    }

    private List<Messages.GherkinDocument.Feature.Tag> getAllTagsList() {
        return Stream.concat(scenario.getTagsList().stream(), featureView.getTagsList().stream()).collect(toList());
    }
}
