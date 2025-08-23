package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTestTest12 {

    @Test
    public void canParseCdataEndingAtEdgeOfBuffer() {
        String cdataStart = "<![CDATA[";
        String cdataEnd = "]]>";
        // also breaks with -2, but not with -3 or 0
        int bufLen = BufferSize - cdataStart.length() - 1;
        char[] cdataContentsArray = new char[bufLen];
        Arrays.fill(cdataContentsArray, 'x');
        String cdataContents = new String(cdataContentsArray);
        String testMarkup = cdataStart + cdataContents + cdataEnd;
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(testMarkup, "");
        Node cdataNode = doc.body().childNode(0);
        assertTrue(cdataNode instanceof CDataNode, "Expected CDATA node");
        assertEquals(cdataContents, ((CDataNode) cdataNode).text());
    }
}
