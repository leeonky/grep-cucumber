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
    public void select(Path input, Path output, String[]... tags) {
        Messages.GherkinDocument gherkinDoc = Gherkin.fromPaths(Collections.singletonList(input.toString()),
                false, true, false, () -> "").findFirst().get().getGherkinDocument();
        TagGroups tagGroups = new TagGroups(tags);
        FeatureView featureView = new FeatureView(gherkinDoc.getFeature(), tagGroups);
        if (featureView.matches(tagGroups)) {
            List<String> lines = new ArrayList<>();
            featureView.output(lines, 0);
            Path resolve = output.resolve(input.getFileName());
            Files.writeString(resolve, String.join("\n", lines));
        }
    }
}
