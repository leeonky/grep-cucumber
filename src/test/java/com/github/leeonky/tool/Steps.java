package com.github.leeonky.tool;

import io.cucumber.gherkin.Gherkin;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.Messages;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.leeonky.dal.Assertions.expect;


public class Steps {
    TempFiles tempFiles = TempFiles.tempFiles();

    @Before
    public void clean() {
        tempFiles.clean();
    }

    @Given("a feature at {string}:")
    public void aFeatureAt(String file, String content) {
        tempFiles.createWithContent(file, content);
    }

    @When("grep {string}")
    public void grep(String file) {
        tempFiles.createDirectory("output");
        new GrepCucumber().select(tempFiles.getAbsolutePath(file), tempFiles.getAbsolutePath("output"));
    }

    @Then("output should be:")
    public void outputShouldBe(String expression) {
        expect(tempFiles.getAbsolutePath("output")).should(expression);
    }

    static class GrepCucumber {
        @SneakyThrows
        public void select(Path input, Path output) {
            Messages.GherkinDocument gherkinDoc = Gherkin.fromPaths(Collections.singletonList(input.toString()),
                    false, true, false, () -> "").findFirst().get().getGherkinDocument();

            List<String> lines = new ArrayList<>();
            Messages.GherkinDocument.Feature feature = gherkinDoc.getFeature();
            lines.add("# language: " + feature.getLanguage());
            lines.add(feature.getKeyword() + ": " + feature.getName());
            Path resolve = output.resolve(input.getFileName());
            Files.writeString(resolve, String.join("\n", lines));
        }
    }

//    format
//    path or file
}
