package com.github.leeonky.tool;

import io.cucumber.messages.Messages;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class View {
    protected final TagGroups tagGroups;

    public View(TagGroups tagGroups) {
        this.tagGroups = tagGroups;
    }

    protected Stream<View> filterChildren() {
        return getChildren().filter(View::matches);
    }

    public void output(List<String> lines, int intentLevel) {
        String intent = String.join("", Collections.nCopies(intentLevel, "  "));
        if (getOwnTags().size() > 0)
            lines.add(intent + getOwnTags().stream().map(Messages.GherkinDocument.Feature.Tag::getName).collect(Collectors.joining(" ")));

        lines.add(intent + getKeyword() + ": " + getName());
        if (!getDescription().isBlank())
            getDescription().lines().forEach(description -> lines.add(intent + "  " + description.trim()));

        filterChildren().forEach(view -> view.newLine(lines).output(lines, intentLevel + 1));
    }

    protected View newLine(List<String> lines) {
        lines.add("");
        return this;
    }

    protected Stream<View> getChildren() {
        return null;
    }

    protected String getDescription() {
        return "";
    }

    protected abstract String getName();

    protected abstract String getKeyword();

    public List<Messages.GherkinDocument.Feature.Tag> getOwnTags() {
        return Collections.emptyList();
    }

    public abstract boolean matches();
}
