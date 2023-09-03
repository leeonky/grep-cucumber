package com.github.leeonky.tool;

import io.cucumber.messages.Messages.GherkinDocument.Feature.Background;

import java.util.stream.Stream;

public class BackgroundView extends View {
    private final Background background;

    public BackgroundView(Background background, TagGroups tagGroups) {
        super(tagGroups);
        this.background = background;
    }

    @Override
    protected Stream<View> getChildren() {
        return Stream.empty();
    }

    @Override
    protected String getDescription() {
        return background.getDescription();
    }

    @Override
    protected String getName() {
        return background.getName();
    }

    @Override
    protected String getKeyword() {
        return background.getKeyword();
    }

    @Override
    public boolean matches() {
        return true;
    }
}
