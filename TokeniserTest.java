package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

public class TokeniserTest {

    @Test
    public void testBufferBoundaryInAttributeValue() {
        // Test to ensure that attribute values crossing the buffer boundary are handled correctly.
        // Related to issue: https://github.com/jhy/jsoup/issues/967

        String[] quoteTypes = {"\"", "'", ""}; // Test with double, single, and unquoted attribute values
        for (String quote : quoteTypes) {
            String preamble = "<img src=" + quote;
            String boundaryMarker = "X"; // Character that crosses the buffer boundary
            String tail = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"; // Tail to ensure buffer overflow

            // Construct a string that fills the buffer up to the boundary
            StringBuilder htmlBuilder = new StringBuilder(preamble);
            int charsToFillBuffer = BufferSize - preamble.length();
            for (int i = 0; i < charsToFillBuffer; i++) {
                htmlBuilder.append('a');
            }
            htmlBuilder.append(boundaryMarker).append(tail).append(quote).append(">\n");

            // Parse the HTML and verify the attribute value
            Document doc = Jsoup.parse(htmlBuilder.toString());
            String src = doc.select("img").attr("src");

            assertTrue(src.contains(boundaryMarker), "Boundary marker should be present for quote: " + quote);
            assertTrue(src.contains(tail), "Tail should be present for quote: " + quote);
        }
    }

    @Test
    public void testHandlingSuperLargeTagNames() {
        // Test to ensure that very large tag names are handled correctly.

        StringBuilder tagNameBuilder = new StringBuilder(BufferSize);
        while (tagNameBuilder.length() < BufferSize) {
            tagNameBuilder.append("LargeTagName");
        }
        String largeTagName = tagNameBuilder.toString();
        String html = "<" + largeTagName + ">One</" + largeTagName + ">";

        Document doc = Parser.htmlParser().settings(ParseSettings.preserveCase).parseInput(html, "");
        Elements elements = doc.select(largeTagName);

        assertEquals(1, elements.size(), "There should be one element with the large tag name.");
        Element element = elements.first();
        assertNotNull(element, "Element should not be null.");
        assertEquals("One", element.text(), "Element text should match.");
        assertEquals(largeTagName, element.tagName(), "Tag name should match the large tag name.");
    }

    @Test
    public void testHandlingSuperLargeAttributeNames() {
        // Test to ensure that very large attribute names are handled correctly.

        StringBuilder attrNameBuilder = new StringBuilder(BufferSize);
        while (attrNameBuilder.length() < BufferSize) {
            attrNameBuilder.append("LargeAttributeName");
        }
        String largeAttrName = attrNameBuilder.toString();
        String html = "<p " + largeAttrName + "=foo>One</p>";

        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByAttribute(largeAttrName);

        assertEquals(1, elements.size(), "There should be one element with the large attribute name.");
        Element element = elements.first();
        assertNotNull(element, "Element should not be null.");
        assertEquals("One", element.text(), "Element text should match.");

        Attribute attribute = element.attributes().asList().get(0);
        assertEquals(largeAttrName.toLowerCase(), attribute.getKey(), "Attribute name should match.");
        assertEquals("foo", attribute.getValue(), "Attribute value should match.");
    }

    @Test
    public void testHandlingLargeText() {
        // Test to ensure that large text content is handled correctly.

        StringBuilder textBuilder = new StringBuilder(BufferSize);
        while (textBuilder.length() < BufferSize) {
            textBuilder.append("A Large Amount of Text");
        }
        String largeText = textBuilder.toString();
        String html = "<p>" + largeText + "</p>";

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("p");

        assertEquals(1, elements.size(), "There should be one paragraph element.");
        Element element = elements.first();
        assertNotNull(element, "Element should not be null.");
        assertEquals(largeText, element.text(), "Element text should match the large text.");
    }

    @Test
    public void testHandlingLargeComment() {
        // Test to ensure that large comments are handled correctly.

        StringBuilder commentBuilder = new StringBuilder(BufferSize);
        while (commentBuilder.length() < BufferSize) {
            commentBuilder.append("Quite a comment ");
        }
        String largeComment = commentBuilder.toString();
        String html = "<p><!-- " + largeComment + " --></p>";

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("p");

        assertEquals(1, elements.size(), "There should be one paragraph element.");
        Element element = elements.first();
        assertNotNull(element, "Element should not be null.");

        Comment commentNode = (Comment) element.childNode(0);
        assertEquals(" " + largeComment + " ", commentNode.getData(), "Comment data should match the large comment.");
    }

