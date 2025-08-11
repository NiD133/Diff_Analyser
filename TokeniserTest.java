package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Tokeniser buffer management, edge cases around large inputs, and Windows-1252 entity handling.
 * The helpers at the bottom reduce duplication and make intent explicit.
 */
public class TokeniserTest {

    // ---------------------------------------------------------------------------------------------
    // Buffer boundary and attribute parsing
    // ---------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Attribute value crossing the internal character buffer boundary is parsed correctly")
    public void bufferUpInAttributeVal() {
        // See https://github.com/jhy/jsoup/issues/967
        // Validate handling for double-quoted, single-quoted, and unquoted attribute values.
        String[] quoteVariants = {"\"", "'", ""};

        for (String quote : quoteVariants) {
            String preamble = "<img src=" + quote;

            // Fill the internal CharacterReader buffer exactly up to BufferSize with 'a' characters,
            // then add a marker 'X' that crosses the buffer boundary, followed by a long tail.
            String longTail = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
            int charsToFill = BufferSize - preamble.length();

            String html = new StringBuilder(preamble)
                .append(repeat('a', charsToFill))
                .append('X')               // first char crossing the buffer boundary
                .append(longTail)
                .append(quote).append(">\n")
                .toString();

            Document doc = Jsoup.parse(html);
            String src = doc.select("img").attr("src");

            assertTrue(src.contains("X"), "Should retain marker across buffer boundary for quote variant: " + printable(quote));
            assertTrue(src.contains(longTail), "Should retain full tail for quote variant: " + printable(quote));
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Very large tag and attribute names
    // ---------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Super large tag names are parsed and selected correctly")
    public void handleSuperLargeTagNames() {
        String tagName = repeatUntilLength("LargeTagName", BufferSize);
        String html = "<" + tagName + ">One</" + tagName + ">";

        Document doc = Parser.htmlParser().settings(ParseSettings.preserveCase).parseInput(html, "");
        Element el = assertSingle(doc.select(tagName), "tag: " + tagName);
        assertEquals("One", el.text(), "Should capture text content inside large tag");
        assertEquals(tagName, el.tagName(), "Should preserve case for large tag when requested");
    }

    @Test
    @DisplayName("Super large attribute names are parsed correctly and normalized to lowercase in HTML")
    public void handleSuperLargeAttributeName() {
        String attrName = repeatUntilLength("LargAttributeName", BufferSize); // original seed retained
        String html = "<p " + attrName + "=foo>One</p>";

        Document doc = Jsoup.parse(html);
        Element el = assertSingle(doc.getElementsByAttribute(attrName), "attribute: " + attrName);
        assertEquals("One", el.text(), "Text content should be preserved");

        Attribute attribute = el.attributes().asList().get(0);
        assertEquals(attrName.toLowerCase(), attribute.getKey(), "HTML attribute keys are lowercased");
        assertEquals("foo", attribute.getValue(), "Attribute value should match");
    }

    // ---------------------------------------------------------------------------------------------
    // Large text nodes, comments, CDATA, titles
    // ---------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Large text inside an element is preserved")
    public void handleLargeText() {
        String text = repeatUntilLength("A Large Amount of Text", BufferSize);
        Document doc = Jsoup.parse("<p>" + text + "</p>");
        Element el = assertSingle(doc.select("p"), "p");
        assertEquals(text, el.text());
    }

    @Test
    @DisplayName("Large HTML comments are preserved")
    public void handleLargeComment() {
        String commentContent = repeatUntilLength("Quite a comment ", BufferSize);
        Document doc = Jsoup.parse("<p><!-- " + commentContent + " --></p>");
        Element el = assertSingle(doc.select("p"), "p");

        Comment comment = assertInstanceOf(Comment.class, el.childNode(0), "First child should be a Comment node");
        assertEquals(" " + commentContent + " ", comment.getData());
    }

    @Test
    @DisplayName("Large CDATA sections are preserved")
    public void handleLargeCdata() {
        String cdataContent = repeatUntilLength("Quite a lot of CDATA <><><><>", BufferSize);
        Document doc = Jsoup.parse("<p><![CDATA[" + cdataContent + "]]></p>");
        Element el = assertSingle(doc.select("p"), "p");

        TextNode child = assertInstanceOf(TextNode.class, el.childNode(0), "First child should be a TextNode from CDATA");
        assertEquals(cdataContent, el.text());
        assertEquals(cdataContent, child.getWholeText());
    }

    @Test
    @DisplayName("Large title elements are preserved and exposed as Document.title()")
    public void handleLargeTitle() {
        String titleText = repeatUntilLength("Quite a long title", BufferSize);
        Document doc = Jsoup.parse("<title>" + titleText + "</title>");
        Element el = assertSingle(doc.select("title"), "title");

        TextNode child = assertInstanceOf(TextNode.class, el.childNode(0), "Title should contain a TextNode");
        assertEquals(titleText, el.text());
        assertEquals(titleText, child.getWholeText());
        assertEquals(titleText, doc.title(), "Document.title() should reflect full title");
    }

    // ---------------------------------------------------------------------------------------------
    // Windows-1252 entity handling (quirks mode mappings)
    // ---------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Windows-1252 numeric entities are unescaped")
    public void cp1252Entities() {
        assertEquals("\u20ac", Jsoup.parse("&#0128;").text(), "Decimal 0128 should map to Euro sign");
        assertEquals("\u201a", Jsoup.parse("&#0130;").text(), "Decimal 0130 should map to single low-9 quotation mark");
        assertEquals("\u20ac", Jsoup.parse("&#x80;").text(), "Hex 0x80 should map to Euro sign");
    }

    @Test
    @DisplayName("Windows-1252 entity substitution triggers a parse error")
    public void cp1252EntitiesProduceError() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);

        assertEquals("\u20ac", parser.parseInput("<html><body>&#0128;</body></html>", "").text());
        assertEquals(1, parser.getErrors().size(), "Substitution should record one parse error");
    }

