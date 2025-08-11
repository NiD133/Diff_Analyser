package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.jsoup.parser.Parser.NamespaceXml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    private static final String FOO_BASE = "http://foo.com/";
    private static final String EXAMPLE_BASE = "http://example.com/";

    // ---------- Helpers ----------
    private static Document xmlDoc(String xml) {
        return Jsoup.parse(xml, "", Parser.xmlParser());
    }

    private static Document xmlDoc(String xml, String baseUri) {
        return Jsoup.parse(xml, baseUri, Parser.xmlParser());
    }

    private static Document xmlDocWithTreeBuilder(String xml, String baseUri) {
        return new XmlTreeBuilder().parse(xml, baseUri);
    }

    private static String strip(String html) {
        return TextUtil.stripNewlines(html);
    }

    private static String strip(Document doc) {
        return strip(doc.html());
    }

    private static Document parseResource(String resourcePath, String baseUri) throws IOException, URISyntaxException {
        File xmlFile = new File(XmlTreeBuilder.class.getResource(resourcePath).toURI());
        try (InputStream in = java.nio.file.Files.newInputStream(xmlFile.toPath())) {
            return Jsoup.parse(in, null, baseUri, Parser.xmlParser());
        }
    }

    // ---------- Basic parsing ----------
    @Test
    public void simpleParse_parsesAttrsAndLinksAbsUrl() {
        String xml = "<doc id=2 href='/bar'>Foo <br /><link>One</link><link>Two</link></doc>";

        Document doc = xmlDocWithTreeBuilder(xml, FOO_BASE);

        assertEquals("<doc id=\"2\" href=\"/bar\">Foo <br /><link>One</link><link>Two</link></doc>", strip(doc));
        assertEquals("http://foo.com/bar", doc.getElementById("2").absUrl("href"));
    }

    @Test
    public void popToClose_closesToMatchingTagIgnoresUnknownClose() {
        // </val> closes Two, </bar> ignored
        String xml = "<doc><val>One<val>Two</val></bar>Three</doc>";

        Document doc = xmlDocWithTreeBuilder(xml, FOO_BASE);

        assertEquals("<doc><val>One<val>Two</val>Three</val></doc>", strip(doc));
    }

    @Test
    public void preservesCommentAndDocType() {
        String xml = "<!DOCTYPE HTML><!-- a comment -->One <qux />Two";

        Document doc = xmlDocWithTreeBuilder(xml, FOO_BASE);

        assertEquals("<!DOCTYPE HTML><!-- a comment -->One <qux />Two", strip(doc));
    }

    @Test
    public void supplyingParserToJsoup_parsesXml() {
        String xml = "<doc><val>One<val>Two</val></bar>Three</doc>";

        Document doc = Jsoup.parse(xml, FOO_BASE, Parser.xmlParser());

        assertEquals("<doc><val>One<val>Two</val>Three</val></doc>", strip(doc));
    }

    @Test
    public void parseInputStreamWithXmlParser() throws IOException, URISyntaxException {
        Document doc = parseResource("/htmltests/xml-test.xml", FOO_BASE);

        assertEquals("<doc><val>One<val>Two</val>Three</val></doc>", strip(doc));
    }

    @Test
    public void htmlVsXmlSelfClosingBehavior() {
        // HTML will force "<br>one</br>" to logically "<br />One<br />". XML should keep "<br>one</br>"
        Document htmlDoc = Jsoup.parse("<br>one</br>");
        assertEquals("<br>\none\n<br>", htmlDoc.body().html());

        Document xmlDoc = xmlDoc("<br>one</br>");
        assertEquals("<br>one</br>", xmlDoc.html());
    }

    @Test
    public void xmlDeclarationIsRecognizedAsDeclaration() {
        String html = "<?xml encoding='UTF-8' ?><body>One</body><!-- comment -->";

        Document doc = xmlDoc(html);

        assertEquals("<?xml encoding=\"UTF-8\"?><body>One</body><!-- comment -->", doc.outerHtml());
        assertEquals("#declaration", doc.childNode(0).nodeName());
        assertEquals("#comment", doc.childNode(2).nodeName());
    }

    @Test
    public void parseXmlFragment_parsesNodesAndBaseUri() {
        String xml = "<one src='/foo/' />Two<three><four /></three>";

        List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com/");

        assertEquals(3, nodes.size());
        assertEquals("http://example.com/foo/", nodes.get(0).absUrl("src"));
        assertEquals("one", nodes.get(0).nodeName());
        assertEquals("Two", ((TextNode) nodes.get(1)).text());
    }

    @Test
    public void xmlOutputSyntaxByDefault() {
        Document doc = Jsoup.parse("x", "", Parser.xmlParser());

        assertEquals(Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void handlesEOFInsideTagAttributes() {
        String html = "<img src=asdf onerror=\"alert(1)\" x=";

        Document xmlDoc = xmlDoc(html);

        assertEquals("<img src=\"asdf\" onerror=\"alert(1)\" x=\"\"></img>", xmlDoc.html());
    }

    // ---------- Declarations ----------
    @Test
    public void detectsCharsetFromXmlDeclaration() throws IOException, URISyntaxException {
        Document doc = parseResource("/htmltests/xml-charset.xml", "http://example.com/");

        assertEquals("ISO-8859-1", doc.charset().name());
        assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><data>äöåéü</data>", strip(doc));
    }

    @Test
    public void parsesDeclarationAttributes() {
        String xml = "<?xml version='1' encoding='UTF-8' something='else'?><val>One</val>";

        Document doc = xmlDoc(xml);
        XmlDeclaration decl = (XmlDeclaration) doc.childNode(0);

        assertEquals("1", decl.attr("version"));
        assertEquals("UTF-8", decl.attr("encoding"));
        assertEquals("else", decl.attr("something"));
        assertEquals("version=\"1\" encoding=\"UTF-8\" something=\"else\"", decl.getWholeDeclaration());
        assertEquals("<?xml version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", decl.outerHtml());
    }

    @Test
    public void parsesProcessingInstructionWithoutAttributes() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<?myProcessingInstruction My Processing instruction.?>";

        Document doc = xmlDoc(xml);
        XmlDeclaration decl = (XmlDeclaration) doc.childNode(2);

        assertEquals("myProcessingInstruction", decl.name());
        assertTrue(decl.hasAttr("My"));
        assertEquals("<?myProcessingInstruction My Processing instruction.?>", decl.outerHtml());
    }

    @Test
    public void preservesCaseInXmlDeclaration() {
        String xml = "<?XML version='1' encoding='UTF-8' something='else'?>";

        Document doc = xmlDoc(xml);

        assertEquals("<?XML version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", doc.outerHtml());
    }

    @Test
    public void createsValidPrologForXmlSyntax() {
        Document document = Document.createShell("");
        document.outputSettings().syntax(Syntax.xml);
        document.charset(StandardCharsets.UTF_8);

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<html>\n" +
            " <head></head>\n" +
            " <body></body>\n" +
            "</html>", document.outerHtml());
    }

    // ---------- Case sensitivity and formatting ----------
    @Test
    public void preservesCaseInTagsAndAttributesByDefault() {
        String xml = "<CHECK>One</CHECK><TEST ID=1>Check</TEST>";

        Document doc = xmlDoc(xml);

        assertEquals("<CHECK>One</CHECK><TEST ID=\"1\">Check</TEST>", strip(doc));
    }

    @Test
    public void appendPreservesCaseByDefaultNoNormalization() {
        String xml = "<One>One</One>";

        Document doc = xmlDoc(xml);
        doc.select("One").append("<Two ID=2>Two</Two>");

        assertEquals("<One>One<Two ID=\"2\">Two</Two></One>", strip(doc));
    }

    @Test
    public void noPrettyPrintingByDefault() {
        String xml = "\n\n<div><one>One</one><one>\n Two</one>\n</div>\n ";

        Document doc = xmlDoc(xml);

        assertEquals(xml, doc.html());
    }

    @Test
    public void canNormalizeCaseWhenUsingHtmlParseSettings() {
        String xml = "<TEST ID=1>Check</TEST>";

        Document doc = Jsoup.parse(xml, "", Parser.xmlParser().settings(ParseSettings.htmlDefault));

        assertEquals("<test id=\"1\">Check</test>", strip(doc));
    }

    @Test
    public void normalizesMismatchedCloseTagsWithHtmlSettings() {
        Parser parser = Parser.xmlParser().settings(ParseSettings.htmlDefault);

        Document document = Jsoup.parse("<div>test</DIV><p></p>", "", parser);

        assertEquals("<div>test</div><p></p>", document.html());
    }

    // ---------- CDATA ----------
    @Test
    public void roundTripsCdataAsTextNode() {
        String xml = "<div id=1><![CDATA[\n<html>\n <foo><&amp;]]></div>";

        Document doc = xmlDoc(xml);
        Element div = doc.getElementById("1");

        assertEquals("<html>\n <foo><&amp;", div.text());
        assertEquals(0, div.children().size());
        assertEquals(1, div.childNodeSize());
        assertEquals("<div id=\"1\"><![CDATA[\n<html>\n <foo><&amp;]]></div>", div.outerHtml());

        CDataNode cdata = (CDataNode) div.textNodes().get(0);
        assertEquals("\n<html>\n <foo><&amp;", cdata.text());
    }

    @Test
    public void cdataPreservesWhitespace() {
        String xml = "<script type=\"text/javascript\">//<![CDATA[\n\n  foo();\n//]]></script>";

        Document doc = xmlDoc(xml);

        assertEquals(xml, doc.outerHtml());
        assertEquals("//\n\n  foo();\n//", doc.selectFirst("script").text());
    }

    // ---------- Edge cases ----------
    @Test
    public void toleratesDodgyXmlDecl() {
        String xml = "<?xml version='1.0'><val>One</val>";

        Document doc = xmlDoc(xml);

        assertEquals("One", doc.select("val").text());
    }

    @Test
    public void convertsPseudoXmlDeclInsideScriptToComment() {
        // https://github.com/jhy/jsoup/issues/1139
        String html = "<script> var a=\"<?\"; var b=\"?>\"; </script>";

        Document doc = xmlDoc(html);

        assertEquals("<script> var a=\"<!--?\"; var b=\"?-->\"; </script>", doc.html());
    }

    @Test
    public void dropsDuplicateAttributesCaseSensitive() {
        // Case sensitive, so should drop Four and Five
        String html = "<p One=One ONE=Two one=Three One=Four ONE=Five two=Six two=Seven Two=Eight>Text</p>";

        Parser parser = Parser.xmlParser().setTrackErrors(10);
        Document doc = parser.parseInput(html, "");

        assertEquals("<p One=\"One\" ONE=\"Two\" one=\"Three\" two=\"Six\" Two=\"Eight\">Text</p>", doc.selectFirst("p").outerHtml());
    }

    @Test
    public void readerAndTokeniserAreClearedAfterParse() {
        Document doc = xmlDoc("Hello");
        TreeBuilder treeBuilder = doc.parser().getTreeBuilder();

        assertNull(treeBuilder.reader);
        assertNull(treeBuilder.tokeniser);
    }

    @Test
    public void xmlParserSetsXmlSyntaxAndXhtmlEscapeMode() {
        Document doc = xmlDoc("<root/>");

        assertEquals(Syntax.xml, doc.outputSettings().syntax());
        assertEquals(Entities.EscapeMode.xhtml, doc.outputSettings().escapeMode());
    }

    @Test
    public void xmlSyntaxAlwaysEscapesLtAndGtInAttributeValues() {
        // https://github.com/jhy/jsoup/issues/2337
        Document doc = xmlDoc("<p one='&lt;two&gt;'>Three</p>");
        doc.outputSettings().escapeMode(Entities.EscapeMode.extended);

        assertEquals(Syntax.xml, doc.outputSettings().syntax());
        assertEquals("<p one=\"&lt;two&gt;\">Three</p>", doc.html());
    }

    @Test
    public void xmlOutputCoercesInvalidAttributeNames() {
        String xml = "<body style=\"color: red\" \" name\"><div =\"\"></div></body>";

        Document doc = xmlDoc(xml);

        assertEquals(Syntax.xml, doc.outputSettings().syntax());
        assertEquals("<body style=\"color: red\" _=\"\" name_=\"\"><div _=\"\"></div></body>", doc.html());
    }

    @Test
    public void xmlOutputAllowsValidAttributeNames() {
        String xml = "<a bB1-_:.=foo _9!=bar xmlns:p1=qux>One</a>";

        Document doc = xmlDoc(xml);

        assertEquals(Syntax.xml, doc.outputSettings().syntax());
        assertEquals("<a bB1-_:.=\"foo\" _9_=\"bar\" xmlns:p1=\"qux\">One</a>", doc.html()); // first is same, second coerced
    }

    @Test
    public void customTagsFromTagNameAreFlyweights() {
        String xml = "<foo>Foo</foo><foo>Foo</foo><FOO>FOO</FOO><FOO>FOO</FOO>";

        Document doc = xmlDoc(xml);
        Elements els = doc.children();

        Tag t1 = els.get(0).tag();
        Tag t2 = els.get(1).tag();
        Tag t3 = els.get(2).tag();
        Tag t4 = els.get(3).tag();

        assertEquals("foo", t1.getName());
        assertEquals("FOO", t3.getName());
        assertSame(t1, t2);
        assertSame(t3, t4);
    }

    @Test
    public void parserSettingsDefaultToXml() {
        Document doc = Jsoup.parse("<foo>", Parser.xmlParser());
        ParseSettings settings = doc.parser().settings();

        assertTrue(settings.preserveTagCase());
        assertTrue(settings.preserveAttributeCase());
        assertEquals(NamespaceXml, doc.parser().defaultNamespace());
    }

    // ---------- Namespaces ----------
    @Test
    public void xmlNamespace() {
        String xml = "<foo><bar><div><svg><math>Qux</bar></foo>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        assertXmlNamespace(doc);

        Elements els = doc.select("*");
        for (Element el : els) {
            assertXmlNamespace(el);
        }

        Document clone = doc.clone();
        assertXmlNamespace(clone);
        assertXmlNamespace(clone.expectFirst("bar"));

        Document shallow = doc.shallowClone();
        assertXmlNamespace(shallow);
    }

    private static void assertXmlNamespace(Element el) {
        assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    public void parseMultipleDeclarationsAndDocType() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><!DOCTYPE html\n" +
            "  PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
            "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
            "<!ELEMENT footnote (#PCDATA|a)*>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser());

        XmlDeclaration proc = (XmlDeclaration) doc.childNode(0);
        DocumentType doctype = (DocumentType) doc.childNode(1);
        XmlDeclaration decl = (XmlDeclaration) doc.childNode(2);

        assertEquals("xml", proc.name());
        assertEquals("1.0", proc.attr("version"));
        assertEquals("utf-8", proc.attr("encoding"));
        assertEquals("version=\"1.0\" encoding=\"utf-8\"", proc.getWholeDeclaration());
        assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>", proc.outerHtml());

        assertEquals("html", doctype.name());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.attr("systemId"));
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", doctype.outerHtml());

        assertEquals("ELEMENT", decl.name());
        assertEquals("footnote (#PCDATA|a)*", decl.getWholeDeclaration());
        assertTrue(decl.hasAttr("footNote"));
        assertFalse(decl.hasAttr("ELEMENT"));
        assertEquals("<!ELEMENT footnote (#PCDATA|a)*>", decl.outerHtml());

        assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
            "<!ELEMENT footnote (#PCDATA|a)*>", doc.outerHtml());
    }

    @Test
    public void declarationAttributeUnescapesGtLt() {
        // https://github.com/jhy/jsoup/issues/1947
        String xml = "<x><?xmlDeclaration att1=\"value1\" att2=\"&lt;val2>\"?></x>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        XmlDeclaration decl = (XmlDeclaration) doc.expectFirst("x").childNode(0);

        assertEquals("<val2>", decl.attr("att2"));
    }

    @Test
    public void xmlHeaderSpacingAccepted() {
        // https://github.com/jhy/jsoup/issues/2298
        String xml = "<?xml version=\"1.0\"?>\n<root></root>";
        String expect = xml;

        Document doc = Jsoup.parse(xml, Parser.xmlParser().setTrackErrors(10));
        assertEquals(0, doc.parser().getErrors().size());
        assertEquals(expect, doc.html());

        xml = "<?xml version=\"1.0\" ?>\n<root></root>";
        doc = Jsoup.parse(xml, Parser.xmlParser().setTrackErrors(10));
        assertEquals(0, doc.parser().getErrors().size());
        assertEquals(expect, doc.html());
    }

    // ---------- Custom TagSet ----------
    @Test
    public void canSetCustomRcdataTag() {
        String inner = "Blah\nblah\n<foo></foo>&quot;";
        String innerText = "Blah\nblah\n<foo></foo>\"";
        String xml = "<x><y><z>" + inner + "</z></y></x><x><z id=2></z>";

        TagSet custom = new TagSet();
        Tag z = custom.valueOf("z", NamespaceXml, ParseSettings.preserveCase);
        z.set(Tag.RcData);

        Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
        Element zEl = doc.expectFirst("z");

        assertNotSame(z, zEl.tag()); // not same because we copy the tagset
        assertEquals(z, zEl.tag());
        assertEquals(1, zEl.childNodeSize());

        Node child = zEl.childNode(0);
        assertTrue(child instanceof TextNode);
        assertEquals(innerText, ((TextNode) child).getWholeText());

        // fragment context parse - should parse <foo> as text
        Element z2 = doc.expectFirst("#2");
        z2.html(inner);
        assertEquals(innerText, z2.wholeText());
    }

    @Test
    public void canSetCustomDataTag() {
        String inner = "Blah\nblah\n<foo></foo>&quot;"; // no character refs, will be as-is
        String xml = "<x><y><z>" + inner + "</z></y></x><x><z id=2></z>";

        TagSet custom = new TagSet();
        Tag z = custom.valueOf("z", NamespaceXml, ParseSettings.preserveCase);
        z.set(Tag.Data);

        Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
        Element zEl = doc.expectFirst("z");

        assertNotSame(z, zEl.tag());
        assertEquals(z, zEl.tag());
        assertEquals(1, zEl.childNodeSize());

        Node child = zEl.childNode(0);
        assertTrue(child instanceof DataNode);
        assertEquals(inner, ((DataNode) child).getWholeData());
        assertEquals(inner, zEl.data());

        // fragment context parse - should parse <foo> as data
        Element z2 = doc.expectFirst("#2");
        z2.html(inner);
        assertEquals(inner, ((DataNode) child).getWholeData());
        assertEquals(inner, zEl.data());
    }

    @Test
    public void canSetCustomVoidTags() {
        String ns = "custom";
        String xml = "<x xmlns=custom><foo><link><meta>";

        TagSet custom = new TagSet();
        custom.valueOf("link", ns).set(Tag.Void);
        custom.valueOf("meta", ns).set(Tag.Void);
        custom.valueOf("foo", "other").set(Tag.Void); // ns doesn't match, won't impact

        Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
        String expect = "<x xmlns=\"custom\"><foo><link /><meta /></foo></x>";

        assertEquals(expect, doc.html());
    }

    @Test
    public void canUseHtmlTagSetWithXmlParser() {
        // Use the properties of HTML tag set but without HtmlTreeBuilder rules
        String xml = "<html xmlns=" + NamespaceHtml + "><div><script>a<b</script><img><p>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(TagSet.Html()));
        doc.outputSettings().prettyPrint(true);

        String expect = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            " <div>\n" +
            "  <script>//<![CDATA[\n" +
            "a<b\n" +
            "//]]></script>\n" +
            "  <img />\n" +
            "  <p></p>\n" +
            " </div>\n" +
            "</html>";
        assertEquals(expect, doc.html());

        doc.outputSettings().syntax(Syntax.html);
        expect = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            " <div>\n" +
            "  <script>a<b</script>\n" +
            "  <img>\n" +
            "  <p></p>\n" +
            " </div>\n" +
            "</html>";
        assertEquals(expect, doc.html());
    }

    @Test
    public void prettyPrintInlineTextWithinXmlUnlessTagsMarkedBlock() {
        // https://github.com/jhy/jsoup/issues/2141
        String xml = "<package><metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
            "<dc:identifier id=\"pub-id\">id</dc:identifier>\n" +
            "<dc:title>title</dc:title>\n" +
            "<dc:language>ja</dc:language>\n" +
            "<dc:description>desc</dc:description>\n" +
            "</metadata></package>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        doc.outputSettings().prettyPrint(true);

        assertEquals("<package>\n" +
            " <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
            "  <dc:identifier id=\"pub-id\">id</dc:identifier> <dc:title>title</dc:title> <dc:language>ja</dc:language> <dc:description>desc</dc:description>\n" +
            " </metadata>\n" +
            "</package>", doc.html());

        // customize: set inner elements to block
        Element meta = doc.expectFirst("metadata");
        meta.tag().set(Tag.Block);
        for (Element inner : meta) inner.tag().set(Tag.Block);

        assertEquals("<package>\n" +
            " <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
            "  <dc:identifier id=\"pub-id\">id</dc:identifier>\n" +
            "  <dc:title>title</dc:title>\n" +
            "  <dc:language>ja</dc:language>\n" +
            "  <dc:description>desc</dc:description>\n" +
            " </metadata>\n" +
            "</package>", doc.html());
    }

    // ---------- Namespace examples ----------
    @Test
    public void namespaceExamples_fromSpec() {
        // Example from the XML namespace spec https://www.w3.org/TR/xml-names/
        String xml = "<?xml version=\"1.0\"?>\n" +
            "<!-- both namespace prefixes are available throughout -->\n" +
            "<bk:book xmlns:bk=\"urn:loc.gov:books\" xmlns:isbn=\"urn:ISBN:0-395-36341-6\">\n" +
            "    <bk:title>Cheaper by the Dozen</bk:title>\n" +
            "    <isbn:number>1568491379</isbn:number>\n" +
            "</bk:book>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser());

        Element book = doc.expectFirst("bk|book");
        assertEquals("bk:book", book.tag().name());
        assertEquals("bk", book.tag().prefix());
        assertEquals("book", book.tag().localName());
        assertEquals("urn:loc.gov:books", book.tag().namespace());

        Element title = doc.expectFirst("bk|title");
        assertEquals("bk:title", title.tag().name());
        assertEquals("urn:loc.gov:books", title.tag().namespace());

        Element number = doc.expectFirst("isbn|number");
        assertEquals("isbn:number", number.tag().name());
        assertEquals("urn:ISBN:0-395-36341-6", number.tag().namespace());

        // and DOM is unchanged
        assertEquals(xml, doc.html());
    }

    @Test
    public void defaultNamespaceFromXmlnsApplied() {
        String xml = "<?xml version=\"1.0\"?>\n" +
            "<!-- elements are in the HTML namespace, in this case by default -->\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "  <head><title>Frobnostication</title></head>\n" +
            "  <body><p>Moved to \n" +
            "    <a href=\"http://frob.example.com\">here</a>.</p></body>\n" +
            "</html>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Element html = doc.expectFirst("html");
        Element a = doc.expectFirst("a");

        assertEquals(NamespaceHtml, html.tag().namespace());
        assertEquals(NamespaceHtml, a.tag().namespace());
    }

    @Test
    public void emptyDefaultNamespaceBehaviorWithinHtmlNamespacedChildren() {
        String xml = "<?xml version='1.0'?>\n" +
            "<Beers>\n" +
            "  <!-- the default namespace inside tables is that of HTML -->\n" +
            "  <table xmlns='http://www.w3.org/1999/xhtml'>\n" +
            "   <th><td>Name</td><td>Origin</td><td>Description</td></th>\n" +
            "   <tr> \n" +
            "     <!-- no default namespace inside table cells -->\n" +
            "     <td><brandName xmlns=\"\">Huntsman</brandName></td>\n" +
            "     <td><origin xmlns=\"\">Bath, UK</origin></td>\n" +
            "     <td>\n" +
            "       <details xmlns=\"\"><class>Bitter</class><hop>Fuggles</hop>\n" +
            "         <pro>Wonderful hop, light alcohol, good summer beer</pro>\n" +
            "         <con>Fragile; excessive variance pub to pub</con>\n" +
            "         </details>\n" +
            "        </td>\n" +
            "      </tr>\n" +
            "    </table>\n" +
            "  </Beers>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser());

        assertEquals(NamespaceXml, doc.expectFirst("Beers").tag().namespace());
        assertEquals(NamespaceHtml, doc.expectFirst("td").tag().namespace());
        assertEquals("", doc.expectFirst("origin").tag().namespace());
        assertEquals("", doc.expectFirst("pro").tag().namespace());
    }

    @Test
    public void attributeWithNamespacePrefixHasNamespace() {
        String xml = "<x xmlns:edi='http://ecommerce.example.org/schema'>\n" +
            "  <!-- the 'taxClass' attribute's namespace is http://ecommerce.example.org/schema -->\n" +
            "  <lineItem edi:taxClass=\"exempt\" other=foo>Baby food</lineItem>\n" +
            "</x>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Element lineItem = doc.expectFirst("lineItem");

        Attribute taxClass = lineItem.attribute("edi:taxClass");
        assertNotNull(taxClass);
        assertEquals("edi", taxClass.prefix());
        assertEquals("taxClass", taxClass.localName());
        assertEquals("http://ecommerce.example.org/schema", taxClass.namespace());

        Attribute other = lineItem.attribute("other");
        assertNotNull(other);
        assertEquals("foo", other.getValue());
        assertEquals("", other.prefix());
        assertEquals("other", other.localName());
        assertEquals("", other.namespace());
    }

    @Test
    public void fragmentParsedElementsInheritNamespaceStack() {
        // When elements/attributes are added via a fragment parse, they inherit the namespace stack, and can override it
        String xml = "<out xmlns='/out'><bk:book xmlns:bk='/books' xmlns:edi='/edi'><bk:title>Test</bk:title><li edi:foo='bar'></bk:book></out>";

        Document doc = Jsoup.parse(xml, Parser.xmlParser());

        // insert parsed xml, inherit bk and edi, and with an inner node override bk
        Element book = doc.expectFirst("bk|book");
        book.append("<bk:content edi:foo=qux>Content</bk:content>");

        Element out = doc.expectFirst("out");
        assertEquals("/out", out.tag().namespace());

        Element content = book.expectFirst("bk|content");
        assertEquals("bk:content", content.tag().name());
        assertEquals("/books", content.tag().namespace());
        assertEquals("/edi", content.attribute("edi:foo").namespace());

        content.append("<data>Data</data><html xmlns='/html' xmlns:bk='/update'><p>Foo</p><bk:news>News</bk:news></html>");
        // p should be in /html, news in /update, data reverts to /out
        Element p = content.expectFirst("p");
        assertEquals("/html", p.tag().namespace());
        Element news = content.expectFirst("bk|news");
        assertEquals("/update", news.tag().namespace());
        Element data = content.expectFirst("data");
        assertEquals("/out", data.tag().namespace());
    }

    // ---------- Self-closing ----------
    @Test
    public void xmlAllowsSelfClosingOnAnyElement() {
        Parser parser = Parser.xmlParser().setTrackErrors(10);
        String xml = "<div id='1'/><p/><div>Foo</div><div></div><foo></foo>";

        Document doc = Jsoup.parse(xml, "", parser);
        ParseErrorList errors = parser.getErrors();

        assertEquals(0, errors.size());
        assertEquals("<div id=\"1\" /><p /><div>Foo</div><div /><foo></foo>", strip(doc.outerHtml()));
    }
}