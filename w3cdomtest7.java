package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;
import static org.jsoup.TextUtil.normalizeSpaces;
import static org.jsoup.nodes.Document.OutputSettings.Syntax.xml;
import static org.junit.jupiter.api.Assertions.*;

public class W3CDomTestTest7 {

    private static Document parseXml(String xml, boolean nameSpaceAware) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(nameSpaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver((publicId, systemId) -> {
                if (systemId.contains("about:legacy-compat")) {
                    // <!doctype html>
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

    private NodeList xpath(Document w3cDoc, String query) throws XPathExpressionException {
        XPathExpression xpath = XPathFactory.newInstance().newXPath().compile(query);
        return ((NodeList) xpath.evaluate(w3cDoc, XPathConstants.NODE));
    }

    private String output(String in, boolean modeHtml) {
        org.jsoup.nodes.Document jdoc = Jsoup.parse(in);
        Document w3c = W3CDom.convert(jdoc);
        Map<String, String> properties = modeHtml ? W3CDom.OutputHtml() : W3CDom.OutputXml();
        return normalizeSpaces(W3CDom.asString(w3c, properties));
    }

    private void assertEqualsIgnoreCase(String want, String have) {
        assertEquals(want.toLowerCase(Locale.ROOT), have.toLowerCase(Locale.ROOT));
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    void doesNotExpandEntities(Parser parser) {
        // Tests that the billion laughs attack doesn't expand entities; also for XXE
        // Not impacted because jsoup doesn't parse the entities within the doctype, and so won't get to the w3c.
        // Added to confirm, and catch if that ever changes
        String billionLaughs = "<?xml version=\"1.0\"?>\n" + "<!DOCTYPE lolz [\n" + " <!ENTITY lol \"lol\">\n" + " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" + "]>\n" + "<html><body><p>&lol1;</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughs, parser);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        // select the p and make sure it's unexpanded
        NodeList p = w3cDoc.getElementsByTagName("p");
        assertEquals(1, p.getLength());
        assertEquals("&lol1;", p.item(0).getTextContent());
        // Check the string
        String string = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(string.contains("lololol"));
        assertTrue(string.contains("&amp;lol1;"));
    }

    private static Stream<Arguments> parserProvider() {
        return Stream.of(Arguments.of(Parser.htmlParser()), Arguments.of(Parser.xmlParser()));
    }

    @Test
    void handlesHtmlElsWithLt() {
        // In HTML, elements can be named "foo<bar" (<foo<bar>). Test that we can convert to W3C, that we can HTML parse our HTML serial, XML parse our XML serial, and W3C XML parse the XML serial and the W3C serial
        // And similarly attributes may have "<" in their name
        // https://github.com/jhy/jsoup/issues/2259
        String input = "<foo<bar attr<name=\"123\"><b>Text</b></foo<bar>";
        // rewrites < to _ in el and attr
        String xmlExpect = "<foo_bar attr_name=\"123\"><b>Text</b></foo_bar>";
        // html round trips
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(input);
        String htmlSerial = htmlDoc.body().html();
        // same as input
        assertEquals(input, normalizeSpaces(htmlSerial));
        Element htmlRound = Jsoup.parse(htmlSerial).body();
        assertTrue(htmlDoc.body().hasSameValue(htmlRound));
        // xml round trips
        htmlDoc.outputSettings().syntax(xml);
        String asXml = htmlDoc.body().html();
        // <foo<bar> -> <foo_bar>
        assertEquals(xmlExpect, normalizeSpaces(asXml));
        org.jsoup.nodes.Document xmlDoc = Jsoup.parse(asXml);
        String xmlSerial = xmlDoc.body().html();
        // same as xmlExpect
        assertEquals(xmlExpect, normalizeSpaces(xmlSerial));
        Element xmlRound = Jsoup.parse(xmlSerial).body();
        assertTrue(xmlDoc.body().hasSameValue(xmlRound));
        // Can W3C parse that XML
        Document w3cXml = parseXml(asXml, true);
        NodeList w3cXmlNodes = w3cXml.getElementsByTagName("foo_bar");
        assertEquals(1, w3cXmlNodes.getLength());
        assertEquals("123", w3cXmlNodes.item(0).getAttributes().getNamedItem("attr_name").getTextContent());
        // Can convert to W3C
        Document w3cDoc = W3CDom.convert(htmlDoc);
        NodeList w3cNodes = w3cDoc.getElementsByTagName("foo_bar");
        assertEquals(1, w3cNodes.getLength());
        assertEquals("123", w3cNodes.item(0).getAttributes().getNamedItem("attr_name").getTextContent());
    }
}
