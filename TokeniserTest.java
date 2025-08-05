package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tokeniser tests")
public class TokeniserTest {

    @DisplayName("should parse attribute values that span the buffer boundary")
    @ParameterizedTest(name = "Quote type: {0}")
    @ValueSource(strings = {"\"", "'", ""})
    void attributeValueSpanningBufferIsParsedCorrectly(String quote) {
        // Arrange
        // Creates an attribute value that forces the tokeniser to buffer-up mid-read.
        String preamble = "<img src=" + quote;
        String tail = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        int charsToFillBuffer = BufferSize - preamble.length();
        String filling = "a".repeat(charsToFillBuffer);
        String charAcrossBoundary = "X";

        String expectedSrc = filling + charAcrossBoundary + tail;
        String html = preamble + expectedSrc + quote + ">";

        // Act
        Document doc = Jsoup.parse(html);
        String actualSrc = doc.selectFirst("img").attr("src");

        // Assert
        assertEquals(expectedSrc, actualSrc);
    }

    private static Stream<Arguments> largeTokenTestCases() {
        // Each argument provides a test case for a token type that is larger than the parse buffer.
        // Arguments: testName, repeatedText, htmlTemplate, verifier
        return Stream.of(
            Arguments.of(
                "Tag Name", "LargeTagName", "<%s>One</%s>",
                (BiConsumer<Document, String>) (doc, largeString) -> {
                    Elements els = doc.select(largeString);
                    assertEquals(1, els.size(), "Should find element by large tag name");
                    Element el = els.first();
                    assertNotNull(el);
                    assertEquals("One", el.text());
                    assertEquals(largeString, el.tagName());
                }
            ),
            Arguments.of(
                "Attribute Name", "LargeAttributeName", "<p %s=foo>One</p>",
                (BiConsumer<Document, String>) (doc, largeString) -> {
                    Elements els = doc.getElementsByAttribute(largeString);
                    assertEquals(1, els.size(), "Should find element by large attribute name");
                    Element el = els.first();
                    assertNotNull(el);
                    assertEquals("One", el.text());
                    Attribute attribute = el.attributes().asList().get(0);
                    assertEquals(largeString.toLowerCase(), attribute.getKey());
                    assertEquals("foo", attribute.getValue());
                }
            ),
            Arguments.of(
                "Text", "A Large Amount of Text ", "<p>%s</p>",
                (BiConsumer<Document, String>) (doc, largeString) -> {
                    Element el = doc.selectFirst("p");
                    assertNotNull(el);
                    assertEquals(largeString, el.text());
                }
            ),
            Arguments.of(
                "Comment", "Quite a comment ", "<p><!-- %s --></p>",
                (BiConsumer<Document, String>) (doc, largeString) -> {
                    Element el = doc.selectFirst("p");
                    assertNotNull(el);
                    Comment child = (Comment) el.childNode(0);
                    assertEquals(" " + largeString + " ", child.getData());
                }
            ),
            Arguments.of(
                "Bogus Comment", "blah blah blah blah ", "<html><body><!%s></body></html>",
                (BiConsumer<Document, String>) (doc, largeString) -> {
                    Comment comment = (Comment) doc.body().childNode(0);
                    assertEquals(largeString, comment.getData());
                }
            ),
            Arguments.of(
                "CDATA", "Quite a lot of CDATA <><><><> ", "<p><
![CDATA[%s]]></p>",
                (BiConsumer<Document, String>)
 (doc, largeString) -> {
                    Element el = doc.selectFirst("p");
                    assertNotNull(el);
                    CDataNode child = (CDataNode) el.childNode(0);
                    assertEquals(largeString, el.text());
                    assertEquals(largeString, child.getWholeText());
                }
            ),
            Arguments.of(
                "Title", "Quite a long title ", "<title>%s</title>",
                (BiConsumer<Document, String>) (doc, largeString) -> {
                    assertEquals(largeString, doc.title());
                    Element el = doc.selectFirst("title");
                    assertNotNull(el);
                    assertEquals(largeString, el.text());
                }
            )
        );
    }

