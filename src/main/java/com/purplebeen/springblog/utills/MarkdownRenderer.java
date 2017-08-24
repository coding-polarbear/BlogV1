package com.purplebeen.springblog.utills;

import com.oracle.tools.packager.Log;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownRenderer {
    public static String render(String text) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(text);
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        String result = htmlRenderer.render(document);
        Log.debug(result);
        return result;
    }
}
