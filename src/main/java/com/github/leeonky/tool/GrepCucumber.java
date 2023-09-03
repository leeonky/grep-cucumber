package com.github.leeonky.tool;

import io.cucumber.gherkin.Gherkin;
import io.cucumber.messages.Messages;
import io.cucumber.messages.Messages.GherkinDocument.Feature.Scenario;
import io.cucumber.messages.Messages.GherkinDocument.Feature.Tag;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class GrepCucumber {

    @SneakyThrows
    public void select(Path input, Path output, String[]... tags) {
        Messages.GherkinDocument gherkinDoc = Gherkin.fromPaths(Collections.singletonList(input.toString()),
                false, true, false, () -> "").findFirst().get().getGherkinDocument();

        Messages.GherkinDocument.Feature feature = gherkinDoc.getFeature();
        if (tagMatches(feature.getTagsList(), tags)) {
            outputFeature(input, output, feature);
        } else {
            Messages.GherkinDocument.Feature.Builder builder = feature.toBuilder();
            for (int ruleOrScenarioIndex = feature.getChildrenList().size() - 1; ruleOrScenarioIndex >= 0; ruleOrScenarioIndex--) {
                Messages.GherkinDocument.Feature.FeatureChild children = builder.getChildren(ruleOrScenarioIndex);
                switch (children.getValueCase()) {
                    case RULE:
                        Messages.GherkinDocument.Feature.FeatureChild.Rule rule = children.getRule();

                        for (int scenarioIndex = rule.getChildrenCount() - 1; scenarioIndex >= 0; scenarioIndex--) {
                            Messages.GherkinDocument.Feature.FeatureChild.RuleChild children1 = rule.getChildren(scenarioIndex);
                            switch (children1.getValueCase()) {
                                case BACKGROUND:
                                    break;
                                case SCENARIO:
                                    if (!tagMatches(Stream.concat(children1.getScenario().getTagsList().stream(), feature.getTagsList().stream()).collect(toList()), tags)) {
                                        builder.getChildrenBuilder(ruleOrScenarioIndex).getRuleBuilder().removeChildren(scenarioIndex);
                                    }
                                    break;
                                case VALUE_NOT_SET:
                                    break;
                            }
                        }


                        if (rule.getChildrenCount() == 0)
                            builder.removeChildren(ruleOrScenarioIndex);
                        break;
                    case BACKGROUND:
                        break;
                    case SCENARIO:
                        Scenario scenario = children.getScenario();
                        if (!tagMatches(Stream.concat(scenario.getTagsList().stream(), feature.getTagsList().stream()).collect(toList()), tags))
                            builder.removeChildren(ruleOrScenarioIndex);
                        break;
//                    case VALUE_NOT_SET:
//                        break;
                }
            }
            if (builder.getChildrenCount() > 0)
                outputFeature(input, output, builder.build());
        }
    }

    private boolean tagMatches(List<Tag> tags, String[][] tagGroups) {
        return Arrays.stream(tagGroups).anyMatch(tagGroup -> tagMatches(tags.stream().map(Tag::getName).collect(toList()), tagGroup));
    }

    protected boolean tagMatches(List<String> tagNames, String[] input) {
        return tagNames.containsAll(asList(input));
    }

    @SneakyThrows
    private void outputFeature(Path input, Path output, Messages.GherkinDocument.Feature feature) {
        List<String> lines = new ArrayList<>();
        lines.add("# language: " + feature.getLanguage());

        if (feature.getTagsCount() > 0)
            lines.add(feature.getTagsList().stream().map(Tag::getName).collect(Collectors.joining(" ")));

        lines.add(feature.getKeyword() + ": " + feature.getName());
        if (!feature.getDescription().isBlank())
            feature.getDescription().lines().forEach(description -> lines.add("  " + description));

        feature.getChildrenList().forEach(featureChild -> {
            lines.add("");
            switch (featureChild.getValueCase()) {
                case RULE:
                    outputRule(lines, featureChild.getRule(), 1);
                    break;
                case BACKGROUND:
                    break;
                case SCENARIO:
                    outputScenario(lines, featureChild.getScenario(), 1);
                    break;
                case VALUE_NOT_SET:
                    break;
            }
        });
        Path resolve = output.resolve(input.getFileName());
        Files.writeString(resolve, String.join("\n", lines));
    }

    private void outputRule(List<String> lines, Messages.GherkinDocument.Feature.FeatureChild.Rule rule, int intentLevel) {
        String intent = String.join("", Collections.nCopies(intentLevel, "  "));
        lines.add(intent + rule.getKeyword() + ": " + rule.getName());

        rule.getChildrenList().forEach(ruleChild -> {
            lines.add("");
            switch (ruleChild.getValueCase()) {
                case BACKGROUND:
                    break;
                case SCENARIO:
                    outputScenario(lines, ruleChild.getScenario(), intentLevel + 1);
                    break;
                case VALUE_NOT_SET:
                    break;
            }
        });
    }

    private void outputScenario(List<String> lines, Scenario scenario, int intentLevel) {
        String intent = String.join("", Collections.nCopies(intentLevel, "  "));
        if (scenario.getTagsCount() > 0)
            lines.add(intent + scenario.getTagsList().stream().map(Tag::getName).collect(Collectors.joining(" ")));
        lines.add(intent + scenario.getKeyword() + ": " + scenario.getName());
    }
}
