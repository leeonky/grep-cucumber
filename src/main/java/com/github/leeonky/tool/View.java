package com.github.leeonky.tool;

import java.util.List;

public interface View {
    void output(List<String> lines, int intentLevel);

    boolean matches(TagGroups tagGroups);
}
