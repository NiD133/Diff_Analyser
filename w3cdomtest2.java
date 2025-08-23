package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.integration.ParseTest;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("W3C DOM Conversion")
public class W3CDomTest {

    private static Document w3cDoc;
    private static SimpleNamespaceContext nsContext;

    /**
     * A simple NamespaceContext for resolving prefixes in XPath queries.
     */
    private static class SimpleNamespaceContext implements NamespaceContext {
        private final Map<String, String> namespaces = new HashMap<>();

        public void add(String prefix, String uri) {
            namespaces.put(prefix, uri);
        }

        @Override
        public String getNamespaceURI(String prefix) {
            return namespaces.get(prefix);
        }

        @Override
        public String getPrefix(String namespaceURI) { return null; /* Not needed for this test */ }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) { return null; /* Not needed for this test */ }
    }

    /**
     * Parses the test XHTML file and converts it to a W3C Document once for all namespace tests.
     */
    @BeforeAll
    public static void setUp() throws IOException {
        File in = ParseTest.getFile("/htmltests/namespaces.xhtml");
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(in, "UTF-8", "", Parser.xmlParser());

        W3CDom w3cDomConverter = new W3CDom();
        w3cDoc = w3cDomConverter.fromJsoup(jsoupDoc);

        nsContext = new SimpleNamespaceContext();
        nsContext.add("xhtml", "http://www.w3.org/1999/xhtml");
        nsContext.add("epub", "http://www.idpf.org/2007/ops");
        nsContext.add("x", "urn:test");
        nsContext.add("svg", "http://www.w3.org/2000/svg");
    }

    private static Stream<Arguments> parserProvider() {
        return Stream.of(Arguments.of(Parser.htmlParser()), Arguments.of(Parser.xmlParser()));
    }

    @DisplayName("Should not expand XML entities to prevent billion laughs attack")
    @ParameterizedTest(name = "with {0}")
    @MethodSource("parserProvider")
    void doesNotExpandEntities(Parser parser) {
        // Tests that the billion laughs attack doesn't expand entities.
        // Jsoup doesn't parse DTD entities, so it's not vulnerable. This test confirms that behavior.
        String billionLaughsXml =
            "<?xml version=\"1.0\"?>" +
            "<!DOCTYPE lolz [" +
            " <!ENTITY lol \"lol\">" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">" +
            "]>" +
            "<html><body><p>&lol1;</p></body></html>";

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);

        // Assert that the entity is not expanded in the DOM
        Node pElement = w3cDoc.getElementsByTagName("p").item(0);
        assertEquals(1, w3cDoc.getElementsByTagName("p").getLength());
        assertEquals("&lol1;", pElement.getTextContent());

        // Assert that the entity is not expanded when converted back to a string
        String out = W3CDom.asString(w3cDoc);
        assertFalse(out.contains("lololol"));
        assertTrue(out.contains("&amp;lol1;")); // Note the XML escaping
    }

    @Test
    @DisplayName("Should preserve namespaces during conversion")
    public void namespacePreservation() throws XPathExpressionException {
        // Root element
        Node html = getNodeByXpath("/xhtml:html");
        assertEquals("http://www.w3.org/1999/xhtml", html.getNamespaceURI());
        assertEquals("html", html.getLocalName());
        assertEquals("html", html.getNodeName());

        // Element with inherited default namespace
        Node head = getNodeByXpath("/xhtml:html/xhtml:head");
        assertEquals("http://www.w3.org/1999/xhtml", head.getNamespaceURI());
        assertEquals("head", head.getLocalName());
        assertEquals("head", head.getNodeName());

        // Element with a prefixed namespace
        Node epubTitle = getNodeByXpath("//epub:title");
        assertEquals("Check", epubTitle.getTextContent());
        assertEquals("http://www.idpf.org/2007/ops", epubTitle.getNamespaceURI());
        assertEquals("title", epubTitle.getLocalName());
        assertEquals("epub:title", epubTitle.getNodeName());

        // Another prefixed namespace
        Node xSection = getNodeByXpath("//x:section");
        assertEquals("urn:test", xSection.getNamespaceURI());
        assertEquals("section", xSection.getLocalName());
        assertEquals("x:section", xSection.getNodeName());

        // SVG namespace, which is not the last declared one (tests issue #977)
        Node svg = getNodeByXpath("//svg:svg");
        assertEquals("http://www.w3.org/2000/svg", svg.getNamespaceURI());
        assertEquals("svg", svg.getLocalName());
        assertEquals("svg", svg.getNodeName());

        // Child of SVG element inherits namespace
        Node path = getNodeByXpath("//svg:path");
        assertEquals("http://www.w3.org/2000/svg", path.getNamespaceURI());
        assertEquals("path", path.getLocalName());
        assertEquals("path", path.getNodeName());

        // Element with a non-namespaced attribute
        Node clip = getNodeByXpath("//svg:path/clip");
        assertEquals("http://example.com/clip", clip.getNamespaceURI());
        assertEquals("clip", clip.getLocalName());
        assertEquals("clip", clip.getNodeName());
        assertEquals("456", clip.getTextContent());

        // Element that reverts to the default XHTML namespace
        Node picture = getNodeByXpath("//xhtml:picture");
        assertEquals("http://www.w3.org/1999/xhtml", picture.getNamespaceURI());
        assertEquals("picture", picture.getLocalName());
        assertEquals("picture", picture.getNodeName());

        // Child of the above element
        Node img = getNodeByXpath("//xhtml:picture/xhtml:img");
        assertEquals("http://www.w3.org/1999/xhtml", img.getNamespaceURI());
        assertEquals("img", img.getLocalName());
        assertEquals("img", img.getNodeName());
    }

    /**
     * Selects a single W3C Node from the document using an XPath query.
     * @param xpathQuery the XPath query to execute
     * @return the first matching Node
     * @throws XPathExpressionException if the query is invalid
     */
    private Node getNodeByXpath(String xpathQuery) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        xpath.setNamespaceContext(nsContext);
        Node node = (Node) xpath.evaluate(xpathQuery, w3cDoc, XPathConstants.NODE);
        assertNotNull(node, "XPath query \"" + xpathQuery + "\" did not find a node.");
        return node;
    }
}