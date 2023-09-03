package com.github.leeonky.tool;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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

    @When("grep {string} and specify tag: {string}")
    public void grepAndSpecifyTag(String file, String tags) {
        tempFiles.createDirectory("output");
        new GrepCucumber().select(tempFiles.getAbsolutePath(file), tempFiles.getAbsolutePath("output"), tags.split(","));
    }
}
