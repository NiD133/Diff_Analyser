package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link DocumentType} to verify its outer HTML representation
 * after being parsed by both HTML and XML parsers.
 * This focuses on the "round-trip" capability: parsing a doctype string
 * and ensuring the generated outer HTML is correct.
 */
public class DocumentTypeTest {

    /**
     * Parses an input string using the default HTML parser, extracts the first child node
     * (expected to be a DocumentType), and returns its outer HTML.
     *
     * @param doctypeString The string containing the doctype declaration.
     * @return The outer HTML of the parsed DocumentType node.
     */
    private String getOuterHtmlAfterHtmlParse(String doctypeString) {
        Document doc = Jsoup.parse(doctypeString);
        DocumentType doctype = (DocumentType) doc.childNode(0);
        return doctype.outerHtml();
    }

    /**
     * Parses an input string using the XML parser, extracts the first child node
     * (expected to be a DocumentType), and returns its outer HTML.
     *
     * @param doctypeString The string containing the doctype declaration.
     * @return The outer HTML of the parsed DocumentType node.
     */
    private String getOuterHtmlAfterXmlParse(String doctypeString) {
        Document doc = Jsoup.parse(doctypeString, "", Parser.xmlParser());
        DocumentType doctype = (DocumentType) doc.childNode(0);
        return doctype.outerHtml();
    }

    @Test
    public void html5DoctypeIsLowercasedByHtmlParser() {
        String originalDoctype = "<!DOCTYPE html>";
        String expectedOutput = "<!doctype html>"; // The HTML parser normalizes the keyword to lowercase.

        String actualOutput = getOuterHtmlAfterHtmlParse(originalDoctype);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void html5DoctypeIsPreservedByXmlParser() {
        String originalDoctype = "<!DOCTYPE html>";
        // The XML parser should preserve the original case.
        String actualOutput = getOuterHtmlAfterXmlParse(originalDoctype);

        assertEquals(originalDoctype, actualOutput);
    }

    @ParameterizedTest(name = "Run {index}: doctype={0}")
    @ValueSource(strings = {
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
        "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">",
        "<!DOCTYPE html SYSTEM \"about:legacy-compat\">"
    })
    public void complexDoctypesArePreservedByHtmlParser(String originalDoctype) {
        // The HTML parser should preserve the full string for public, system, and legacy doctypes.
        String actualOutput = getOuterHtmlAfterHtmlParse(originalDoctype);
        assertEquals(originalDoctype, actualOutput);
    }

    @ParameterizedTest(name = "Run {index}: doctype={0}")
    @ValueSource(strings = {
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
        "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">",
        "<!DOCTYPE html SYSTEM \"about:legacy-compat\">"
    })
    public void complexDoctypesArePreservedByXmlParser(String originalDoctype) {
        // The XML parser should also preserve the full string for these doctypes.
        String actualOutput = getOuterHtmlAfterXmlParse(originalDoctype);
        assertEquals(originalDoctype, actualOutput);
    }
}