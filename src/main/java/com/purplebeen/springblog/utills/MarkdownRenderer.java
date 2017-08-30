package com.purplebeen.springblog.utills;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownRenderer {
    public static String render(String text) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(text);
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        String result = htmlRenderer.render(document);
        return result;
    }
}
