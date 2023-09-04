package com.github.leeonky.tool;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
        tempFiles.createWithContent("input/" + file, content);
    }

    @When("grep {string}")
    public void grep(String file) {
        tempFiles.createDirectory("output");
        final TagGroups tagGroups = new TagGroups(new String[][]{});
        new GrepCucumber().select(tempFiles.getAbsolutePath("input/" + file), tempFiles.getAbsolutePath("output"), tagGroups);
    }

    @Then("output should be:")
    public void outputShouldBe(String expression) {
        expect(tempFiles.getAbsolutePath("output")).should(expression);
    }

    @When("grep {string} and specify tag: {string}")
    public void grepAndSpecifyTag(String file, String tags) {
        tempFiles.createDirectory("output");
        final TagGroups tagGroups = new TagGroups(new String[][]{tags.split(",")});
        new GrepCucumber().select(tempFiles.getAbsolutePath("input/" + file), tempFiles.getAbsolutePath("output"), tagGroups);
    }

    @When("grep {string} and specify tag: {string} and {string}")
    public void grepAndSpecifyTagAnd(String file, String tags, String tags2) {
        tempFiles.createDirectory("output");
        final TagGroups tagGroups = new TagGroups(new String[][]{tags.split(","), tags2.split(",")});
        new GrepCucumber().select(tempFiles.getAbsolutePath("input/" + file), tempFiles.getAbsolutePath("output"), tagGroups);
    }

    @When("grep {string} and specify tag: {string} and always matching tag group")
    public void grepAndSpecifyTagAndAlwaysMatchingTagGroup(String file, String tags) {
        tempFiles.createDirectory("output");
        final TagGroups tagGroups = new TagGroups(new String[][]{tags.split(",")}){
            @Override
            protected boolean tagMatches(List<String> tagNames, String[] input) {
                return true;
            }
        };
        new GrepCucumber().select(tempFiles.getAbsolutePath("input/" + file), tempFiles.getAbsolutePath("output"), tagGroups);
    }
}
