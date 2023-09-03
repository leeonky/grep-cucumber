package com.github.leeonky.tool;

import io.cucumber.messages.Messages.GherkinDocument.Feature.Step;
import io.cucumber.messages.Messages.GherkinDocument.Feature.TableRow;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
        if (step.hasDataTable())
            outputTable(calculateWidth()).forEach(l -> lines.add(intent + "  " + l));
    }

    private List<String> outputTable(int[] width) {
        return step.getDataTable().getRowsList().stream().map(row -> {
            StringJoiner joiner = new StringJoiner(" | ", "| ", " |");
            for (int cell = 0; cell < row.getCellsList().size(); cell++)
                joiner.add(String.format(String.format("%%-%ds", width[cell]), row.getCellsList().get(cell).getValue()));
            return joiner.toString();
        }).collect(Collectors.toList());
    }

    private int[] calculateWidth() {
        int[] width = new int[step.getDataTable().getRows(0).getCellsCount()];
        for (TableRow row : step.getDataTable().getRowsList())
            for (int cell = 0; cell < row.getCellsList().size(); cell++)
                width[cell] = Math.max(width[cell], row.getCells(cell).getValue().length());
        return width;
    }
}
