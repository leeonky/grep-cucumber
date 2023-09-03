package com.github.leeonky.tool;

import io.cucumber.messages.Messages;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class TagGroups {
    private final String[][] tagGroups;

    public TagGroups(String[][] tagGroups) {
        this.tagGroups = tagGroups;
    }

    protected boolean tagMatches(List<String> tagNames, String[] input) {
        return tagNames.containsAll(asList(input));
    }

    public boolean tagMatches(List<Messages.GherkinDocument.Feature.Tag> tags) {
        return stream(tagGroups).anyMatch(tagGroup ->
                tagMatches(tags.stream().map(Messages.GherkinDocument.Feature.Tag::getName).collect(toList()), tagGroup));
    }
}