    @Test
    public void testHandlingLargeCdata() {
        // Test to ensure that large CDATA sections are handled correctly.

        StringBuilder cdataBuilder = new StringBuilder(BufferSize);
        while (cdataBuilder.length() < BufferSize) {
            cdataBuilder.append("Quite a lot of CDATA <><><><>");
        }
        String largeCdata = cdataBuilder.toString();
        String html = "<p><![CDATA[" + largeCdata + "]]></p>";

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("p");

        assertEquals(1, elements.size(), "There should be one paragraph element.");
        Element element = elements.first();
        assertNotNull(element, "Element should not be null.");

        TextNode cdataNode = (TextNode) element.childNode(0);
        assertEquals(largeCdata, element.text(), "Element text should match the large CDATA.");
        assertEquals(largeCdata, cdataNode.getWholeText(), "CDATA node text should match the large CDATA.");
    }

    @Test
    public void testHandlingLargeTitle() {
        // Test to ensure that large titles are handled correctly.

        StringBuilder titleBuilder = new StringBuilder(BufferSize);
        while (titleBuilder.length() < BufferSize) {
            titleBuilder.append("Quite a long title");
        }
        String largeTitle = titleBuilder.toString();
        String html = "<title>" + largeTitle + "</title>";

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("title");

        assertEquals(1, elements.size(), "There should be one title element.");
        Element element = elements.first();
        assertNotNull(element, "Element should not be null.");

        TextNode titleNode = (TextNode) element.childNode(0);
        assertEquals(largeTitle, element.text(), "Element text should match the large title.");
        assertEquals(largeTitle, titleNode.getWholeText(), "Title node text should match the large title.");
        assertEquals(largeTitle, doc.title(), "Document title should match the large title.");
    }

    @Test
    public void testCp1252Entities() {
        // Test to ensure that CP1252 entities are parsed correctly.

        assertEquals("\u20ac", Jsoup.parse("&#0128;").text(), "Entity &#0128; should be parsed as €.");
        assertEquals("\u201a", Jsoup.parse("&#0130;").text(), "Entity &#0130; should be parsed as ‚.");
        assertEquals("\u20ac", Jsoup.parse("&#x80;").text(), "Entity &#x80; should be parsed as €.");
    }

    @Test
    public void testCp1252EntitiesProduceError() {
        // Test to ensure that CP1252 entities produce errors as expected.

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);

        assertEquals("\u20ac", parser.parseInput("<html><body>&#0128;</body></html>", "").text(), "Entity &#0128; should be parsed as €.");
        assertEquals(1, parser.getErrors().size(), "There should be one parsing error.");
    }

    @Test
    public void testCp1252SubstitutionTable() {
        // Test to ensure that the CP1252 substitution table is correct.

        for (int i = 0; i < Tokeniser.win1252Extensions.length; i++) {
            String character = new String(new byte[]{ (byte) (i + Tokeniser.win1252ExtensionsStart) }, Charset.forName("Windows-1252"));
            assertEquals(1, character.length(), "Character length should be 1.");

            // Some characters are illegal and should be skipped
            if (character.charAt(0) == '\ufffd') {
                continue;
            }

            assertEquals(character.charAt(0), Tokeniser.win1252Extensions[i], "Character at index " + i + " should match.");
        }
    }

    @Test
    public void testParsingVeryLongBogusComment() {
        // Test to ensure that very long bogus comments are parsed correctly.

        StringBuilder commentDataBuilder = new StringBuilder(BufferSize);
        while (commentDataBuilder.length() < BufferSize) {
            commentDataBuilder.append("blah blah blah blah ");
        }
        String expectedCommentData = commentDataBuilder.toString();
        String html = "<html><body><!" + expectedCommentData + "></body></html>";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "");

        Node commentNode = doc.body().childNode(0);
        assertTrue(commentNode instanceof Comment, "Expected a comment node.");
        assertEquals(expectedCommentData, ((Comment) commentNode).getData(), "Comment data should match.");
    }

    @Test
    public void testParsingCdataEndingAtBufferEdge() {
        // Test to ensure that CDATA sections ending at the buffer edge are parsed correctly.

        String cdataStart = "<![CDATA[";
        String cdataEnd = "]]>";
        int bufferLength = BufferSize - cdataStart.length() - 1;
        char[] cdataContentsArray = new char[bufferLength];
        Arrays.fill(cdataContentsArray, 'x');
        String cdataContents = new String(cdataContentsArray);
        String html = cdataStart + cdataContents + cdataEnd;

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "");

        Node cdataNode = doc.body().childNode(0);
        assertTrue(cdataNode instanceof CDataNode, "Expected a CDATA node.");
        assertEquals(cdataContents, ((CDataNode) cdataNode).text(), "CDATA content should match.");
    }

    @Test
    public void testTokenDataToString() {
        // Test to ensure that TokenData toString method works correctly.

        TokenData data = new TokenData();
        assertEquals("", data.toString(), "Initial TokenData should be empty.");

        data.set("abc");
        assertEquals("abc", data.toString(), "TokenData should be 'abc' after setting.");

        data.append("def");
        assertEquals("abcdef", data.toString(), "TokenData should be 'abcdef' after appending.");
    }
}