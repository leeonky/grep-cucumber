package com.github.leeonky.tool;

import io.cucumber.gherkin.Gherkin;
import io.cucumber.messages.Messages;
import io.cucumber.messages.Messages.GherkinDocument.Feature.Tag;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class GrepCucumber {
    @SneakyThrows
    public void select(Path input, Path output, String[]... tags) {
        Messages.GherkinDocument gherkinDoc = Gherkin.fromPaths(Collections.singletonList(input.toString()),
                false, true, false, () -> "").findFirst().get().getGherkinDocument();

        Messages.GherkinDocument.Feature feature = gherkinDoc.getFeature();

        if (feature.getTagsList().stream().map(Tag::getName).collect(Collectors.toList()).containsAll(asList(tags[0]))) {
            List<String> lines = new ArrayList<>();
            lines.add("# language: " + feature.getLanguage());

            if (feature.getTagsCount() > 0)
                lines.add(feature.getTagsList().stream().map(Tag::getName).collect(Collectors.joining(" ")));

            lines.add(feature.getKeyword() + ": " + feature.getName());
            if (!feature.getDescription().isBlank())
                feature.getDescription().lines().forEach(description -> lines.add("  " + description));
            Path resolve = output.resolve(input.getFileName());
            Files.writeString(resolve, String.join("\n", lines));
        }
    }
}
