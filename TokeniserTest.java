package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Tokeniser class, focusing on edge cases involving buffer boundaries,
 * large content handling, and character encoding scenarios.
 */
public class TokeniserTest {

    // Test data constants
    private static final String[] QUOTE_TYPES = {"\"", "'", ""}; // double, single, unquoted
    private static final String REPEATED_CONTENT = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
    private static final char BUFFER_BOUNDARY_MARKER = 'X';

    /**
     * Tests that attribute values are correctly parsed when they span across the character buffer boundary.
     * This addresses issue #967 where content crossing buffer boundaries could be lost or corrupted.
     * 
     * The test verifies all three attribute quoting styles: double-quoted, single-quoted, and unquoted.
     */
    @Test
    public void shouldParseAttributeValuesSpanningBufferBoundary() {
        for (String quote : QUOTE_TYPES) {
            // Arrange: Create HTML with attribute value that crosses buffer boundary
            String htmlWithLargeAttribute = createHtmlWithAttributeSpanningBuffer(quote);
            
            // Act: Parse the HTML
            Document document = Jsoup.parse(htmlWithLargeAttribute);
            String actualSrcValue = document.select("img").attr("src");
            
            // Assert: Verify content crossing buffer boundary is preserved
            String testContext = "quote type: " + (quote.isEmpty() ? "unquoted" : quote);
            assertTrue(actualSrcValue.contains(String.valueOf(BUFFER_BOUNDARY_MARKER)), 
                      "Buffer boundary marker should be preserved for " + testContext);
            assertTrue(actualSrcValue.contains(REPEATED_CONTENT), 
                      "Tail content should be preserved for " + testContext);
        }
    }

    /**
     * Tests parsing of extremely large tag names that exceed the buffer size.
     * Verifies that the parser can handle edge cases with unusually long tag names.
     */
    @Test 
    public void shouldHandleTagNamesLargerThanBuffer() {
        // Arrange: Create a tag name larger than buffer size
        String largeTagName = createStringLargerThanBuffer("LargeTagName");
        String htmlWithLargeTag = String.format("<%s>One</%s>", largeTagName, largeTagName);
        
        // Act: Parse with case preservation to maintain exact tag name
        Document document = Parser.htmlParser()
                                 .settings(ParseSettings.preserveCase)
                                 .parseInput(htmlWithLargeTag, "");
        
        // Assert: Verify the large tag is correctly parsed
        Elements matchingElements = document.select(largeTagName);
        assertEquals(1, matchingElements.size(), "Should find exactly one element with large tag name");
        
        Element element = matchingElements.first();
        assertNotNull(element, "Element should not be null");
        assertEquals("One", element.text(), "Element text should be preserved");
        assertEquals(largeTagName, element.tagName(), "Tag name should be preserved exactly");
    }

    /**
     * Tests parsing of extremely large attribute names that exceed the buffer size.
     */
    @Test 
    public void shouldHandleAttributeNamesLargerThanBuffer() {
        // Arrange: Create an attribute name larger than buffer size
        String largeAttributeName = createStringLargerThanBuffer("LargAttributeName");
        String htmlWithLargeAttribute = String.format("<p %s=foo>One</p>", largeAttributeName);
        
        // Act: Parse the HTML
        Document document = Jsoup.parse(htmlWithLargeAttribute);
        
        // Assert: Verify the large attribute is correctly parsed
        Elements elementsWithAttribute = document.getElementsByAttribute(largeAttributeName);
        assertEquals(1, elementsWithAttribute.size(), "Should find element with large attribute");
        
        Element element = elementsWithAttribute.first();
        assertNotNull(element, "Element should not be null");
        assertEquals("One", element.text(), "Element text should be preserved");
        
        Attribute attribute = element.attributes().asList().get(0);
        assertEquals(largeAttributeName.toLowerCase(), attribute.getKey(), 
                    "Attribute name should be preserved (lowercased by HTML parser)");
        assertEquals("foo", attribute.getValue(), "Attribute value should be preserved");
    }

    /**
     * Tests parsing of text content larger than the buffer size.
     */
    @Test 
    public void shouldHandleTextContentLargerThanBuffer() {
        // Arrange: Create text content larger than buffer size
        String largeTextContent = createStringLargerThanBuffer("A Large Amount of Text");
        String htmlWithLargeText = String.format("<p>%s</p>", largeTextContent);
        
        // Act & Assert: Verify large text content is preserved
        verifyElementTextContent(htmlWithLargeText, "p", largeTextContent);
    }

