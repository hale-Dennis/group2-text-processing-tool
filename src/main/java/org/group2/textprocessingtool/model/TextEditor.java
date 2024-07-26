package org.group2.textprocessingtool.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TextEditor that = (TextEditor) obj;
        return Objects.equals(content, that.content);
    }

    @Override
    public String toString() {
        return String.join("\n", content);
    }

}

