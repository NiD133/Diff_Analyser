package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.integration.ParseTest;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.xml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link W3CDom}.
 * Focuses on security aspects and compatibility with standard W3C parsers.
 */
public class W3CDomTest {

    /**
     * Provides HTML and XML parsers for parameterized tests.
     */
    private static Stream<Arguments> parserProvider() {
        return Stream.of(Arguments.of(Parser.htmlParser()), Arguments.of(Parser.xmlParser()));
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    void fromJsoupIsSafeFromBillionLaughsEntityExpansionAttack(Parser parser) {
        // Arrange
        // A "Billion Laughs" XML document is a form of denial-of-service (DoS) attack
        // that uses heavily nested entities. This test ensures that when converting
        // a Jsoup-parsed document to a W3C DOM, these entities are not expanded.
        // This is because Jsoup itself does not parse or expand entities within the DTD.
        String billionLaughsXml = "<?xml version=\"1.0\"?>\n"
            + "<!DOCTYPE lolz [\n"
            + " <!ENTITY lol \"lol\">\n"
            + " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n"
            + "]>\n"
            + "<html><body><p>&lol1;</p></body></html>";

        // Act
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(w3cDoc);

        // 1. Check the W3C DOM structure: the entity should be present as text, not expanded.
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        Node pElement = pElements.item(0);
        assertEquals("&lol1;", pElement.getTextContent());

        // 2. Check the serialized string output: it should not contain the expanded "lol"s.
        // The entity reference should be correctly escaped.
        String outputXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(outputXml.contains("lololol"));
        assertTrue(outputXml.contains("&amp;lol1;"));
    }

    @Test
    public void jsoupXmlOutputForScriptTagIsCompatibleWithW3cParser() throws Exception {
        // This test verifies that Jsoup's XML output for a <script> tag is compatible
        // with a standard W3C DOM parser.
        // Note: this test does not use W3CDom.convert, but rather Jsoup's standard XML serialization.

        // Arrange
        String html = "<p><script>1 && 2</script></p>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.outputSettings().syntax(xml); // Set output to XML

        // When outputting as XML, Jsoup wraps script content in a CDATA section to preserve its content.
        String xmlFragment = jsoupDoc.body().html();

        // Act
        // Parse the generated XML fragment using a standard Java XML parser.
        // This will throw an exception if the XML is not well-formed.
        Document w3cDoc = parseXml(xmlFragment, false);

        // Assert
        // Navigate the W3C DOM to find the script element and verify its content.
        org.w3c.dom.Element pElement = w3cDoc.getDocumentElement();
        assertEquals("p", pElement.getTagName());

        NodeList scriptList = pElement.getElementsByTagName("script");
        assertEquals(1, scriptList.getLength());
        org.w3c.dom.Element scriptElement = (org.w3c.dom.Element) scriptList.item(0);

        // The full text content within the <script> tag should be preserved,
        // including the protective comments Jsoup adds around the CDATA section.
        String expectedScriptContent = "//\n" + "1 && 2\n" + "//";
        assertEquals(expectedScriptContent, scriptElement.getTextContent().trim());
    }

    /**
     * Helper to parse an XML string into a W3C Document for testing purposes.
     */
    private Document parseXml(String xml, boolean nameSpaceAware) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(nameSpaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Suppress resolution of external entities, e.g. <!doctype html>
            builder.setEntityResolver((publicId, systemId) -> {
                if (systemId.contains("about:legacy-compat")) {
                    return new InputSource(new StringReader(""));
                } else {
                    return null;
                }
            });
            Document dom = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            dom.normalizeDocument();
            return dom;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}