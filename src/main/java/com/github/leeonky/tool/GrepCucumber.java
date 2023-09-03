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
            for (int i = feature.getChildrenList().size() - 1; i >= 0; i--) {
                Scenario scenario = builder.getChildren(i).getScenario();
                if (!tagMatches(Stream.concat(scenario.getTagsList().stream(), feature.getTagsList().stream()).collect(toList()), tags))
                    builder.removeChildren(i);
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
            Scenario scenario = featureChild.getScenario();
            if (scenario.getTagsCount() > 0)
                lines.add("  " + scenario.getTagsList().stream().map(Tag::getName).collect(Collectors.joining(" ")));
            lines.add("  " + scenario.getKeyword() + ": " + scenario.getName());
        });
        Path resolve = output.resolve(input.getFileName());
        Files.writeString(resolve, String.join("\n", lines));
    }
}
