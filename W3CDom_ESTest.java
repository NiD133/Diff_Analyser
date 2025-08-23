package org.jsoup.helper;

import org.junit.Test;

import javax.xml.transform.OutputKeys;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class W3CDomTest {

    // --- Configuration: namespaceAware ------------------------------------------------------------

    @Test
    public void namespaceAware_isTrueByDefault_andCanBeToggled() {
        W3CDom dom = new W3CDom();
        assertTrue(dom.namespaceAware());

        W3CDom chained = dom.namespaceAware(false);
        assertSame("namespaceAware(boolean) should return this for chaining", dom, chained);
        assertFalse(dom.namespaceAware());

        dom.namespaceAware(true);
        assertTrue(dom.namespaceAware());
    }

    // --- Conversion: Document and Element ---------------------------------------------------------

    @Test
    public void convertDocument_toW3C_andSerialize() {
        Document jsoupDoc = Parser.parse("Hello", "http://example/");
        org.w3c.dom.Document w3cDoc = W3CDom.convert(jsoupDoc);

        String out = W3CDom.asString(w3cDoc, null);
        assertTrue(out.contains("<html"));
        assertTrue(out.contains("<body>"));
        assertTrue(out.contains("Hello"));
        assertTrue("Should include the XHTML namespace", out.contains("http://www.w3.org/1999/xhtml"));
    }

    @Test
    public void fromJsoup_element_preservesContextAndSourceLink() {
        Document jsoupDoc = Parser.parse("<div id='one'><p class='msg'>Hello</p></div>", "http://example/");
        Element p = jsoupDoc.selectFirst("p");

        W3CDom dom = new W3CDom();
        org.w3c.dom.Document w3cDoc = dom.fromJsoup(p);

        // The W3C context node should correspond to the jsoup element we passed in
        Node context = dom.contextNode(w3cDoc);
        assertNotNull(context);
        assertEquals("p", context.getNodeName());

        // We can select the node back via XPath and retrieve the original jsoup nodes
        NodeList matches = dom.selectXpath("//p", w3cDoc);
        List<Element> originals = dom.sourceNodes(matches, Element.class);
        assertEquals(1, originals.size());
        assertSame("sourceNodes should return the same jsoup element instance", p, originals.get(0));
    }

    // --- XPath selection --------------------------------------------------------------------------

    @Test
    public void selectXpath_onDocument_returnsMatches() {
        Document jsoupDoc = Parser.parse("<h1>Title</h1>", "http://example/");
        W3CDom dom = new W3CDom();
        org.w3c.dom.Document w3cDoc = dom.fromJsoup(jsoupDoc);

        NodeList nodes = dom.selectXpath("//body", w3cDoc);
        assertEquals(1, nodes.getLength());
        assertEquals("body", nodes.item(0).getNodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectXpath_requiresNonNullContextNode() {
        new W3CDom().selectXpath("//body", (Node) null);
    }

    @Test(expected = IllegalStateException.class)
    public void selectXpath_invalidExpression_throwsIllegalStateException() {
        Document jsoupDoc = Parser.parse("<div/>", "http://example/");
        W3CDom dom = new W3CDom();
        org.w3c.dom.Document w3cDoc = dom.fromJsoup(jsoupDoc);

        dom.selectXpath("-", w3cDoc); // malformed XPath
    }

    // --- Serialization ----------------------------------------------------------------------------

    @Test
    public void asString_withAutoDetectedOutput_succeeds() {
        Document jsoupDoc = Parser.parse("<p>Hi</p>", "http://example/");
        W3CDom dom = new W3CDom();
        org.w3c.dom.Document w3cDoc = dom.fromJsoup(jsoupDoc);

        String out = dom.asString(w3cDoc);
        assertTrue(out.contains("<p>Hi</p>"));
    }

    @Test
    public void asString_withExplicitXmlOutput_includesXmlDeclaration() {
        Document jsoupDoc = Parser.parse("<p>Hi</p>", "http://example/");
        org.w3c.dom.Document w3cDoc = W3CDom.convert(jsoupDoc);

        HashMap<String, String> xmlProps = W3CDom.OutputXml();
        String out = W3CDom.asString(w3cDoc, xmlProps);

        assertTrue("Expect XML declaration when using XML output", out.startsWith("<?xml"));
        assertTrue(out.contains("<p>Hi</p>"));
    }

    @Test(expected = NullPointerException.class)
    public void asString_requiresNonNullDocument() {
        W3CDom.asString(null, null);
    }

    // --- Properties helpers -----------------------------------------------------------------------

    @Test
    public void propertiesFromMap_copiesProvidedValues() {
        Map<String, String> map = new HashMap<>();
        map.put(OutputKeys.INDENT, "yes");
        map.put(OutputKeys.METHOD, "xml");

        Properties props = W3CDom.propertiesFromMap(map);
        assertEquals("yes", props.getProperty(OutputKeys.INDENT));
        assertEquals("xml", props.getProperty(OutputKeys.METHOD));
    }

    @Test
    public void outputDefaults_areSensible() {
        HashMap<String, String> html = W3CDom.OutputHtml();
        assertEquals("html", html.get(OutputKeys.METHOD));

        HashMap<String, String> xml = W3CDom.OutputXml();
        assertEquals("xml", xml.get(OutputKeys.METHOD));
    }

    // --- Context node guard ------------------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void contextNode_requiresDocumentCreatedByFromJsoup() {
        new W3CDom().contextNode(null);
    }
}