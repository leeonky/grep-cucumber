package com.github.leeonky.tool;

import io.cucumber.messages.Messages;
import io.cucumber.messages.Messages.GherkinDocument.Feature.Scenario;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ScenarioView extends View {
    private final Scenario scenario;
    private final FeatureView featureView;

    public ScenarioView(Scenario scenario, FeatureView featureView, TagGroups tagGroups) {
        super(tagGroups);
        this.scenario = scenario;
        this.featureView = featureView;
    }

    @Override
    protected Stream<View> getChildren() {
        return Stream.empty();
    }

    @Override
    protected String getDescription() {
        return scenario.getDescription();
    }

    @Override
    protected String getName() {
        return scenario.getName();
    }

    @Override
    protected String getKeyword() {
        return scenario.getKeyword();
    }

    @Override
    public List<Messages.GherkinDocument.Feature.Tag> getOwnTags() {
        return scenario.getTagsList();
    }

    @Override
    public boolean matches() {
        return tagGroups.tagMatches(Stream.concat(scenario.getTagsList().stream(), featureView.getOwnTags().stream()).collect(toList()));
    }
}
