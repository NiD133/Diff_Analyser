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

    /**
     * Parses an XML string into a W3C Document.
     * 
     * @param xml The XML content as a string.
     * @param nameSpaceAware Whether the parser should be namespace-aware.
     * @return The parsed W3C Document.
     */
    private static Document parseXml(String xml, boolean nameSpaceAware) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(nameSpaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
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

    @Test
    public void testSimpleHtmlToW3cDomConversion() {
        String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --><invalid>What<script>alert('!')";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        NodeList metaTags = w3cDoc.getElementsByTagName("META");
        assertEquals(0, metaTags.getLength());

        String outputXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>W3c</title></head><body><p class=\"one\" id=\"12\">Text</p><!-- comment --><invalid>What<script>alert('!')</script></invalid></body></html>";
        assertEquals(expectedXml, TextUtil.stripNewlines(outputXml));

        Document roundTripDoc = parseXml(outputXml, true);
        assertEquals("Text", roundTripDoc.getElementsByTagName("p").item(0).getTextContent());

        // Check that properties can be set
        Map<String, String> properties = W3CDom.OutputXml();
        properties.put(OutputKeys.INDENT, "yes");
        String formattedOutput = W3CDom.asString(w3cDoc, properties);
        assertTrue(formattedOutput.length() > outputXml.length());
        String formattedExpectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>W3c</title></head><body><p class=\"one\" id=\"12\">Text</p><!-- comment --><invalid>What<script>alert('!')</script></invalid></body></html>";
        assertEquals(formattedExpectedXml, TextUtil.stripNewlines(formattedOutput));
    }

    @Test
    public void testNamespacePreservation() throws IOException {
        File inputFile = ParseTest.getFile("/htmltests/namespaces.xhtml");
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(inputFile, "UTF-8", "", Parser.xmlParser());

        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Node htmlElement = w3cDoc.getChildNodes().item(0);
        assertEquals("http://www.w3.org/1999/xhtml", htmlElement.getNamespaceURI());
        assertEquals("html", htmlElement.getLocalName());

        Node headElement = htmlElement.getFirstChild().getNextSibling();
        assertEquals("http://www.w3.org/1999/xhtml", headElement.getNamespaceURI());
        assertEquals("head", headElement.getLocalName());

        Node epubTitleElement = htmlElement.getChildNodes().item(3).getChildNodes().item(3);
        assertEquals("Check", epubTitleElement.getTextContent());
        assertEquals("http://www.idpf.org/2007/ops", epubTitleElement.getNamespaceURI());
        assertEquals("title", epubTitleElement.getLocalName());

        Node xSectionElement = epubTitleElement.getNextSibling().getNextSibling();
        assertEquals("urn:test", xSectionElement.getNamespaceURI());
        assertEquals("section", xSectionElement.getLocalName());

        Node svgElement = xSectionElement.getNextSibling().getNextSibling();
        assertEquals("http://www.w3.org/2000/svg", svgElement.getNamespaceURI());
        assertEquals("svg", svgElement.getLocalName());

        Node pathElement = svgElement.getChildNodes().item(1);
        assertEquals("http://www.w3.org/2000/svg", pathElement.getNamespaceURI());
        assertEquals("path", pathElement.getLocalName());

        Node clipElement = pathElement.getChildNodes().item(1);
        assertEquals("http://example.com/clip", clipElement.getNamespaceURI());
        assertEquals("clip", clipElement.getLocalName());
        assertEquals("456", clipElement.getTextContent());

        Node pictureElement = svgElement.getNextSibling().getNextSibling();
        assertEquals("http://www.w3.org/1999/xhtml", pictureElement.getNamespaceURI());
        assertEquals("picture", pictureElement.getLocalName());

        Node imgElement = pictureElement.getFirstChild();
        assertEquals("http://www.w3.org/1999/xhtml", imgElement.getNamespaceURI());
        assertEquals("img", imgElement.getLocalName());
    }

    @Test
    public void testHandlesInvalidAttributeNames() {
        String html = "<html><head></head><body style=\"color: red\" \" name\"></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Element bodyElement = jsoupDoc.select("body").first();
        assertTrue(bodyElement.hasAttr("\""));
        assertTrue(bodyElement.hasAttr("name\""));

        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body _=\"\" name_=\"\" style=\"color: red\"/></html>", xmlOutput);
    }

    @Test
    public void testHtmlInputDocMaintainsHtmlAttributeNames() {
        String html = "<!DOCTYPE html><html><head></head><body><p hành=\"1\" hình=\"2\">unicode attr names</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String htmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
        String expectedHtml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p hành=\"1\" hình=\"2\">unicode attr names</p></body></html>";
        assertEquals(expectedHtml, TextUtil.stripNewlines(htmlOutput));
    }

    @Test
    public void testXmlInputDocMaintainsHtmlAttributeNames() {
        String html = "<!DOCTYPE html><html><head></head><body><p hành=\"1\" hình=\"2\">unicode attr names coerced</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.outputSettings().syntax(xml);

        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String htmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
        String expectedHtml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p h_nh=\"2\">unicode attr names coerced</p></body></html>";
        assertEquals(expectedHtml, TextUtil.stripNewlines(htmlOutput));
    }

    @Test
    public void testHandlesInvalidTagAsText() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<インセンティブで高収入！>Text <p>More</p>");

        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body>&lt;インセンティブで高収入！&gt;Text <p>More</p></body></html>", xmlOutput);
    }

    @Test
    public void testHandlesHtmlElementsWithLt() {
        String input = "<foo<bar attr<name=\"123\"><b>Text</b></foo<bar>";
        String expectedXml = "<foo_bar attr_name=\"123\"><b>Text</b></foo_bar>";

        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(input);
        String htmlSerialized = htmlDoc.body().html();
        assertEquals(input, normalizeSpaces(htmlSerialized));
        Element htmlRoundTrip = Jsoup.parse(htmlSerialized).body();
        assertTrue(htmlDoc.body().hasSameValue(htmlRoundTrip));

        htmlDoc.outputSettings().syntax(xml);
        String xmlSerialized = htmlDoc.body().html();
        assertEquals(expectedXml, normalizeSpaces(xmlSerialized));
        org.jsoup.nodes.Document xmlDoc = Jsoup.parse(xmlSerialized);
        String xmlRoundTripSerialized = xmlDoc.body().html();
        assertEquals(expectedXml, normalizeSpaces(xmlRoundTripSerialized));
        Element xmlRoundTrip = Jsoup.parse(xmlRoundTripSerialized).body();
        assertTrue(xmlDoc.body().hasSameValue(xmlRoundTrip));

        Document w3cXmlDoc = parseXml(xmlSerialized, true);
        NodeList w3cXmlNodes = w3cXmlDoc.getElementsByTagName("foo_bar");
        assertEquals(1, w3cXmlNodes.getLength());
        assertEquals("123", w3cXmlNodes.item(0).getAttributes().getNamedItem("attr_name").getTextContent());

        Document w3cDoc = W3CDom.convert(htmlDoc);
        NodeList w3cNodes = w3cDoc.getElementsByTagName("foo_bar");
        assertEquals(1, w3cNodes.getLength());
        assertEquals("123", w3cNodes.item(0).getAttributes().getNamedItem("attr_name").getTextContent());
    }

    @Test
    public void testConvertToCustomDocument() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><div></div></html>");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document customDoc = factory.newDocumentBuilder().newDocument();

        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, customDoc);

        String htmlOutput = W3CDom.asString(customDoc, W3CDom.OutputHtml());
        assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div></div></body></html>", htmlOutput);
    }

    @Test
    public void testTreatsUndeclaredNamespaceAsLocalName() {
        String html = "<fb:like>One</fb:like>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Node htmlElement = w3cDoc.getFirstChild();

        assertEquals("http://www.w3.org/1999/xhtml", htmlElement.getNamespaceURI());
        assertEquals("html", htmlElement.getLocalName());

        Node fbElement = htmlElement.getFirstChild().getNextSibling().getFirstChild();
        assertEquals("http://www.w3.org/1999/xhtml", fbElement.getNamespaceURI());
        assertEquals("like", fbElement.getLocalName());
        assertEquals("fb:like", fbElement.getNodeName());
    }

    @Test
    public void testXmlnsXpath() throws XPathExpressionException {
        W3CDom w3cDom = new W3CDom();
        String html = "<html><body><div>hello</div></body></html>";
        Document dom = w3cDom.fromJsoup(Jsoup.parse(html));
        NodeList nodeList = evaluateXpath(dom, "//*[local-name()=\"body\"]");
        assertEquals("div", nodeList.item(0).getLocalName());

        html = "<html xmlns='http://www.w3.org/1999/xhtml'><body id='One'><div>hello</div></body></html>";
        dom = w3cDom.fromJsoup(Jsoup.parse(html));
        nodeList = evaluateXpath(dom, "//body");
        assertNull(nodeList);

        dom = w3cDom.fromJsoup(Jsoup.parse(html));
        nodeList = evaluateXpath(dom, "//*[local-name()=\"body\"]");
        assertNotNull(nodeList);
        assertEquals(1, nodeList.getLength());
        assertEquals("div", nodeList.item(0).getLocalName());
        assertEquals("http://www.w3.org/1999/xhtml", nodeList.item(0).getNamespaceURI());

        String xml = w3cDom.asString(dom);
        dom = parseXml(xml, false);
        Node item = (Node) evaluateXpath(dom, "//body");
        assertEquals("body", item.getNodeName());
        assertNull(item.getNamespaceURI());

        dom = parseXml(xml, true);
        nodeList = evaluateXpath(dom, "//body");
        assertNull(nodeList);
    }

    @Test
    public void testXhtmlNoNamespace() throws XPathExpressionException {
        W3CDom w3cDom = new W3CDom();
        String html = "<html><body><div>hello</div></body></html>";
        w3cDom.namespaceAware(false);
        Document dom = w3cDom.fromJsoup(Jsoup.parse(html));
        NodeList nodeList = evaluateXpath(dom, "//body");
        assertEquals(1, nodeList.getLength());
        assertEquals("div", nodeList.item(0).getLocalName());
    }

    @Test
    public void testCanDisableNamespaces() throws XPathExpressionException {
        W3CDom w3cDom = new W3CDom();
        assertTrue(w3cDom.namespaceAware());

        w3cDom.namespaceAware(false);
        assertFalse(w3cDom.namespaceAware());

        String html = "<html xmlns='http://www.w3.org/1999/xhtml'><body id='One'><div>hello</div></body></html>";
        Document dom = w3cDom.fromJsoup(Jsoup.parse(html));
        NodeList nodeList = evaluateXpath(dom, "//body");
        assertEquals("div", nodeList.item(0).getLocalName());
    }

    private NodeList evaluateXpath(Document doc, String query) throws XPathExpressionException {
        XPathExpression xpath = XPathFactory.newInstance().newXPath().compile(query);
        return (NodeList) xpath.evaluate(doc, XPathConstants.NODE);
    }

    @Test
    public void testRoundTripDoctype() {
        String baseHtml = "<!DOCTYPE html><p>One</p>";
        assertEqualsIgnoreCase("<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>", convertAndOutput(baseHtml, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><p>One</p></body></html>", convertAndOutput(baseHtml, false));

        String publicDoc = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEqualsIgnoreCase("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>", convertAndOutput(publicDoc, true));
        assertTrue(convertAndOutput(publicDoc, false).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC"));

        String systemDoc = "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">";
        assertEqualsIgnoreCase("<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>", convertAndOutput(systemDoc, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body/></html>", convertAndOutput(systemDoc, false));

        String legacyDoc = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        assertEqualsIgnoreCase("<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>", convertAndOutput(legacyDoc, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body/></html>", convertAndOutput(legacyDoc, false));

        String noDoctype = "<p>One</p>";
        assertEqualsIgnoreCase("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>", convertAndOutput(noDoctype, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><p>One</p></body></html>", convertAndOutput(noDoctype, false));
    }

    private String convertAndOutput(String input, boolean modeHtml) {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(input);
        Document w3cDoc = W3CDom.convert(jsoupDoc);

        Map<String, String> properties = modeHtml ? W3CDom.OutputHtml() : W3CDom.OutputXml();
        return normalizeSpaces(W3CDom.asString(w3cDoc, properties));
    }

    private void assertEqualsIgnoreCase(String expected, String actual) {
        assertEquals(expected.toLowerCase(Locale.ROOT), actual.toLowerCase(Locale.ROOT));
    }

    @Test
    public void testCanOutputHtmlWithoutNamespace() {
        String html = "<p>One</p>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        w3cDom.namespaceAware(false);

        String htmlOutput = W3CDom.asString(w3cDom.fromJsoup(jsoupDoc), W3CDom.OutputHtml());
        String xmlOutput = W3CDom.asString(w3cDom.fromJsoup(jsoupDoc), W3CDom.OutputXml());
        assertEqualsIgnoreCase(
            "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"></head><body><p>one</p></body></html>",
            htmlOutput);
        assertEqualsIgnoreCase(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head/><body><p>One</p></body></html>",
            xmlOutput);
    }

    @Test
    public void testConvertsElementsAndMaintainsSource() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<body><div><p>One</div><div><p>Two");
        W3CDom w3cDom = new W3CDom();
        Element jsoupDiv = jsoupDoc.selectFirst("div");
        assertNotNull(jsoupDiv);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDiv);
        Node w3cDiv = w3cDom.contextNode(w3cDoc);

        assertEquals("div", w3cDiv.getLocalName());
        assertEquals(jsoupDiv, w3cDiv.getUserData(W3CDom.SourceProperty));

        Node textNode = w3cDiv.getFirstChild().getFirstChild();
        assertEquals("One", textNode.getTextContent());
        assertEquals(Node.TEXT_NODE, textNode.getNodeType());

        org.jsoup.nodes.TextNode jsoupTextNode = (TextNode) jsoupDiv.childNode(0).childNode(0);
        assertEquals(jsoupTextNode, textNode.getUserData(W3CDom.SourceProperty));
    }

    @Test
    public void testCanXmlParseCdataNodes() throws XPathExpressionException {
        String html = "<p><script>1 && 2</script><style>3 && 4</style> 5 &amp;&amp; 6</p>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.outputSettings().syntax(xml);
        String xml = jsoupDoc.body().html();
        assertTrue(xml.contains("<script>//<![CDATA[\n1 && 2\n//]]></script>"));
        Document w3cDoc = parseXml(xml, false);
        NodeList nodeList = evaluateXpath(w3cDoc, "//script");
        assertEquals(2, nodeList.getLength());
        Node scriptComment = nodeList.item(0);
        assertEquals("//", scriptComment.getTextContent());
        Node script = nodeList.item(1);
        assertEquals("\n1 && 2\n//", script.getTextContent());
    }

    @Test
    public void testHandlesEmptyDoctype() {
        String html = "<!doctype>Foo";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNull(w3cDoc.getDoctype());
        assertEquals("Foo", w3cDoc.getFirstChild().getTextContent());
    }

    @Test
    public void testHtmlParseAttributesAreCaseInsensitive() throws IOException {
        String html = "<html lang=en>\n" +
            "<body>\n" +
            "<img src=\"firstImage.jpg\" alt=\"Alt one\" />\n" +
            "<IMG SRC=\"secondImage.jpg\" AlT=\"Alt two\" />\n" +
            "</body>\n" +
            "</html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Element bodyElement = (org.w3c.dom.Element) w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
        NodeList imgElements = bodyElement.getElementsByTagName("img");
        assertEquals(2, imgElements.getLength());
        org.w3c.dom.Element firstImg = (org.w3c.dom.Element) imgElements.item(0);
        assertEquals(2, firstImg.getAttributes().getLength());
        assertEquals("firstImage.jpg", firstImg.getAttribute("src"));
        assertEquals("Alt one", firstImg.getAttribute("alt"));
        org.w3c.dom.Element secondImg = (org.w3c.dom.Element) imgElements.item(1);
        assertEquals(2, secondImg.getAttributes().getLength());
        assertEquals("secondImage.jpg", secondImg.getAttribute("src"));
        assertEquals("Alt two", secondImg.getAttribute("alt"));
    }

    @ParameterizedTest
    @MethodSource("parserProvider")
    public void testDoesNotExpandEntities(Parser parser) {
        String billionLaughs = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughs, parser);
        W3CDom w3cDom = new W3CDom();

        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        NodeList pElements = w3cDoc.getElementsByTagName("p");
        assertEquals(1, pElements.getLength());
        assertEquals("&lol1;", pElements.item(0).getTextContent());

        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(xmlOutput.contains("lololol"));
        assertTrue(xmlOutput.contains("&amp;lol1;"));
    }

    @Test
    public void testUndeclaredAttrNamespaceAsString() {
        W3CDom w3cDom = new W3CDom();
        String html = "<html><body><div v-bind:class='test'></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String xmlOutput = w3cDom.asString(w3cDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"/></body></html>", xmlOutput);
    }

    @Test
    public void testDeclaredNamespaceIsUsed() {
        W3CDom w3cDom = new W3CDom();
        String html = "<html xmlns:v-bind=\"http://example.com\"><body><div v-bind:class='test'></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String xmlOutput = w3cDom.asString(w3cDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v-bind=\"http://example.com\"><head/><body><div v-bind:class=\"test\"/></body></html>", xmlOutput);
    }

    @Test
    public void testNestedElementsWithUndeclaredNamespace() {
        W3CDom w3cDom = new W3CDom();
        String html = "<html><body><div v-bind:class='test'><span v-bind:style='color:red'></span></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String xmlOutput = w3cDom.asString(w3cDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"><span v-bind:style=\"color:red\"/></div></body></html>", xmlOutput);
    }

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }
}