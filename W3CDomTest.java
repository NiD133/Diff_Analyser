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

public class W3CDomTest {

    // Helper to parse XML with namespace awareness
    private static Document parseXml(String xml, boolean nameSpaceAware) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(nameSpaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver((publicId, systemId) -> {
                if (systemId.contains("about:legacy-compat")) {
                    return new InputSource(new StringReader(""));
                }
                return null;
            });
            Document dom = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            dom.normalizeDocument();
            return dom;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    // Helper for XPath evaluation
    private NodeList xpath(Document w3cDoc, String query) throws XPathExpressionException {
        XPathExpression xpath = XPathFactory.newInstance().newXPath().compile(query);
        return (NodeList) xpath.evaluate(w3cDoc, XPathConstants.NODESET);
    }

    // Helper to normalize and compare strings ignoring case
    private void assertEqualIgnoreCase(String expected, String actual) {
        assertEquals(expected.toLowerCase(Locale.ROOT), actual.toLowerCase(Locale.ROOT));
    }

    // Test group: Basic conversion functionality
    // ==========================================

    @Test
    void convertsSimpleHtmlToW3cDocument() {
        String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --><invalid>What<script>alert('!')";
        org.jsoup.nodes.Document doc = Jsoup.parse(html);

        W3CDom w3c = new W3CDom();
        Document wDoc = w3c.fromJsoup(doc);
        
        // Should not add meta tags automatically
        NodeList meta = wDoc.getElementsByTagName("META");
        assertEquals(0, meta.getLength());
    }

    @Test
    void generatesCorrectXmlStringFromW3cDocument() {
        String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --><invalid>What<script>alert('!')";
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        Document wDoc = new W3CDom().fromJsoup(doc);

        String out = W3CDom.asString(wDoc, W3CDom.OutputXml());
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>W3c</title></head><body><p class=\"one\" id=\"12\">Text</p><!-- comment --><invalid>What<script>alert('!')</script></invalid></body></html>";
        assertEquals(expected, TextUtil.stripNewlines(out));
    }

    @Test
    void survivesRoundTripConversion() {
        String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --><invalid>What<script>alert('!')";
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        Document wDoc = new W3CDom().fromJsoup(doc);
        String xml = W3CDom.asString(wDoc, W3CDom.OutputXml());
        
        Document roundTrip = parseXml(xml, true);
        assertEquals("Text", roundTrip.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    void appliesOutputFormattingProperties() {
        String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --><invalid>What<script>alert('!')";
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        Document wDoc = new W3CDom().fromJsoup(doc);
        
        Map<String, String> properties = W3CDom.OutputXml();
        properties.put(OutputKeys.INDENT, "yes");
        String formatted = W3CDom.asString(wDoc, properties);
        
        // Formatting increases length but content remains the same
        String minified = W3CDom.asString(wDoc, W3CDom.OutputXml());
        assertTrue(formatted.length() > minified.length());
        
        // Compare normalized content ignoring formatting
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>W3c</title></head><body><p class=\"one\" id=\"12\">Text</p><!-- comment --><invalid>What<script>alert('!')</script></invalid></body></html>";
        assertEquals(expected, TextUtil.stripNewlines(formatted));
    }

    // Test group: Namespace handling
    // =============================

    @Test
    void preservesNamespacesInXhtmlDocument() throws IOException {
        File in = ParseTest.getFile("/htmltests/namespaces.xhtml");
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(in, "UTF-8", "", Parser.xmlParser());
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        Node htmlEl = w3cDoc.getChildNodes().item(0);
        assertEquals("http://www.w3.org/1999/xhtml", htmlEl.getNamespaceURI());
        assertEquals("html", htmlEl.getLocalName());

        Node head = htmlEl.getFirstChild().getNextSibling();
        assertEquals("http://www.w3.org/1999/xhtml", head.getNamespaceURI());
        assertEquals("head", head.getLocalName());
    }

    @Test
    void preservesCustomNamespacesInXhtmlDocument() throws IOException {
        File in = ParseTest.getFile("/htmltests/namespaces.xhtml");
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(in, "UTF-8", "", Parser.xmlParser());
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Node htmlEl = w3cDoc.getChildNodes().item(0);

        Node epubTitle = htmlEl.getChildNodes().item(3).getChildNodes().item(3);
        assertEquals("Check", epubTitle.getTextContent());
        assertEquals("http://www.idpf.org/2007/ops", epubTitle.getNamespaceURI());
        assertEquals("title", epubTitle.getLocalName());
        assertEquals("epub:title", epubTitle.getNodeName());

        Node xSection = epubTitle.getNextSibling().getNextSibling();
        assertEquals("urn:test", xSection.getNamespaceURI());
        assertEquals("section", xSection.getLocalName());
        assertEquals("x:section", xSection.getNodeName());
    }

    @Test
    void preservesSvgNamespacesInXhtmlDocument() throws IOException {
        File in = ParseTest.getFile("/htmltests/namespaces.xhtml");
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(in, "UTF-8", "", Parser.xmlParser());
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Node htmlEl = w3cDoc.getChildNodes().item(0);

        Node svg = htmlEl.getChildNodes().item(3).getChildNodes().item(7);
        assertEquals("http://www.w3.org/2000/svg", svg.getNamespaceURI());
        assertEquals("svg", svg.getLocalName());

        Node path = svg.getChildNodes().item(1);
        assertEquals("http://www.w3.org/2000/svg", path.getNamespaceURI());
        assertEquals("path", path.getLocalName());

        Node clip = path.getChildNodes().item(1);
        assertEquals("http://example.com/clip", clip.getNamespaceURI());
        assertEquals("clip", clip.getLocalName());
        assertEquals("456", clip.getTextContent());
    }

    @Test
    void handlesUndeclaredNamespacePrefix() {
        String html = "<fb:like>One</fb:like>";
        Document w3cDoc = new W3CDom().fromJsoup(Jsoup.parse(html));
        Node htmlEl = w3cDoc.getFirstChild();

        Node fb = htmlEl.getFirstChild().getNextSibling().getFirstChild();
        assertEquals("http://www.w3.org/1999/xhtml", fb.getNamespaceURI());
        assertEquals("like", fb.getLocalName());
        assertEquals("fb:like", fb.getNodeName());
    }

    // Test group: Special character handling
    // =====================================

    @Test
    void convertsInvalidAttributeNames() {
        String html = "<html><body style=\"color: red\" \" name\"></body></html>";
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        Element body = doc.selectFirst("body");
        
        assertTrue(body.hasAttr("\""));
        assertTrue(body.hasAttr("name\""));

        Document w3cDoc = W3CDom.convert(doc);
        String xml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body _=\"\" name_=\"\" style=\"color: red\"/></html>", xml);
    }

    @Test
    void maintainsUnicodeAttributesInHtmlOutput() {
        String html = "<!DOCTYPE html><html><body><p hành=\"1\" hình=\"2\">unicode attr names</p></body></html>";
        Document w3cDoc = W3CDom.convert(Jsoup.parse(html));
        String out = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
        
        String expected = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p hành=\"1\" hình=\"2\">unicode attr names</p></body></html>";
        assertEquals(expected, TextUtil.stripNewlines(out));
    }

    @Test
    void normalizesUnicodeAttributesInXmlOutput() {
        String html = "<!DOCTYPE html><html><body><p hành=\"1\" hình=\"2\">unicode attr names coerced</p></body></html>";
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        doc.outputSettings().syntax(xml);  // Force XML output

        Document w3cDoc = W3CDom.convert(doc);
        String out = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
        
        String expected = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p h_nh=\"2\">unicode attr names coerced</p></body></html>";
        assertEquals(expected, TextUtil.stripNewlines(out));
    }

    @Test
    void convertsInvalidTagsToTextNodes() {
        org.jsoup.nodes.Document jsoup = Jsoup.parse("<インセンティブで高収入！>Text <p>More</p>");
        Document w3cDoc = W3CDom.convert(jsoup);
        String xml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body>&lt;インセンティブで高収入！&gt;Text <p>More</p></body></html>", xml);
    }

    @Test
    void handlesElementsAndAttributesWithLtInName() {
        String input = "<foo<bar attr<name=\"123\"><b>Text</b></foo<bar>";
        
        // HTML parsing round-trip
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(input);
        String htmlSerial = htmlDoc.body().html();
        assertEquals(input, normalizeSpaces(htmlSerial));
        
        // XML parsing round-trip
        htmlDoc.outputSettings().syntax(xml);
        String asXml = htmlDoc.body().html();
        assertEquals("<foo_bar attr_name=\"123\"><b>Text</b></foo_bar>", normalizeSpaces(asXml));
        
        // W3C conversion
        Document w3cDoc = W3CDom.convert(htmlDoc);
        NodeList nodes = w3cDoc.getElementsByTagName("foo_bar");
        assertEquals(1, nodes.getLength());
        assertEquals("123", nodes.item(0).getAttributes().getNamedItem("attr_name").getTextContent());
    }

    // Test group: Output configuration
    // ===============================

    @Test
    void convertsToCustomDocument() throws ParserConfigurationException {
        org.jsoup.nodes.Document doc = Jsoup.parse("<html><div></div></html>");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document customDoc = factory.newDocumentBuilder().newDocument();

        new W3CDom().convert(doc, customDoc);
        String html = W3CDom.asString(customDoc, W3CDom.OutputHtml());
        
        assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div></div></body></html>", html);
    }

    @Test
    void outputsHtmlWithoutNamespace() {
        String html = "<p>One</p>";
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        W3CDom w3c = new W3CDom();
        w3c.namespaceAware(false);

        String asHtml = W3CDom.asString(w3c.fromJsoup(doc), W3CDom.OutputHtml());
        String asXml = W3CDom.asString(w3c.fromJsoup(doc), W3CDom.OutputXml());
        
        assertEqualIgnoreCase(
            "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"></head><body><p>one</p></body></html>",
            asHtml
        );
        assertEqualIgnoreCase(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head/><body><p>One</p></body></html>",
            asXml
        );
    }

    // Test group: XPath functionality
    // ===============================

    @Test
    void evaluatesXpathWithNamespaces() throws XPathExpressionException {
        W3CDom w3c = new W3CDom();
        String html = "<html><body><div>hello</div></body></html>";
        
        // Namespace aware by default
        Document dom = w3c.fromJsoup(Jsoup.parse(html));
        NodeList nodeList = xpath(dom, "//*[local-name()=\"body\"]");
        assertEquals("div", nodeList.item(0).getLocalName());

        // Explicit namespace in document
        html = "<html xmlns='http://www.w3.org/1999/xhtml'><body id='One'><div>hello</div></body></html>";
        dom = w3c.fromJsoup(Jsoup.parse(html));
        nodeList = xpath(dom, "//*[local-name()=\"body\"]");
        assertNotNull(nodeList);
        assertEquals(1, nodeList.getLength());
        assertEquals("div", nodeList.item(0).getLocalName());
    }

    @Test
    void evaluatesXpathWithoutNamespaces() throws XPathExpressionException {
        W3CDom w3c = new W3CDom();
        w3c.namespaceAware(false);
        String html = "<html><body><div>hello</div></body></html>";
        
        Document dom = w3c.fromJsoup(Jsoup.parse(html));
        NodeList nodeList = xpath(dom, "//body");
        assertEquals(1, nodeList.getLength());
        assertEquals("div", nodeList.item(0).getLocalName());
    }

    @Test
    void canConvertWithoutNamespaces() throws XPathExpressionException {
        W3CDom w3c = new W3CDom();
        w3c.namespaceAware(false);
        
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'><body id='One'><div>hello</div></body></html>";
        Document dom = w3c.fromJsoup(Jsoup.parse(html));
        NodeList nodeList = xpath(dom, "//body");
        assertEquals("div", nodeList.item(0).getLocalName());
    }

    // Test group: Doctype handling
    // ============================

    @Test
    void roundTripsDoctype() {
        assertDoctypeOutput(
            "<!DOCTYPE html><p>One</p>",
            "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><p>One</p></body></html>"
        );

        assertDoctypeOutput(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>",
            null // XML output varies by JDK
        );

        assertDoctypeOutput(
            "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">",
            "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body/></html>"
        );
    }

    private void assertDoctypeOutput(String input, String expectedHtml, String expectedXml) {
        String htmlOutput = output(input, true);
        assertEqualIgnoreCase(expectedHtml, htmlOutput);

        if (expectedXml != null) {
            String xmlOutput = output(input, false);
            assertEqualIgnoreCase(expectedXml, xmlOutput);
        } else {
            // For variable XML outputs, just check it starts correctly
            String xmlOutput = output(input, false);
            assertTrue(xmlOutput.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC"));
        }
    }

    private String output(String in, boolean modeHtml) {
        org.jsoup.nodes.Document jdoc = Jsoup.parse(in);
        Document w3c = W3CDom.convert(jdoc);
        Map<String, String> properties = modeHtml ? W3CDom.OutputHtml() : W3CDom.OutputXml();
        return normalizeSpaces(W3CDom.asString(w3c, properties));
    }

    // Test group: Source tracking
    // ===========================

    @Test
    void convertsElementAndMaintainsSourceReferences() {
        org.jsoup.nodes.Document jdoc = Jsoup.parse("<body><div><p>One</div><div><p>Two");
        W3CDom w3CDom = new W3CDom();
        Element jDiv = jdoc.selectFirst("div");
        
        Document doc = w3CDom.fromJsoup(jDiv);
        Node div = w3CDom.contextNode(doc);

        assertEquals("div", div.getLocalName());
        assertEquals(jDiv, div.getUserData(W3CDom.SourceProperty));

        Node textNode = div.getFirstChild().getFirstChild();
        assertEquals("One", textNode.getTextContent());
        assertEquals(jdoc.selectFirst("p").childNode(0), textNode.getUserData(W3CDom.SourceProperty));
    }

    // Test group: Edge cases
    // ======================

    @Test
    void parsesCdataNodesInXml() throws XPathExpressionException {
        String html = "<p><script>1 && 2</script><style>3 && 4</style> 5 &amp;&amp; 6</p>";
        org.jsoup.nodes.Document jdoc = Jsoup.parse(html);
        jdoc.outputSettings().syntax(xml);
        String xml = jdoc.body().html();
        
        Document doc = parseXml(xml, false);
        NodeList list = xpath(doc, "//script");
        assertEquals(2, list.getLength());
    }

    @Test
    void ignoresEmptyDoctype() {
        String html = "<!doctype>Foo";
        Document doc = new W3CDom().fromJsoup(Jsoup.parse(html));
        assertNull(doc.getDoctype());
        assertEquals("Foo", doc.getFirstChild().getTextContent());
    }

    @Test
    void handlesCaseInsensitiveAttributesInHtml() throws IOException {
        String html = "<html lang=en>\n" +
            "<body>\n" +
            "<img src=\"firstImage.jpg\" alt=\"Alt one\" />\n" +
            "<IMG SRC=\"secondImage.jpg\" AlT=\"Alt two\" />\n" +
            "</body>\n" +
            "</html>";
        
        Document doc = new W3CDom().fromJsoup(Jsoup.parse(html));
        NodeList imgs = doc.getElementsByTagName("img");
        assertEquals(2, imgs.getLength());
        
        // First image
        Node first = imgs.item(0);
        assertEquals("firstImage.jpg", first.getAttributes().getNamedItem("src").getNodeValue());
        assertEquals("Alt one", first.getAttributes().getNamedItem("alt").getNodeValue());
        
        // Second image
        Node second = imgs.item(1);
        assertEquals("secondImage.jpg", second.getAttributes().getNamedItem("src").getNodeValue());
        assertEquals("Alt two", second.getAttributes().getNamedItem("alt").getNodeValue());
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    void doesNotExpandEntitiesInDoctype(Parser parser) {
        String billionLaughs = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughs, parser);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        
        NodeList p = w3cDoc.getElementsByTagName("p");
        assertEquals(1, p.getLength());
        assertEquals("&lol1;", p.item(0).getTextContent());
    }

    @Test
    void outputsUndeclaredAttributeNamespaceWithXmlns() {
        String html = "<html><body><div v-bind:class='test'></div></body></html>";
        Document w3cDoc = new W3CDom().fromJsoup(Jsoup.parse(html));
        
        String xml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"/></body></html>", xml);
    }

    @Test
    void outputsDeclaredNamespace() {
        String html = "<html xmlns:v-bind=\"http://example.com\"><body><div v-bind:class='test'></div></body></html>";
        Document w3cDoc = new W3CDom().fromJsoup(Jsoup.parse(html));
        
        String xml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v-bind=\"http://example.com\"><head/><body><div v-bind:class=\"test\"/></body></html>", xml);
    }

    @Test
    void outputsNestedElementsWithUndeclaredNamespace() {
        String html = "<html><body><div v-bind:class='test'><span v-bind:style='color:red'></span></div></body></html>";
        Document w3cDoc = new W3CDom().fromJsoup(Jsoup.parse(html));
        
        String xml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"><span v-bind:style=\"color:red\"/></div></body></html>", xml);
    }

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }
}