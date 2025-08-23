package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest11 {

    @Test
    public void canParseVeryLongBogusComment() {
        StringBuilder commentData = new StringBuilder(BufferSize);
        do {
            commentData.append("blah blah blah blah ");
        } while (commentData.length() < BufferSize);
        String expectedCommentData = commentData.toString();
        String testMarkup = "<html><body><!" + expectedCommentData + "></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(testMarkup, "");
        Node commentNode = doc.body().childNode(0);
        assertTrue(commentNode instanceof Comment, "Expected comment node");
        assertEquals(expectedCommentData, ((Comment) commentNode).getData());
    }
}