    @DisplayName("should parse tokens larger than the buffer")
    @ParameterizedTest(name = "Token type: {0}")
    @MethodSource("largeTokenTestCases")
    void parsingTokenLargerThanBufferIsSuccessful(String testName, String repeatedText, String htmlTemplate, BiConsumer<Document, String> verifier) {
        // Arrange
        // Create a token body that is larger than the default buffer size to test buffer-up logic.
        StringBuilder sb = new StringBuilder(BufferSize * 2);
        do {
            sb.append(repeatedText);
        } while (sb.length() < BufferSize);
        String largeTokenContent = sb.toString();

        String html = String.format(htmlTemplate, largeTokenContent, largeTokenContent);

        // Act
        // The "Tag Name" test requires case preservation, others use the default parser.
        Document doc;
        if ("Tag Name".equals(testName)) {
            doc = Parser.htmlParser().settings(ParseSettings.preserveCase).parseInput(html, "");
        } else {
            doc = Jsoup.parse(html);
        }

        // Assert
        verifier.accept(doc, largeTokenContent);
    }

    @Test
    @DisplayName("should parse CDATA that ends exactly at the buffer boundary")
    public void parsingCdataEndingExactlyAtBufferBoundaryIsSuccessful() {
        // Arrange
        // This test targets a specific off-by-one bug. The length is chosen to place the end of the
        // CDATA section ('_]]>') exactly at the boundary of the character buffer, which previously
        // caused a parsing error.
        String cdataStart = "<
![CDATA[";
        String cdataEnd = "]]>";
        int cdataContentsLength = BufferSize - cdataStart.length()
 - 1;
        String cdataContents = "x".repeat(cdataContentsLength);
        String html = cdataStart + cdataContents + cdataEnd;

        // Act
        Document doc = Jsoup.parse(html);
        CDataNode cdataNode = (CDataNode) doc.body().childNode(0);

        // Assert
        assertEquals(cdataContents, cdataNode.getWholeText());
    }

    @Test
    @DisplayName("should parse numeric entities that map to Windows-1252 characters")
    public void parsesNumericEntitiesThatMapToWindows1252Chars() {
        assertEquals("\u20ac", Jsoup.parse("&#0128;").text()); // euro
        assertEquals("\u201a", Jsoup.parse("&#0130;").text()); // single low-9 quotation mark
        assertEquals("\u20ac", Jsoup.parse("&#x80;").text());  // euro (hex)
    }

    @Test
    @DisplayName("should add a parse error when parsing Windows-1252 numeric entities")
    public void parsingWindows1252NumericEntitiesAddsParseError() {
        // Arrange
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);

        // Act
        parser.parseInput("<html><body>&#0128;</body></html>", "");

        // Assert
        assertEquals(1, parser.getErrors().size());
    }

    @Test
    @DisplayName("should have an internal Win-1252 extension table that matches Java's charset")
    public void internalWin1252ExtensionsTableIsValid() {
        // This test verifies that our hard-coded win1252 extension table is correct,
        // by comparing it against the platform's built-in Windows-1252 charset.
        for (int i = 0; i < Tokeniser.win1252Extensions.length; i++) {
            String s = new String(new byte[]{(byte) (i + Tokeniser.win1252ExtensionsStart)}, Charset.forName("Windows-1252"));
            if (s.charAt(0) == '\ufffd') continue; // skip undefined characters

            assertEquals(s.charAt(0), Tokeniser.win1252Extensions[i], "Mismatch at index " + i);
        }
    }

    @Test
    @DisplayName("should correctly convert TokenData to string")
    void tokenDataToStringIsCorrect() {
        // Arrange
        Token.TokenData data = new Token.TokenData();

        // Act & Assert
        assertEquals("", data.toString());

        data.set("abc");
        assertEquals("abc", data.toString());

        data.append("def");
        assertEquals("abcdef", data.toString());
    }
}