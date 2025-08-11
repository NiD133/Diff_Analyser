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
    // Constants for reusable test values
    private static final int TEST_BUFFER_SIZE = BufferSize;
    private static final String LONG_STRING_PATTERN = "AAAA";
    private static final String LARGE_TAG_NAME_PATTERN = "LargeTagName";
    private static final String LARGE_ATTR_NAME_PATTERN = "LargeAttributeName";
    private static final String LONG_TEXT_PATTERN = "Text ";
    private static final String LONG_COMMENT_PATTERN = "Comment ";
    private static final String LONG_CDATA_PATTERN = "CDATA<> ";
    private static final String LONG_TITLE_PATTERN = "Title ";

    // ========================================================================
    // Tests for buffer boundary handling
    // ========================================================================

    @Test
    void testHandlesBufferCrossingInAttributeValues() {
        /* Tests tokenization when attribute values cross internal buffer boundaries */
        final String[] QUOTES = {"\"", "'", ""};  // Double, single, unquoted
        final String TAIL = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";

        for (String quote : QUOTES) {
            // Build attribute value that crosses buffer boundary
            String preamble = "<img src=" + quote;
            int fillCount = TEST_BUFFER_SIZE - preamble.length();
            
            StringBuilder sb = new StringBuilder(preamble)
                .append(generateRepeatedString('a', fillCount))  // Fill buffer
                .append('X')                                    // Cross boundary
                .append(TAIL).append(quote).append(">");         // Remainder
            
            // Parse and verify attribute value
            Document doc = Jsoup.parse(sb.toString());
            String src = doc.select("img").attr("src");
            
            assertTrue(src.contains("X"), "Should contain boundary-crossing character");
            assertTrue(src.contains(TAIL), "Should contain tail data");
        }
    }

    // ========================================================================
    // Tests for large HTML components
    // ========================================================================

    @Test 
    void testParsesTagsWithSizeExceedingBuffer() {
        /* Tests tag names larger than the internal buffer */
        String largeTag = generateRepeatedString(LARGE_TAG_NAME_PATTERN, TEST_BUFFER_SIZE);
        String html = "<" + largeTag + ">Content</" + largeTag + ">";

        Document doc = Parser.htmlParser()
            .settings(ParseSettings.preserveCase)
            .parseInput(html, "");
        
        Elements els = doc.select(largeTag);
        assertEquals(1, els.size(), "Should find one element");
        
        Element el = els.first();
        assertEquals("Content", el.text());
        assertEquals(largeTag, el.tagName(), "Tag name should match");
    }

    @Test 
    void testParsesAttributesWithSizeExceedingBuffer() {
        /* Tests attribute names larger than internal buffer */
        String largeAttr = generateRepeatedString(LARGE_ATTR_NAME_PATTERN, TEST_BUFFER_SIZE);
        String html = "<div " + largeAttr + "=value>Content</div>";

        Document doc = Jsoup.parse(html);
        Elements els = doc.getElementsByAttribute(largeAttr);
        assertEquals(1, els.size(), "Should find one element");
        
        Element el = els.first();
        Attribute attr = el.attributes().asList().get(0);
        assertEquals(largeAttr.toLowerCase(), attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test 
    void testParsesLargeTextNodes() {
        /* Tests text content larger than internal buffer */
        String largeText = generateRepeatedString(LONG_TEXT_PATTERN, TEST_BUFFER_SIZE);
        String html = "<p>" + largeText + "</p>";

        Document doc = Jsoup.parse(html);
        Element p = doc.selectFirst("p");
        
        assertNotNull(p);
        assertEquals(largeText, p.text());
    }

    @Test 
    void testParsesLargeComments() {
        /* Tests comments larger than internal buffer */
        String largeComment = generateRepeatedString(LONG_COMMENT_PATTERN, TEST_BUFFER_SIZE);
        String html = "<!--" + largeComment + "-->";

        Document doc = Jsoup.parse(html);
        Comment comment = (Comment) doc.body().childNode(0);
        
        assertNotNull(comment);
        assertEquals(largeComment, comment.getData().trim());
    }

    @Test 
    void testParsesLargeCdataSections() {
        /* Tests CDATA sections larger than internal buffer */
        String largeCdata = generateRepeatedString(LONG_CDATA_PATTERN, TEST_BUFFER_SIZE);
        String html = "<![CDATA[" + largeCdata + "]]>";

        Document doc = Jsoup.parse(html);
        CDataNode cdata = (CDataNode) doc.body().childNode(0);
        
        assertNotNull(cdata);
        assertEquals(largeCdata, cdata.text());
    }

    @Test 
    void testParsesLargeTitleElements() {
        /* Tests <title> elements larger than internal buffer */
        String largeTitle = generateRepeatedString(LONG_TITLE_PATTERN, TEST_BUFFER_SIZE);
        String html = "<title>" + largeTitle + "</title>";

        Document doc = Jsoup.parse(html);
        Element title = doc.selectFirst("title");
        
        assertNotNull(title);
        assertEquals(largeTitle, title.text());
    }

    @Test 
    void testParsesVeryLongBogusComments() {
        /* Tests bogus comments larger than internal buffer */
        String comment = generateRepeatedString("Bogus ", TEST_BUFFER_SIZE);
        String html = "<!" + comment + ">";

        Document doc = new Parser(new HtmlTreeBuilder()).parseInput(html, "");
        Comment commentNode = (Comment) doc.body().childNode(0);
        
        assertEquals(comment, commentNode.getData());
    }

    // ========================================================================
    // Tests for edge case handling
    // ========================================================================

    @Test 
    void testHandlesCdataEndingAtBufferEdge() {
        /* Tests CDATA ending exactly at buffer boundary */
        final String CDATA_START = "<![CDATA[";
        final String CDATA_END = "]]>";
        
        // Create CDATA content ending at buffer boundary
        int contentLength = TEST_BUFFER_SIZE - CDATA_START.length() - 1;
        String content = generateRepeatedString('x', contentLength);
        String html = CDATA_START + content + CDATA_END;

        Document doc = new Parser(new HtmlTreeBuilder()).parseInput(html, "");
        CDataNode cdata = (CDataNode) doc.body().childNode(0);
        
        assertEquals(content, cdata.text());
    }

    @Test 
    void testHandlesCp1252EntitySubstitution() {
        /* Tests Windows-1252 character entity replacement */
        assertEquals("\u20ac", Jsoup.parse("&#0128;").text());  // Euro
        assertEquals("\u201a", Jsoup.parse("&#0130;").text());  // Single quote
        assertEquals("\u20ac", Jsoup.parse("&#x80;").text());   // Hex euro
    }

    @Test 
    void testTracksErrorsForCp1252Entities() {
        /* Tests error tracking for invalid CP1252 entities */
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        
        parser.parseInput("<html>&#0128;</html>", "");
        assertEquals(1, parser.getErrors().size(), "Should log one error");
    }

    @Test 
    void testValidatesCp1252SubstitutionTable() {
        /* Validates the Windows-1252 substitution table */
        for (int i = 0; i < Tokeniser.win1252Extensions.length; i++) {
            byte[] byteData = {(byte) (i + Tokeniser.win1252ExtensionsStart)};
            String actual = new String(byteData, Charset.forName("Windows-1252"));
            
            if (actual.charAt(0) == '\ufffd')  // Skip invalid chars
                continue;
            
            char expected = (char) Tokeniser.win1252Extensions[i];
            assertEquals(expected, actual.charAt(0), "Mismatch at index: " + i);
        }
    }

    // ========================================================================
    // Tests for tokenization utilities
    // ========================================================================

    @Test 
    void testTokenDataToString() {
        /* Tests TokenData string representation */
        TokenData data = new TokenData();
        
        data.set("abc");
        assertEquals("abc", data.toString());
        
        data.append("def");
        assertEquals("abcdef", data.toString());
    }

    // ========================================================================
    // Helper utilities
    // ========================================================================

    /**
     * Generates a string by repeating a character to reach minimum length
     * @param c Character to repeat
     * @param minLength Minimum length of resulting string
     */
    private String generateRepeatedString(char c, int minLength) {
        char[] chars = new char[minLength];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    /**
     * Generates a string by repeating a pattern to reach minimum length
     * @param pattern String pattern to repeat
     * @param minLength Minimum length of resulting string
     */
    private String generateRepeatedString(String pattern, int minLength) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < minLength) {
            sb.append(pattern);
        }
        return sb.toString();
    }
}