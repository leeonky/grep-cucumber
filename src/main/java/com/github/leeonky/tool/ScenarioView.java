package com.github.leeonky.tool;

import io.cucumber.messages.Messages;
import io.cucumber.messages.Messages.GherkinDocument.Feature.Scenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.github.leeonky.tool.StepView.outputTable;
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
    public void output(List<String> lines, int intentLevel) {
        super.output(lines, intentLevel);

        String intent = String.join("", Collections.nCopies(intentLevel + 1, "  "));
        for (Scenario.Examples examples : scenario.getExamplesList()) {
            lines.add(intent + examples.getKeyword() + ":");
            outputTable(new ArrayList<>() {{
                add(examples.getTableHeader());
                addAll(examples.getTableBodyList());
            }}).forEach(l -> lines.add(intent + "  " + l));
        }
    }

    @Override
    protected Stream<View> getChildren() {
        return scenario.getStepsList().stream().map(step -> new StepView(step, tagGroups));
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
