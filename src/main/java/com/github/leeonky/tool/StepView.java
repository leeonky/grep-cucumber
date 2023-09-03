package com.github.leeonky.tool;

import io.cucumber.messages.Messages.GherkinDocument.Feature.Step;

import java.util.Collections;
import java.util.List;

public class StepView extends View {
    private final Step step;

    public StepView(Step step, TagGroups tagGroups) {
        super(tagGroups);
        this.step = step;
    }

    @Override
    protected String getName() {
        return step.getText();
    }

    @Override
    protected String getKeyword() {
        return step.getKeyword();
    }

    @Override
    public boolean matches() {
        return true;
    }

    @Override
    protected View newLine(List<String> lines) {
        return this;
    }

    @Override
    public void output(List<String> lines, int intentLevel) {
        String intent = String.join("", Collections.nCopies(intentLevel, "  "));
        lines.add(intent + getKeyword() + getName());
    }
}