    /**
     * Tests parsing of HTML comments larger than the buffer size.
     */
    @Test 
    public void shouldHandleCommentsLargerThanBuffer() {
        // Arrange: Create comment content larger than buffer size
        String largeCommentContent = createStringLargerThanBuffer("Quite a comment ");
        String htmlWithLargeComment = String.format("<p><!-- %s --></p>", largeCommentContent);
        
        // Act: Parse the HTML
        Document document = Jsoup.parse(htmlWithLargeComment);
        Element paragraphElement = document.select("p").first();
        
        // Assert: Verify comment content is preserved with surrounding spaces
        assertNotNull(paragraphElement, "Paragraph element should exist");
        Comment commentNode = (Comment) paragraphElement.childNode(0);
        String expectedCommentData = " " + largeCommentContent + " ";
        assertEquals(expectedCommentData, commentNode.getData(), 
                    "Comment data should preserve surrounding spaces");
    }

    /**
     * Tests parsing of CDATA sections larger than the buffer size.
     */
    @Test 
    public void shouldHandleCdataLargerThanBuffer() {
        // Arrange: Create CDATA content larger than buffer size
        String largeCdataContent = createStringLargerThanBuffer("Quite a lot of CDATA <><><><>");
        String htmlWithLargeCdata = String.format("<p><![CDATA[%s]]></p>", largeCdataContent);
        
        // Act: Parse the HTML
        Document document = Jsoup.parse(htmlWithLargeCdata);
        Element paragraphElement = document.select("p").first();
        
        // Assert: Verify CDATA content is preserved
        assertNotNull(paragraphElement, "Paragraph element should exist");
        TextNode textNode = (TextNode) paragraphElement.childNode(0);
        assertEquals(largeCdataContent, paragraphElement.text(), "Element text should match CDATA content");
        assertEquals(largeCdataContent, textNode.getWholeText(), "Text node should match CDATA content");
    }

    /**
     * Tests parsing of title elements with content larger than the buffer size.
     */
    @Test 
    public void shouldHandleTitleContentLargerThanBuffer() {
        // Arrange: Create title content larger than buffer size
        String largeTitleContent = createStringLargerThanBuffer("Quite a long title");
        String htmlWithLargeTitle = String.format("<title>%s</title>", largeTitleContent);
        
        // Act: Parse the HTML
        Document document = Jsoup.parse(htmlWithLargeTitle);
        Element titleElement = document.select("title").first();
        
        // Assert: Verify title content is preserved in multiple ways
        assertNotNull(titleElement, "Title element should exist");
        TextNode textNode = (TextNode) titleElement.childNode(0);
        assertEquals(largeTitleContent, titleElement.text(), "Title element text should be preserved");
        assertEquals(largeTitleContent, textNode.getWholeText(), "Title text node should be preserved");
        assertEquals(largeTitleContent, document.title(), "Document title should be preserved");
    }

    /**
     * Tests that Windows-1252 character entities are correctly converted to Unicode.
     */
    @Test 
    public void shouldConvertWindows1252EntitiesToUnicode() {
        // Test decimal entity references
        assertEquals("\u20ac", Jsoup.parse("&#0128;").text(), "Decimal entity &#0128; should convert to Euro symbol");
        assertEquals("\u201a", Jsoup.parse("&#0130;").text(), "Decimal entity &#0130; should convert to single low-9 quotation mark");
        
        // Test hexadecimal entity references
        assertEquals("\u20ac", Jsoup.parse("&#x80;").text(), "Hex entity &#x80; should convert to Euro symbol");
    }

    /**
     * Tests that Windows-1252 character entities produce parsing errors when error tracking is enabled.
     */
    @Test 
    public void shouldReportErrorsForWindows1252Entities() {
        // Arrange: Create parser with error tracking enabled
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        
        // Act: Parse HTML with Windows-1252 entity
        String parsedText = parser.parseInput("<html><body>&#0128;</body></html>", "").text();
        
        // Assert: Verify entity is converted but error is reported
        assertEquals("\u20ac", parsedText, "Entity should still be converted despite error");
        assertEquals(1, parser.getErrors().size(), "Should report exactly one parsing error");
    }

