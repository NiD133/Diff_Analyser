package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.DOMException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Test suite for the W3CDom helper class, focusing on conversion behavior.
 */
public class W3CDomTest {

    /**
     * Verifies that attempting to convert a Jsoup element with an invalid W3C attribute name
     * throws a DOMException. Attribute names in XML and W3C DOM have a restricted character set
     * and cannot contain characters like spaces, ampersands, or brackets.
     */
    @Test(expected = DOMException.class)
    public void conversionFailsForElementWithInvalidAttributeName() throws ParserConfigurationException {
        // Arrange:
        // 1. Create a destination W3C document to host the converted nodes.
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        // 2. Create a Jsoup element with an attribute name that is invalid for W3C DOM.
        // The name contains spaces and an ampersand, which are not permitted.
        Document jsoupDoc = Parser.parse("<p invalid & name='value'>Test</p>");
        Element elementWithInvalidAttr = jsoupDoc.selectFirst("p");

        // Act:
        // Attempt to traverse the Jsoup element. The W3CBuilder will try to create a
        // corresponding W3C element with the invalid attribute, which must fail.
        w3cBuilder.traverse(elementWithInvalidAttr);

        // Assert: A DOMException is expected, as declared in the @Test annotation.
    }
}