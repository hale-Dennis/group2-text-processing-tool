package org.group2.textprocessingtool.model;

import java.util.ArrayList;
import java.util.List;

public class TextEditor {
    private final List<String> content;

    public TextEditor() {
        content = new ArrayList<>();
    }


    public List<String> getContent() {
        return content;
    }

    public void addText(String text) {
        content.add(text);
    }
}