    /**
     * Tests the Windows-1252 character substitution table for correctness.
     */
    @Test 
    public void shouldHaveCorrectWindows1252SubstitutionTable() {
        for (int i = 0; i < Tokeniser.win1252Extensions.length; i++) {
            // Get the character from Windows-1252 encoding
            byte[] singleByte = {(byte) (i + Tokeniser.win1252ExtensionsStart)};
            String windows1252Char = new String(singleByte, Charset.forName("Windows-1252"));
            assertEquals(1, windows1252Char.length(), "Should produce single character");

            // Skip illegal characters (represented as replacement character)
            if (windows1252Char.charAt(0) == '\ufffd') { 
                continue; 
            }

            // Verify our substitution table matches Windows-1252 encoding
            assertEquals(windows1252Char.charAt(0), Tokeniser.win1252Extensions[i], 
                        "Substitution table should match Windows-1252 at index: " + i);
        }
    }

    /**
     * Tests parsing of very long bogus comments (malformed comment syntax).
     */
    @Test 
    public void shouldParseVeryLongBogusComments() {
        // Arrange: Create bogus comment larger than buffer size
        String largeBogusCommentContent = createStringLargerThanBuffer("blah blah blah blah ");
        String htmlWithBogusComment = String.format("<html><body><!%s></body></html>", largeBogusCommentContent);
        
        // Act: Parse the HTML
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(htmlWithBogusComment, "");
        
        // Assert: Verify bogus comment is treated as regular comment
        Node commentNode = document.body().childNode(0);
        assertTrue(commentNode instanceof Comment, "Bogus comment should be parsed as Comment node");
        assertEquals(largeBogusCommentContent, ((Comment) commentNode).getData(), 
                    "Bogus comment content should be preserved");
    }

    /**
     * Tests parsing of CDATA sections that end exactly at the buffer boundary.
     * This tests a specific edge case where CDATA ending coincides with buffer limits.
     */
    @Test 
    public void shouldParseCdataEndingAtBufferBoundary() {
        // Arrange: Create CDATA that ends exactly at buffer boundary
        String cdataStart = "<![CDATA[";
        String cdataEnd = "]]>";
        int contentLength = BufferSize - cdataStart.length() - 1; // Position end at buffer boundary
        
        char[] contentArray = new char[contentLength];
        Arrays.fill(contentArray, 'x');
        String cdataContent = new String(contentArray);
        String htmlWithBoundaryCdata = cdataStart + cdataContent + cdataEnd;
        
        // Act: Parse the HTML
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(htmlWithBoundaryCdata, "");
        
        // Assert: Verify CDATA at buffer boundary is correctly parsed
        Node cdataNode = document.body().childNode(0);
        assertTrue(cdataNode instanceof CDataNode, "Should parse as CDATA node");
        assertEquals(cdataContent, ((CDataNode) cdataNode).text(), 
                    "CDATA content should be preserved when ending at buffer boundary");
    }

    /**
     * Tests the TokenData utility class string operations.
     */
    @Test 
    void shouldHandleTokenDataStringOperations() {
        TokenData tokenData = new TokenData();
        
        // Test initial empty state
        assertEquals("", tokenData.toString(), "New TokenData should be empty");
        
        // Test setting data
        tokenData.set("abc");
        assertEquals("abc", tokenData.toString(), "Should return set data");
        
        // Test appending data
        tokenData.append("def");
        assertEquals("abcdef", tokenData.toString(), "Should append to existing data");
    }

    // Helper methods for better code reuse and readability

    /**
     * Creates a string larger than the buffer size by repeating the given pattern.
     */
    private String createStringLargerThanBuffer(String pattern) {
        StringBuilder builder = new StringBuilder(BufferSize);
        do {
            builder.append(pattern);
        } while (builder.length() < BufferSize);
        return builder.toString();
    }

    /**
     * Creates HTML with an img element whose src attribute spans the buffer boundary.
     */
    private String createHtmlWithAttributeSpanningBuffer(String quote) {
        String attributeStart = "<img src=" + quote;
        StringBuilder htmlBuilder = new StringBuilder(attributeStart);

        // Fill up to buffer boundary
        int charactersToFillBuffer = BufferSize - attributeStart.length();
        for (int i = 0; i < charactersToFillBuffer; i++) {
            htmlBuilder.append('a');
        }

        // Add marker character that crosses buffer boundary, then remaining content
        htmlBuilder.append(BUFFER_BOUNDARY_MARKER)
                  .append(REPEATED_CONTENT)
                  .append(quote)
                  .append(">\n");

        return htmlBuilder.toString();
    }

    /**
     * Helper method to verify that an element's text content is correctly preserved.
     */
    private void verifyElementTextContent(String html, String selector, String expectedText) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select(selector);
        assertEquals(1, elements.size(), "Should find exactly one element matching selector: " + selector);
        
        Element element = elements.first();
        assertNotNull(element, "Element should not be null");
        assertEquals(expectedText, element.text(), "Element text should match expected content");
    }
}