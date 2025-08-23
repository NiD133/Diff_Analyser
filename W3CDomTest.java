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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathConstants;
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

    // Common namespaces used across multiple tests
    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
    private static final String SVG_NS = "http://www.w3.org/2000/svg";
    private static final String EPUB_NS = "http://www.idpf.org/2007/ops";
    private static final String CLIP_NS = "http://example.com/clip";
    private static final String TEST_NS = "urn:test";
    private static final String LEGACY_DOCTYPE = "about:legacy-compat";

    // --------------------
    // Test helpers
    // --------------------

    /**
     * Parse XML into a W3C Document with explicit namespace awareness control.
     */
    private static Document parseW3cXml(String xml, boolean namespaceAware) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(namespaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver((publicId, systemId) -> {
                if (systemId.contains(LEGACY_DOCTYPE)) {
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

    /**
     * Evaluate an XPath and return the first matching node (or null if none).
     */
    private static Node selectNode(Document w3cDoc, String query) throws XPathExpressionException {
        XPathExpression xp = XPathFactory.newInstance().newXPath().compile(query);
        return (Node) xp.evaluate(w3cDoc, XPathConstants.NODE);
    }

    /**
     * Evaluate an XPath and return all matching nodes (empty NodeList if none).
     */
    private static NodeList selectNodes(Document w3cDoc, String query) throws XPathExpressionException {
        XPathExpression xp = XPathFactory.newInstance().newXPath().compile(query);
        return (NodeList) xp.evaluate(w3cDoc, XPathConstants.NODESET);
    }

    private static String asXml(Document w3cDoc) {
        return W3CDom.asString(w3cDoc, W3CDom.OutputXml());
    }

    private static String asHtml(Document w3cDoc) {
        return W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
    }

    private void assertEqualsIgnoreCase(String want, String have) {
        assertEquals(want.toLowerCase(Locale.ROOT), have.toLowerCase(Locale.ROOT));
    }

    private String output(String in, boolean modeHtml) {
        org.jsoup.nodes.Document jdoc = Jsoup.parse(in);
        Document w3c = W3CDom.convert(jdoc);
        Map<String, String> properties = modeHtml ? W3CDom.OutputHtml() : W3CDom.OutputXml();
        return normalizeSpaces(W3CDom.asString(w3c, properties));
    }

    // --------------------
    // Tests
    // --------------------

    @Test
    public void simpleConversion() {
        String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --><invalid>What<script>alert('!')";
        org.jsoup.nodes.Document jsoup = Jsoup.parse(html);

        // Convert
        W3CDom w3c = new W3CDom();
        Document wDoc = w3c.fromJsoup(jsoup);

        // Sanity: no META added in XML output mode
        NodeList meta = wDoc.getElementsByTagName("META");
        assertEquals(0, meta.getLength());

        // XML output is XHTML with sanitized attributes/structure
        String out = asXml(wDoc);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"" + XHTML_NS + "\"><head><title>W3c</title></head><body><p class=\"one\" id=\"12\">Text</p><!-- comment --><invalid>What<script>alert('!')</script></invalid></body></html>";
        assertEquals(expected, TextUtil.stripNewlines(out));

        // Round-trip parse to verify content
        Document roundTrip = parseW3cXml(out, true);
        assertEquals("Text", roundTrip.getElementsByTagName("p").item(0).getTextContent());

        // Pretty-print settings: check output length grew and structure stable
        Map<String, String> properties = W3CDom.OutputXml();
        properties.put(OutputKeys.INDENT, "yes");
        String furtherOut = W3CDom.asString(wDoc, properties);
        assertTrue(furtherOut.length() > out.length());
        String furtherExpected = expected;
        assertEquals(furtherExpected, TextUtil.stripNewlines(furtherOut));
    }

    @Test
    public void namespacePreservation() throws IOException {
        // Given: Namespaced input (parsed with XML parser)
        File in = ParseTest.getFile("/htmltests/namespaces.xhtml");
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(in, "UTF-8", "", Parser.xmlParser());

        // When: Converting to W3C DOM
        Document doc = new W3CDom().fromJsoup(jsoupDoc);

        // Then: Root element is XHTML
        org.w3c.dom.Element htmlEl = doc.getDocumentElement();
        assertEquals(XHTML_NS, htmlEl.getNamespaceURI());
        assertEquals("html", htmlEl.getLocalName());
        assertEquals("html", htmlEl.getNodeName());

        // Head inherits default XHTML namespace
        Node head = doc.getElementsByTagNameNS(XHTML_NS, "head").item(0);
        assertEquals(XHTML_NS, head.getNamespaceURI());
        assertEquals("head", head.getLocalName());
        assertEquals("head", head.getNodeName());

        // epub:title is preserved in the EPUB namespace
        Node epubTitle = doc.getElementsByTagNameNS(EPUB_NS, "title").item(0);
        assertEquals("Check", epubTitle.getTextContent());
        assertEquals(EPUB_NS, epubTitle.getNamespaceURI());
        assertEquals("title", epubTitle.getLocalName());
        assertEquals("epub:title", epubTitle.getNodeName());

        // x:section is preserved in test namespace
        Node xSection = doc.getElementsByTagNameNS(TEST_NS, "section").item(0);
        assertEquals(TEST_NS, xSection.getNamespaceURI());
        assertEquals("section", xSection.getLocalName());
        assertEquals("x:section", xSection.getNodeName());

        // SVG subtree correctly scoped
        Node svg = doc.getElementsByTagNameNS(SVG_NS, "svg").item(0);
        assertEquals(SVG_NS, svg.getNamespaceURI());
        assertEquals("svg", svg.getLocalName());
        assertEquals("svg", svg.getNodeName());

        Node path = doc.getElementsByTagNameNS(SVG_NS, "path").item(0);
        assertEquals(SVG_NS, path.getNamespaceURI());
        assertEquals("path", path.getLocalName());
        assertEquals("path", path.getNodeName());

        Node clip = doc.getElementsByTagNameNS(CLIP_NS, "clip").item(0);
        assertEquals(CLIP_NS, clip.getNamespaceURI());
        assertEquals("clip", clip.getLocalName());
        assertEquals("clip", clip.getNodeName());
        assertEquals("456", clip.getTextContent());

        // Return to XHTML for picture/img
        Node picture = doc.getElementsByTagNameNS(XHTML_NS, "picture").item(0);
        assertEquals(XHTML_NS, picture.getNamespaceURI());
        assertEquals("picture", picture.getLocalName());
        assertEquals("picture", picture.getNodeName());

        Node img = doc.getElementsByTagNameNS(XHTML_NS, "img").item(0);
        assertEquals(XHTML_NS, img.getNamespaceURI());
        assertEquals("img", img.getLocalName());
        assertEquals("img", img.getNodeName());
    }

    @Test
    public void handlesInvalidAttributeNames() {
        String html = "<html><head></head><body style=\"color: red\" \" name\"></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Element body = jsoupDoc.selectFirst("body");
        assertNotNull(body);
        assertTrue(body.hasAttr("\""));    // per HTML5 spec
        assertTrue(body.hasAttr("name\""));

        Document w3Doc = W3CDom.convert(jsoupDoc);
        String xmlOut = asXml(w3Doc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"" + XHTML_NS + "\"><head/><body _=\"\" name_=\"\" style=\"color: red\"/></html>", xmlOut);
    }

    @Test
    public void htmlInputDocMaintainsHtmlAttributeNames() {
        String html = "<!DOCTYPE html><html><head></head><body><p hành=\"1\" hình=\"2\">unicode attr names</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        Document w3Doc = W3CDom.convert(jsoupDoc);
        String out = asHtml(w3Doc);
        String expected = "<!DOCTYPE html SYSTEM \"" + LEGACY_DOCTYPE + "\"><html xmlns=\"" + XHTML_NS + "\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p hành=\"1\" hình=\"2\">unicode attr names</p></body></html>";
        assertEquals(expected, TextUtil.stripNewlines(out));
    }

    @Test
    public void xmlInputDocMaintainsHtmlAttributeNames() {
        String html = "<!DOCTYPE html><html><head></head><body><p hành=\"1\" hình=\"2\">unicode attr names coerced</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.outputSettings().syntax(xml);

        Document w3Doc = W3CDom.convert(jsoupDoc);
        String out = asHtml(w3Doc);
        String expected = "<!DOCTYPE html SYSTEM \"" + LEGACY_DOCTYPE + "\"><html xmlns=\"" + XHTML_NS + "\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p h_nh=\"2\">unicode attr names coerced</p></body></html>";
        assertEquals(expected, TextUtil.stripNewlines(out));
    }

    @Test
    public void handlesInvalidTagAsText() {
        org.jsoup.nodes.Document jsoup = Jsoup.parse("<インセンティブで高収入！>Text <p>More</p>");
        Document w3Doc = W3CDom.convert(jsoup);
        String xmlOut = asXml(w3Doc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"" + XHTML_NS + "\"><head/><body>&lt;インセンティブで高収入！&gt;Text <p>More</p></body></html>", xmlOut);
    }

    @Test
    void handlesHtmlElsWithLt() {
        // Elements and attributes may legally include '<' in HTML names (e.g. <foo<bar>), which need coercion for XML.
        String input = "<foo<bar attr<name=\"123\"><b>Text</b></foo<bar>";
        String xmlExpect = "<foo_bar attr_name=\"123\"><b>Text</b></foo_bar>";

        // HTML round-trip
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(input);
        String htmlSerial = htmlDoc.body().html();
        assertEquals(input, normalizeSpaces(htmlSerial));
        Element htmlRound = Jsoup.parse(htmlSerial).body();
        assertTrue(htmlDoc.body().hasSameValue(htmlRound));

        // XML round-trip (coercion applied)
        htmlDoc.outputSettings().syntax(xml);
        String asXml = htmlDoc.body().html();
        assertEquals(xmlExpect, normalizeSpaces(asXml));
        org.jsoup.nodes.Document xmlDoc = Jsoup.parse(asXml);
        String xmlSerial = xmlDoc.body().html();
        assertEquals(xmlExpect, normalizeSpaces(xmlSerial));
        Element xmlRound = Jsoup.parse(xmlSerial).body();
        assertTrue(xmlDoc.body().hasSameValue(xmlRound));

        // W3C can parse the coerced XML
        Document w3cXml = parseW3cXml(asXml, true);
        NodeList w3cXmlNodes = w3cXml.getElementsByTagName("foo_bar");
        assertEquals(1, w3cXmlNodes.getLength());
        assertEquals("123", w3cXmlNodes.item(0).getAttributes().getNamedItem("attr_name").getTextContent());

        // jsoup -> W3C conversion coerces the names as expected
        Document w3cDoc = W3CDom.convert(htmlDoc);
        NodeList w3cNodes = w3cDoc.getElementsByTagName("foo_bar");
        assertEquals(1, w3cNodes.getLength());
        assertEquals("123", w3cNodes.item(0).getAttributes().getNamedItem("attr_name").getTextContent());
    }

    @Test
    public void canConvertToCustomDocument() throws ParserConfigurationException {
        org.jsoup.nodes.Document in = Jsoup.parse("<html><div></div></html>");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document customDoc = dbf.newDocumentBuilder().newDocument();

        new W3CDom().convert(in, customDoc);

        String html = asHtml(customDoc);
        assertEquals("<html xmlns=\"" + XHTML_NS + "\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div></div></body></html>", html);
    }

    @Test
    public void treatsUndeclaredNamespaceAsLocalName() {
        String html = "<fb:like>One</fb:like>";
        org.jsoup.nodes.Document jdoc = Jsoup.parse(html);

        Document w3Doc = new W3CDom().fromJsoup(jdoc);
        Node htmlEl = w3Doc.getFirstChild();

        assertEquals(XHTML_NS, htmlEl.getNamespaceURI());
        assertEquals("html", htmlEl.getLocalName());
        assertEquals("html", htmlEl.getNodeName());

        Node fb = htmlEl.getFirstChild().getNextSibling().getFirstChild();
        assertEquals(XHTML_NS, fb.getNamespaceURI());
        assertEquals("like", fb.getLocalName());
        assertEquals("fb:like", fb.getNodeName());
    }

    @Test
    public void xmlnsXpathTest() throws XPathExpressionException {
        W3CDom w3c = new W3CDom();

        // Default HTML without explicit xmlns: XPath by local-name works
        String html = "<html><body><div>hello</div></body></html>";
        Document dom = w3c.fromJsoup(Jsoup.parse(html));
        NodeList nodes = selectNodes(dom, "//*[local-name()=\"body\"]");
        assertEquals("div", nodes.item(0).getLocalName());

        // With explicit XHTML namespace, //body won't match if not namespace-aware in the query
        html = "<html xmlns='" + XHTML_NS + "'><body id='One'><div>hello</div></body></html>";
        dom = w3c.fromJsoup(Jsoup.parse(html));
        Node noMatch = selectNode(dom, "//body");
        assertNull(noMatch);

        // Using local-name works
        dom = w3c.fromJsoup(Jsoup.parse(html));
        nodes = selectNodes(dom, "//*[local-name()=\"body\"]");
        assertNotNull(nodes);
        assertEquals(1, nodes.getLength());
        assertEquals("div", nodes.item(0).getLocalName());
        assertEquals(XHTML_NS, nodes.item(0).getNamespaceURI());
        assertNull(nodes.item(0).getPrefix());

        // If the DOM is not namespace-aware, //body can match
        String xml = w3c.asString(dom);
        dom = parseW3cXml(xml, false);
        Node item = selectNode(dom, "//body");
        assertEquals("body", item.getNodeName());
        assertNull(item.getNamespaceURI());
        assertNull(item.getPrefix());

        // If we re-enable namespace awareness, //body won't match again
        dom = parseW3cXml(xml, true);
        Node none = selectNode(dom, "//body");
        assertNull(none);
    }

    @Test
    public void xhtmlNoNamespace() throws XPathExpressionException {
        // When W3CDom is not namespace aware, //body selects without prefixes
        W3CDom w3c = new W3CDom().namespaceAware(false);
        Document dom = w3c.fromJsoup(Jsoup.parse("<html><body><div>hello</div></body></html>"));
        NodeList nodeList = selectNodes(dom, "//body");
        assertEquals(1, nodeList.getLength());
        assertEquals("div", nodeList.item(0).getLocalName());
    }

    @Test
    void canDisableNamespaces() throws XPathExpressionException {
        W3CDom w3c = new W3CDom();
        assertTrue(w3c.namespaceAware());

        w3c.namespaceAware(false);
        assertFalse(w3c.namespaceAware());

        String html = "<html xmlns='" + XHTML_NS + "'><body id='One'><div>hello</div></body></html>";
        Document dom = w3c.fromJsoup(Jsoup.parse(html));
        NodeList nodes = selectNodes(dom, "//body"); // no ns, so needs no prefix
        assertEquals("div", nodes.item(0).getLocalName());
    }

    @Test
    public void testRoundTripDoctype() {
        // Note: Transformer impls may differ in element case (META vs meta), so compare case-insensitively.

        String base = "<!DOCTYPE html><p>One</p>";
        assertEqualsIgnoreCase("<!DOCTYPE html SYSTEM \"" + LEGACY_DOCTYPE + "\"><html xmlns=\"" + XHTML_NS + "\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>", output(base, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"" + LEGACY_DOCTYPE + "\"><html xmlns=\"" + XHTML_NS + "\"><head/><body><p>One</p></body></html>", output(base, false));

        String publicDoc = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEqualsIgnoreCase("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"" + XHTML_NS + "\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>", output(publicDoc, true));
        assertTrue(output(publicDoc, false).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC"));

        String systemDoc = "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">";
        assertEqualsIgnoreCase("<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"" + XHTML_NS + "\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>", output(systemDoc, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"" + XHTML_NS + "\"><head/><body/></html>", output(systemDoc, false));

        String legacyDoc = "<!DOCTYPE html SYSTEM \"" + LEGACY_DOCTYPE + "\">";
        assertEqualsIgnoreCase("<!DOCTYPE html SYSTEM \"" + LEGACY_DOCTYPE + "\"><html xmlns=\"" + XHTML_NS + "\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>", output(legacyDoc, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"" + LEGACY_DOCTYPE + "\"><html xmlns=\"" + XHTML_NS + "\"><head/><body/></html>", output(legacyDoc, false));

        String noDoctype = "<p>One</p>";
        assertEqualsIgnoreCase("<html xmlns=\"" + XHTML_NS + "\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>", output(noDoctype, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"" + XHTML_NS + "\"><head/><body><p>One</p></body></html>", output(noDoctype, false));
    }

    @Test
    public void canOutputHtmlWithoutNamespace() {
        String html = "<p>One</p>";
        org.jsoup.nodes.Document jdoc = Jsoup.parse(html);
        W3CDom w3c = new W3CDom().namespaceAware(false);

        String asHtml = W3CDom.asString(w3c.fromJsoup(jdoc), W3CDom.OutputHtml());
        String asXtml = W3CDom.asString(w3c.fromJsoup(jdoc), W3CDom.OutputXml());
        assertEqualsIgnoreCase(
            "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"></head><body><p>one</p></body></html>",
            asHtml);
        assertEqualsIgnoreCase(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head/><body><p>One</p></body></html>",
            asXtml);
    }

    @Test
    public void convertsElementsAndMaintainsSource() {
        org.jsoup.nodes.Document jdoc = Jsoup.parse("<body><div><p>One</div><div><p>Two");
        W3CDom w3CDom = new W3CDom();
        Element jDiv = jdoc.selectFirst("div");
        assertNotNull(jDiv);

        Document doc = w3CDom.fromJsoup(jDiv);
        Node div = w3CDom.contextNode(doc);

        assertEquals("div", div.getLocalName());
        assertEquals(jDiv, div.getUserData(W3CDom.SourceProperty));

        Node textNode = div.getFirstChild().getFirstChild();
        assertEquals("One", textNode.getTextContent());
        assertEquals(Node.TEXT_NODE, textNode.getNodeType());

        TextNode jText = (TextNode) jDiv.childNode(0).childNode(0);
        assertEquals(jText, textNode.getUserData(W3CDom.SourceProperty));
    }

    @Test
    public void canXmlParseCdataNodes() throws XPathExpressionException {
        String html = "<p><script>1 && 2</script><style>3 && 4</style> 5 &amp;&amp; 6</p>";
        org.jsoup.nodes.Document jdoc = Jsoup.parse(html);
        jdoc.outputSettings().syntax(xml);

        String xmlBody = jdoc.body().html();
        assertTrue(xmlBody.contains("<script>//<![CDATA[\n1 && 2\n//]]></script>"));

        Document doc = parseW3cXml(xmlBody, false);
        NodeList list = selectNodes(doc, "//script");
        assertEquals(2, list.getLength());

        Node scriptComment = list.item(0); // will be the cdata wrapper comment node
        assertEquals("//", scriptComment.getTextContent());
        Node script = list.item(1);
        assertEquals("\n1 && 2\n//", script.getTextContent());
    }

    @Test
    public void handlesEmptyDoctype() {
        String html = "<!doctype>Foo";
        org.jsoup.nodes.Document jdoc = Jsoup.parse(html);
        Document doc = new W3CDom().fromJsoup(jdoc);
        assertNull(doc.getDoctype());
        assertEquals("Foo", doc.getFirstChild().getTextContent());
    }

    @Test
    void testHtmlParseAttributesAreCaseInsensitive() throws IOException {
        // Attribute names differ in case across IMG elements; ensure both map to lowercase in output
        String html = "<html lang=en>\n" +
            "<body>\n" +
            "<img src=\"firstImage.jpg\" alt=\"Alt one\" />\n" +
            "<IMG SRC=\"secondImage.jpg\" AlT=\"Alt two\" />\n" +
            "</body>\n" +
            "</html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        Document doc = new W3CDom().fromJsoup(jsoupDoc);
        org.w3c.dom.Element body = (org.w3c.dom.Element) doc.getDocumentElement().getElementsByTagName("body").item(0);
        NodeList imgs = body.getElementsByTagName("img");
        assertEquals(2, imgs.getLength());

        org.w3c.dom.Element first = (org.w3c.dom.Element) imgs.item(0);
        assertEquals(2, first.getAttributes().getLength());
        assertEquals("firstImage.jpg", first.getAttribute("src"));
        assertEquals("Alt one", first.getAttribute("alt"));

        org.w3c.dom.Element second = (org.w3c.dom.Element) imgs.item(1);
        assertEquals(2, second.getAttributes().getLength());
        assertEquals("secondImage.jpg", second.getAttribute("src"));
        assertEquals("Alt two", second.getAttribute("alt"));
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    void doesNotExpandEntities(Parser parser) {
        // billion laughs / XXE doctype entities are not expanded by jsoup parser, so conversion must keep them unexpanded.
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

        String string = asXml(w3cDoc);
        assertFalse(string.contains("lololol"));
        assertTrue(string.contains("&amp;lol1;"));
    }

    @Test
    void undeclaredAttrNamespaceAsString() {
        // Undeclared attribute namespace should be emitted with xmlns:prefix="undefined" for transformer validity
        W3CDom w3CDom = new W3CDom();
        String html = "<html><body><div v-bind:class='test'></div></body></html>";
        org.jsoup.nodes.Document jdoc = Jsoup.parse(html);
        Document w3CDoc = w3CDom.fromJsoup(jdoc);

        String xml = w3CDom.asString(w3CDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"" + XHTML_NS + "\"><head/><body><div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"/></body></html>", xml);
    }

    @Test
    void declaredNamespaceIsUsed() {
        W3CDom w3CDom = new W3CDom();
        String html = "<html xmlns:v-bind=\"http://example.com\"><body><div v-bind:class='test'></div></body></html>";
        org.jsoup.nodes.Document jdoc = Jsoup.parse(html);
        Document w3CDoc = w3CDom.fromJsoup(jdoc);

        String xml = w3CDom.asString(w3CDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"" + XHTML_NS + "\" xmlns:v-bind=\"http://example.com\"><head/><body><div v-bind:class=\"test\"/></body></html>", xml);
    }

    @Test
    void nestedElementsWithUndeclaredNamespace() {
        W3CDom w3CDom = new W3CDom();
        String html = "<html><body><div v-bind:class='test'><span v-bind:style='color:red'></span></div></body></html>";
        org.jsoup.nodes.Document jdoc = Jsoup.parse(html);
        Document w3CDoc = w3CDom.fromJsoup(jdoc);

        String xml = w3CDom.asString(w3CDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"" + XHTML_NS + "\"><head/><body><div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"><span v-bind:style=\"color:red\"/></div></body></html>", xml);
    }

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }
}