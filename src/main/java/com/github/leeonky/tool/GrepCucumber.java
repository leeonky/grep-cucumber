package com.github.leeonky.tool;

import io.cucumber.gherkin.Gherkin;
import io.cucumber.messages.Messages;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrepCucumber {

    @SneakyThrows
    public void select(Path input, Path output, TagGroups tagGroups) {
        if (input.toFile().isFile())
            selectFile(input, output, input.getParent(), tagGroups);
        else
            Files.walk(input).filter(path -> path.toFile().isFile()).forEach(file -> selectFile(file, output, input, tagGroups));
    }

    @SneakyThrows
    private void selectFile(Path feature, Path output, Path featureFolder, TagGroups tagGroups) {
        Messages.GherkinDocument gherkinDoc = Gherkin.fromPaths(Collections.singletonList(feature.toString()),
                false, true, false, () -> "").findFirst().get().getGherkinDocument();
        FeatureView featureView = new FeatureView(gherkinDoc.getFeature(), tagGroups);
        if (featureView.matches()) {
            List<String> lines = new ArrayList<>();
            featureView.output(lines, 0);
            Path outputFile = output.resolve(featureFolder.relativize(feature));
            Files.createDirectories(outputFile.getParent());
            Files.writeString(outputFile, String.join("\n", lines));
        }
    }
}
