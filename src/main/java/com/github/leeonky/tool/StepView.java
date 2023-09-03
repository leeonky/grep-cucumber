package com.github.leeonky.tool;

import io.cucumber.messages.Messages.GherkinDocument.Feature.Step;
import io.cucumber.messages.Messages.GherkinDocument.Feature.TableRow;

import java.util.ArrayList;
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
        outputTableAndDoc().forEach(l -> lines.add(intent + "  " + l));
    }

    private List<String> outputTableAndDoc() {
        if (step.hasDataTable())
            return outputTable(step.getDataTable().getRowsList());
        else if (step.hasDocString())
            return outputDocString();
        else
            return Collections.emptyList();
    }

    private List<String> outputDocString() {
        return new ArrayList<>() {{
            Step.DocString docString = step.getDocString();
            add(docString.getDelimiter() + (docString.getMediaType().isBlank() ? "" : " " + docString.getMediaType()));
            docString.getContent().lines().forEach(this::add);
            add(docString.getDelimiter());
        }};
    }

    public static List<String> outputTable(List<TableRow> rowsList) {
        int[] width = calculateWidth(rowsList);
        return rowsList.stream().map(row -> {
            StringJoiner joiner = new StringJoiner(" | ", "| ", " |");
            for (int cell = 0; cell < row.getCellsList().size(); cell++) {
                joiner.add(String.format(String.format("%%-%ds", width[cell]), row.getCellsList().get(cell).getValue()));
            }
            return joiner.toString();
        }).collect(Collectors.toList());
    }

    private static int[] calculateWidth(List<TableRow> rowsList) {
        int[] width = new int[rowsList.get(0).getCellsCount()];
        for (TableRow row : rowsList)
            for (int cell = 0; cell < row.getCellsList().size(); cell++)
                width[cell] = Math.max(width[cell], row.getCells(cell).getValue().length());
        return width;
    }
}