    @Test
    @DisplayName("Windows-1252 substitution table matches Java's Windows-1252 decoding where applicable")
    public void cp1252SubstitutionTable() {
        for (int i = 0; i < Tokeniser.win1252Extensions.length; i++) {
            String s = new String(
                new byte[]{ (byte) (i + Tokeniser.win1252ExtensionsStart) },
                Charset.forName("Windows-1252")
            );
            assertEquals(1, s.length(), "Decoded string should be one char at index " + i);

            // Some characters decode to replacement char; skip those.
            if (s.charAt(0) == '\ufffd') continue;

            assertEquals(s.charAt(0), Tokeniser.win1252Extensions[i],
                "Mismatch at index " + i + " for Windows-1252 extension");
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Edge cases: bogus comment and CDATA at buffer edge
    // ---------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Very long bogus comment (<!...) is parsed as a comment")
    public void canParseVeryLongBogusComment() {
        StringBuilder sb = new StringBuilder(BufferSize);
        while (sb.length() < BufferSize) sb.append("blah blah blah blah ");
        String expected = sb.toString();

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body><!" + expected + "></body></html>", "");

        Comment comment = assertInstanceOf(Comment.class, doc.body().childNode(0), "Expected a Comment node");
        assertEquals(expected, comment.getData(), "Bogus comment content should be preserved");
    }

    @Test
    @DisplayName("CDATA section ending exactly at the buffer edge is parsed correctly")
    public void canParseCdataEndingAtEdgeOfBuffer() {
        String cdataStart = "<![CDATA[";
        String cdataEnd = "]]>";

        // Choose the content length so that the trailing "]]>" falls right at/near the internal buffer boundary.
        int contentLen = BufferSize - cdataStart.length() - 1; // also fails with -2, passes with -3 or 0 (see original)
        String cdataContent = repeat('x', contentLen);

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(cdataStart + cdataContent + cdataEnd, "");

        CDataNode cdataNode = assertInstanceOf(CDataNode.class, doc.body().childNode(0), "Expected a CDATA node");
        assertEquals(cdataContent, cdataNode.text(), "CDATA text should be preserved");
    }

    // ---------------------------------------------------------------------------------------------
    // TokenData basic behavior
    // ---------------------------------------------------------------------------------------------

    @Test
    @DisplayName("TokenData toString reflects appended content")
    void tokenDataToString() {
        TokenData data = new TokenData();
        assertEquals("", data.toString(), "Empty TokenData should stringify to empty string");
        data.set("abc");
        assertEquals("abc", data.toString(), "Set value should be reflected");
        data.append("def");
        assertEquals("abcdef", data.toString(), "Append should concatenate");
    }

    // ---------------------------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------------------------

    private static String repeatUntilLength(String seed, int minLen) {
        StringBuilder sb = new StringBuilder(minLen);
        while (sb.length() < minLen) sb.append(seed);
        return sb.toString();
    }

    private static String repeat(char ch, int count) {
        char[] arr = new char[count];
        Arrays.fill(arr, ch);
        return new String(arr);
    }

    private static Element assertSingle(Elements elements, String selectorDescription) {
        assertEquals(1, elements.size(), "Expected exactly one element for selector: " + selectorDescription);
        Element el = elements.get(0);
        assertNotNull(el, "Selected element should not be null");
        return el;
    }

    private static String printable(String s) {
        return s.isEmpty() ? "<unquoted>" : s;
    }
}