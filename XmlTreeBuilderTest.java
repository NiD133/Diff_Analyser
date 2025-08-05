package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
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
 * Unit tests for the XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void parsesSimpleXml() {
        String xml = "<doc id=2 href='/bar'>Foo <br /><link>One</link><link>Two</link></doc>";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = treeBuilder.parse(xml, "http://foo.com/");
        
        assertEquals("<doc id=\"2\" href=\"/bar\">Foo <br /><link>One</link><link>Two</link></doc>",
                TextUtil.stripNewlines(document.html()));
        assertEquals("http://foo.com/bar", document.getElementById("2").absUrl("href"));
    }

    @Test
    public void handlesNestedTagsCorrectly() {
        String xml = "<doc><val>One<val>Two</val></bar>Three</doc>";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = treeBuilder.parse(xml, "http://foo.com/");
        
        assertEquals("<doc><val>One<val>Two</val>Three</val></doc>",
                TextUtil.stripNewlines(document.html()));
    }

    @Test
    public void parsesCommentsAndDocTypes() {
        String xml = "<!DOCTYPE HTML><!-- a comment -->One <qux />Two";
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = treeBuilder.parse(xml, "http://foo.com/");
        
        assertEquals("<!DOCTYPE HTML><!-- a comment -->One <qux />Two",
                TextUtil.stripNewlines(document.html()));
    }

    @Test
    public void usesSuppliedParserWithJsoup() {
        String xml = "<doc><val>One<val>Two</val></bar>Three</doc>";
        Document document = Jsoup.parse(xml, "http://foo.com/", Parser.xmlParser());
        
        assertEquals("<doc><val>One<val>Two</val>Three</val></doc>",
                TextUtil.stripNewlines(document.html()));
    }

    @Test
    public void parsesXmlFromDataStream() throws IOException, URISyntaxException {
        File xmlFile = new File(XmlTreeBuilder.class.getResource("/htmltests/xml-test.xml").toURI());
        try (InputStream inputStream = new FileInputStream(xmlFile)) {
            Document document = Jsoup.parse(inputStream, null, "http://foo.com", Parser.xmlParser());
            assertEquals("<doc><val>One<val>Two</val>Three</val></doc>",
                    TextUtil.stripNewlines(document.html()));
        }
    }

    @Test
    public void doesNotForceSelfClosingTagsInXml() {
        Document htmlDoc = Jsoup.parse("<br>one</br>");
        assertEquals("<br>\none\n<br>", htmlDoc.body().html());

        Document xmlDoc = Jsoup.parse("<br>one</br>", "", Parser.xmlParser());
        assertEquals("<br>one</br>", xmlDoc.html());
    }

    @Test
    public void handlesXmlDeclarationCorrectly() {
        String xml = "<?xml encoding='UTF-8' ?><body>One</body><!-- comment -->";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        
        assertEquals("<?xml encoding=\"UTF-8\"?><body>One</body><!-- comment -->", document.outerHtml());
        assertEquals("#declaration", document.childNode(0).nodeName());
        assertEquals("#comment", document.childNode(2).nodeName());
    }

    @Test
    public void parsesXmlFragment() {
        String xml = "<one src='/foo/' />Two<three><four /></three>";
        List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com/");
        
        assertEquals(3, nodes.size());
        assertEquals("http://example.com/foo/", nodes.get(0).absUrl("src"));
        assertEquals("one", nodes.get(0).nodeName());
        assertEquals("Two", ((TextNode) nodes.get(1)).text());
    }

    @Test
    public void defaultsToHtmlOutputSyntax() {
        Document document = Jsoup.parse("x", "", Parser.xmlParser());
        assertEquals(Syntax.xml, document.outputSettings().syntax());
    }

    @Test
    public void handlesEOFInTag() {
        String html = "<img src=asdf onerror=\"alert(1)\" x=";
        Document xmlDoc = Jsoup.parse(html, "", Parser.xmlParser());
        
        assertEquals("<img src=\"asdf\" onerror=\"alert(1)\" x=\"\"></img>", xmlDoc.html());
    }

    @Test
    public void detectsCharsetEncodingDeclaration() throws IOException, URISyntaxException {
        File xmlFile = new File(XmlTreeBuilder.class.getResource("/htmltests/xml-charset.xml").toURI());
        try (InputStream inputStream = new FileInputStream(xmlFile)) {
            Document document = Jsoup.parse(inputStream, null, "http://example.com/", Parser.xmlParser());
            assertEquals("ISO-8859-1", document.charset().name());
            assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><data>äöåéü</data>",
                    TextUtil.stripNewlines(document.html()));
        }
    }

    @Test
    public void parsesDeclarationAttributes() {
        String xml = "<?xml version='1' encoding='UTF-8' something='else'?><val>One</val>";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        XmlDeclaration declaration = (XmlDeclaration) document.childNode(0);
        
        assertEquals("1", declaration.attr("version"));
        assertEquals("UTF-8", declaration.attr("encoding"));
        assertEquals("else", declaration.attr("something"));
        assertEquals("version=\"1\" encoding=\"UTF-8\" something=\"else\"", declaration.getWholeDeclaration());
        assertEquals("<?xml version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", declaration.outerHtml());
    }

    @Test
    public void parsesDeclarationWithoutAttributes() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<?myProcessingInstruction My Processing instruction.?>";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        XmlDeclaration declaration = (XmlDeclaration) document.childNode(2);
        
        assertEquals("myProcessingInstruction", declaration.name());
        assertTrue(declaration.hasAttr("My"));
        assertEquals("<?myProcessingInstruction My Processing instruction.?>", declaration.outerHtml());
    }

    @Test
    public void handlesCaseSensitiveDeclaration() {
        String xml = "<?XML version='1' encoding='UTF-8' something='else'?>";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        
        assertEquals("<?XML version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", document.outerHtml());
    }

    @Test
    public void createsValidProlog() {
        Document document = Document.createShell("");
        document.outputSettings().syntax(Syntax.xml);
        document.charset(StandardCharsets.UTF_8);
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                " <head></head>\n" +
                " <body></body>\n" +
                "</html>", document.outerHtml());
    }

    @Test
    public void preservesCaseByDefault() {
        String xml = "<CHECK>One</CHECK><TEST ID=1>Check</TEST>";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        
        assertEquals("<CHECK>One</CHECK><TEST ID=\"1\">Check</TEST>", TextUtil.stripNewlines(document.html()));
    }

    @Test
    public void appendPreservesCaseByDefault() {
        String xml = "<One>One</One>";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        Elements one = document.select("One");
        one.append("<Two ID=2>Two</Two>");
        
        assertEquals("<One>One<Two ID=\"2\">Two</Two></One>", TextUtil.stripNewlines(document.html()));
    }

    @Test
    public void disablesPrettyPrintingByDefault() {
        String xml = "\n\n<div><one>One</one><one>\n Two</one>\n</div>\n ";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        
        assertEquals(xml, document.html());
    }

    @Test
    public void canNormalizeCase() {
        String xml = "<TEST ID=1>Check</TEST>";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser().settings(ParseSettings.htmlDefault));
        
        assertEquals("<test id=\"1\">Check</test>", TextUtil.stripNewlines(document.html()));
    }

    @Test
    public void normalizesDiscordantTags() {
        Parser parser = Parser.xmlParser().settings(ParseSettings.htmlDefault);
        Document document = Jsoup.parse("<div>test</DIV><p></p>", "", parser);
        
        assertEquals("<div>test</div><p></p>", document.html());
    }

    @Test
    public void roundTripsCdata() {
        String xml = "<div id=1><![CDATA[\n<html>\n <foo><&amp;]]></div>";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());

        Element div = document.getElementById("1");
        assertEquals("<html>\n <foo><&amp;", div.text());
        assertEquals(0, div.children().size());
        assertEquals(1, div.childNodeSize());

        assertEquals("<div id=\"1\"><![CDATA[\n<html>\n <foo><&amp;]]></div>", div.outerHtml());

        CDataNode cdata = (CDataNode) div.textNodes().get(0);
        assertEquals("\n<html>\n <foo><&amp;", cdata.text());
    }

    @Test
    public void cdataPreservesWhiteSpace() {
        String xml = "<script type=\"text/javascript\">//<![CDATA[\n\n  foo();\n//]]></script>";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        
        assertEquals(xml, document.outerHtml());
        assertEquals("//\n\n  foo();\n//", document.selectFirst("script").text());
    }

    @Test
    public void handlesDodgyXmlDeclaration() {
        String xml = "<?xml version='1.0'><val>One</val>";
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        
        assertEquals("One", document.select("val").text());
    }

    @Test
    public void handlesLTinScript() {
        String html = "<script> var a=\"<?\"; var b=\"?>\"; </script>";
        Document document = Jsoup.parse(html, "", Parser.xmlParser());
        
        assertEquals("<script> var a=\"<!--?\"; var b=\"?-->\"; </script>", document.html());
    }

    @Test
    public void dropsDuplicateAttributes() {
        String html = "<p One=One ONE=Two one=Three One=Four ONE=Five two=Six two=Seven Two=Eight>Text</p>";
        Parser parser = Parser.xmlParser().setTrackErrors(10);
        Document document = parser.parseInput(html, "");

        assertEquals("<p One=\"One\" ONE=\"Two\" one=\"Three\" two=\"Six\" Two=\"Eight\">Text</p>", document.selectFirst("p").outerHtml());
    }

    @Test
    public void readerClosedAfterParse() {
        Document document = Jsoup.parse("Hello", "", Parser.xmlParser());
        TreeBuilder treeBuilder = document.parser().getTreeBuilder();
        
        assertNull(treeBuilder.reader);
        assertNull(treeBuilder.tokeniser);
    }

    @Test
    public void xmlParserEnablesXmlOutputAndEscapes() {
        Document document = Jsoup.parse("<root/>", "", Parser.xmlParser());
        
        assertEquals(Syntax.xml, document.outputSettings().syntax());
        assertEquals(Entities.EscapeMode.xhtml, document.outputSettings().escapeMode());
    }

    @Test
    public void xmlSyntaxEscapesLtAndGtInAttributes() {
        Document document = Jsoup.parse("<p one='&lt;two&gt;'>Three</p>", "", Parser.xmlParser());
        document.outputSettings().escapeMode(Entities.EscapeMode.extended);
        
        assertEquals(Syntax.xml, document.outputSettings().syntax());
        assertEquals("<p one=\"&lt;two&gt;\">Three</p>", document.html());
    }

    @Test
    public void xmlOutputCorrectsInvalidAttributeNames() {
        String xml = "<body style=\"color: red\" \" name\"><div =\"\"></div></body>";
        Document document = Jsoup.parse(xml, Parser.xmlParser());
        
        assertEquals(Syntax.xml, document.outputSettings().syntax());
        assertEquals("<body style=\"color: red\" _=\"\" name_=\"\"><div _=\"\"></div></body>", document.html());
    }

    @Test
    public void xmlValidAttributes() {
        String xml = "<a bB1-_:.=foo _9!=bar xmlns:p1=qux>One</a>";
        Document document = Jsoup.parse(xml, Parser.xmlParser());
        
        assertEquals(Syntax.xml, document.outputSettings().syntax());
        assertEquals("<a bB1-_:.=\"foo\" _9_=\"bar\" xmlns:p1=\"qux\">One</a>", document.html());
    }

    @Test
    public void customTagsAreFlyweights() {
        String xml = "<foo>Foo</foo><foo>Foo</foo><FOO>FOO</FOO><FOO>FOO</FOO>";
        Document document = Jsoup.parse(xml, Parser.xmlParser());
        Elements elements = document.children();

        Tag tag1 = elements.get(0).tag();
        Tag tag2 = elements.get(1).tag();
        Tag tag3 = elements.get(2).tag();
        Tag tag4 = elements.get(3).tag();
        
        assertEquals("foo", tag1.getName());
        assertEquals("FOO", tag3.getName());
        assertSame(tag1, tag2);
        assertSame(tag3, tag4);
    }

    @Test
    public void rootHasXmlSettings() {
        Document document = Jsoup.parse("<foo>", Parser.xmlParser());
        ParseSettings settings = document.parser().settings();
        
        assertTrue(settings.preserveTagCase());
        assertTrue(settings.preserveAttributeCase());
        assertEquals(NamespaceXml, document.parser().defaultNamespace());
    }

    @Test
    public void xmlNamespace() {
        String xml = "<foo><bar><div><svg><math>Qux</bar></foo>";
        Document document = Jsoup.parse(xml, Parser.xmlParser());

        assertXmlNamespace(document);
        Elements elements = document.select("*");
        for (Element element : elements) {
            assertXmlNamespace(element);
        }

        Document clone = document.clone();
        assertXmlNamespace(clone);
        assertXmlNamespace(clone.expectFirst("bar"));

        Document shallowClone = document.shallowClone();
        assertXmlNamespace(shallowClone);
    }

    private static void assertXmlNamespace(Element element) {
        assertEquals(NamespaceXml, element.tag().namespace(), String.format("Element %s not in XML namespace", element.tagName()));
    }

    @Test
    public void declarations() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><!DOCTYPE html\n" +
                "  PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
                "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<!ELEMENT footnote (#PCDATA|a)*>";
        Document document = Jsoup.parse(xml, Parser.xmlParser());

        XmlDeclaration proc = (XmlDeclaration) document.childNode(0);
        DocumentType doctype = (DocumentType) document.childNode(1);
        XmlDeclaration decl = (XmlDeclaration) document.childNode(2);

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
                "<!ELEMENT footnote (#PCDATA|a)*>", document.outerHtml());
    }

    @Test
    public void declarationWithGt() {
        String xml = "<x><?xmlDeclaration att1=\"value1\" att2=\"&lt;val2>\"?></x>";
        Document document = Jsoup.parse(xml, Parser.xmlParser());
        XmlDeclaration declaration = (XmlDeclaration) document.expectFirst("x").childNode(0);
        
        assertEquals("<val2>", declaration.attr("att2"));
    }

    @Test
    public void xmlHeaderIsValid() {
        String xml = "<?xml version=\"1.0\"?>\n<root></root>";
        String expected = xml;

        Document document = Jsoup.parse(xml, Parser.xmlParser().setTrackErrors(10));
        assertEquals(0, document.parser().getErrors().size());
        assertEquals(expected, document.html());

        xml = "<?xml version=\"1.0\" ?>\n<root></root>";
        document = Jsoup.parse(xml, Parser.xmlParser().setTrackErrors(10));
        assertEquals(0, document.parser().getErrors().size());
        assertEquals(expected, document.html());
    }

    @Test
    public void canSetCustomRcdataTag() {
        String inner = "Blah\nblah\n<foo></foo>&quot;";
        String innerText = "Blah\nblah\n<foo></foo>\"";

        String xml = "<x><y><z>" + inner + "</z></y></x><x><z id=2></z>";
        TagSet custom = new TagSet();
        Tag z = custom.valueOf("z", NamespaceXml, ParseSettings.preserveCase);
        z.set(Tag.RcData);

        Document document = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
        Element zElement = document.expectFirst("z");
        assertNotSame(z, zElement.tag());
        assertEquals(z, zElement.tag());

        assertEquals(1, zElement.childNodeSize());
        Node child = zElement.childNode(0);
        assertTrue(child instanceof TextNode);
        assertEquals(innerText, ((TextNode) child).getWholeText());

        Element z2 = document.expectFirst("#2");
        z2.html(inner);
        assertEquals(innerText, z2.wholeText());
    }

    @Test
    public void canSetCustomDataTag() {
        String inner = "Blah\nblah\n<foo></foo>&quot;";

        String xml = "<x><y><z>" + inner + "</z></y></x><x><z id=2></z>";
        TagSet custom = new TagSet();
        Tag z = custom.valueOf("z", NamespaceXml, ParseSettings.preserveCase);
        z.set(Tag.Data);

        Document document = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
        Element zElement = document.expectFirst("z");
        assertNotSame(z, zElement.tag());
        assertEquals(z, zElement.tag());

        assertEquals(1, zElement.childNodeSize());
        Node child = zElement.childNode(0);
        assertTrue(child instanceof DataNode);
        assertEquals(inner, ((DataNode) child).getWholeData());
        assertEquals(inner, zElement.data());

        Element z2 = document.expectFirst("#2");
        z2.html(inner);
        assertEquals(inner, ((DataNode) child).getWholeData());
        assertEquals(inner, zElement.data());
    }

    @Test
    public void canSetCustomVoid() {
        String ns = "custom";
        String xml = "<x xmlns=custom><foo><link><meta>";
        TagSet custom = new TagSet();
        custom.valueOf("link", ns).set(Tag.Void);
        custom.valueOf("meta", ns).set(Tag.Void);
        custom.valueOf("foo", "other").set(Tag.Void);

        Document document = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
        String expected = "<x xmlns=\"custom\"><foo><link /><meta /></foo></x>";
        assertEquals(expected, document.html());
    }

    @Test
    public void canSupplyWithHtmlTagSet() {
        String xml = "<html xmlns=" + NamespaceHtml + "><div><script>a<b</script><img><p>";
        Document document = Jsoup.parse(xml, Parser.xmlParser().tagSet(TagSet.Html()));
        document.outputSettings().prettyPrint(true);
        String expected = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                " <div>\n" +
                "  <script>//<![CDATA[\n" +
                "a<b\n" +
                "//]]></script>\n" +
                "  <img />\n" +
                "  <p></p>\n" +
                " </div>\n" +
                "</html>";
        assertEquals(expected, document.html());

        document.outputSettings().syntax(Syntax.html);
        expected = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                " <div>\n" +
                "  <script>a<b</script>\n" +
                "  <img>\n" +
                "  <p></p>\n" +
                " </div>\n" +
                "</html>";
        assertEquals(expected, document.html());
    }

    @Test
    public void prettyFormatsTextInline() {
        String xml = "<package><metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "<dc:identifier id=\"pub-id\">id</dc:identifier>\n" +
                "<dc:title>title</dc:title>\n" +
                "<dc:language>ja</dc:language>\n" +
                "<dc:description>desc</dc:description>\n" +
                "</metadata></package>";
        Document document = Jsoup.parse(xml, Parser.xmlParser());
        document.outputSettings().prettyPrint(true);
        assertEquals("<package>\n" +
                " <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "  <dc:identifier id=\"pub-id\">id</dc:identifier> <dc:title>title</dc:title> <dc:language>ja</dc:language> <dc:description>desc</dc:description>\n" +
                " </metadata>\n" +
                "</package>", document.html());

        Element metadata = document.expectFirst("metadata");
        Tag metadataTag = metadata.tag();
        metadataTag.set(Tag.Block);
        for (Element inner : metadata) inner.tag().set(Tag.Block);

        assertEquals("<package>\n" +
                " <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "  <dc:identifier id=\"pub-id\">id</dc:identifier>\n" +
                "  <dc:title>title</dc:title>\n" +
                "  <dc:language>ja</dc:language>\n" +
                "  <dc:description>desc</dc:description>\n" +
                " </metadata>\n" +
                "</package>", document.html());
    }

    @Test
    public void xmlns() {
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<!-- both namespace prefixes are available throughout -->\n" +
                "<bk:book xmlns:bk=\"urn:loc.gov:books\" xmlns:isbn=\"urn:ISBN:0-395-36341-6\">\n" +
                "    <bk:title>Cheaper by the Dozen</bk:title>\n" +
                "    <isbn:number>1568491379</isbn:number>\n" +
                "</bk:book>";
        Document document = Jsoup.parse(xml, Parser.xmlParser());

        Element book = document.expectFirst("bk|book");
        assertEquals("bk:book", book.tag().name());
        assertEquals("bk", book.tag().prefix());
        assertEquals("book", book.tag().localName());
        assertEquals("urn:loc.gov:books", book.tag().namespace());

        Element title = document.expectFirst("bk|title");
        assertEquals("bk:title", title.tag().name());
        assertEquals("urn:loc.gov:books", title.tag().namespace());

        Element number = document.expectFirst("isbn|number");
        assertEquals("isbn:number", number.tag().name());
        assertEquals("urn:ISBN:0-395-36341-6", number.tag().namespace());

        assertEquals(xml, document.html());
    }

    @Test
    public void unprefixedDefaults() {
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<!-- elements are in the HTML namespace, in this case by default -->\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "  <head><title>Frobnostication</title></head>\n" +
                "  <body><p>Moved to \n" +
                "    <a href=\"http://frob.example.com\">here</a>.</p></body>\n" +
                "</html>";

        Document document = Jsoup.parse(xml, Parser.xmlParser());
        Element html = document.expectFirst("html");
        assertEquals(NamespaceHtml, html.tag().namespace());
        Element a = document.expectFirst("a");
        assertEquals(NamespaceHtml, a.tag().namespace());
    }

    @Test
    public void emptyDefault() {
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

        Document document = Jsoup.parse(xml, Parser.xmlParser());
        Element beers = document.expectFirst("Beers");
        assertEquals(NamespaceXml, beers.tag().namespace());
        Element td = document.expectFirst("td");
        assertEquals(NamespaceHtml, td.tag().namespace());
        Element origin = document.expectFirst("origin");
        assertEquals("", origin.tag().namespace());
        Element pro = document.expectFirst("pro");
        assertEquals("", pro.tag().namespace());
    }

    @Test
    public void namespacedAttribute() {
        String xml = "<x xmlns:edi='http://ecommerce.example.org/schema'>\n" +
                "  <!-- the 'taxClass' attribute's namespace is http://ecommerce.example.org/schema -->\n" +
                "  <lineItem edi:taxClass=\"exempt\" other=foo>Baby food</lineItem>\n" +
                "</x>";

        Document document = Jsoup.parse(xml, Parser.xmlParser());
        Element lineItem = document.expectFirst("lineItem");

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
    public void elementsViaAppendHtmlAreNamespaced() {
        String xml = "<out xmlns='/out'><bk:book xmlns:bk='/books' xmlns:edi='/edi'><bk:title>Test</bk:title><li edi:foo='bar'></bk:book></out>";
        Document document = Jsoup.parse(xml, Parser.xmlParser());

        Element book = document.expectFirst("bk|book");
        book.append("<bk:content edi:foo=qux>Content</bk:content>");

        Element out = document.expectFirst("out");
        assertEquals("/out", out.tag().namespace());

        Element content = book.expectFirst("bk|content");
        assertEquals("bk:content", content.tag().name());
        assertEquals("/books", content.tag().namespace());
        assertEquals("/edi", content.attribute("edi:foo").namespace());

        content.append("<data>Data</data><html xmlns='/html' xmlns:bk='/update'><p>Foo</p><bk:news>News</bk:news></html>");
        Element p = content.expectFirst("p");
        assertEquals("/html", p.tag().namespace());
        Element news = content.expectFirst("bk|news");
        assertEquals("/update", news.tag().namespace());
        Element data = content.expectFirst("data");
        assertEquals("/out", data.tag().namespace());
    }

    @Test
    public void selfClosingOK() {
        Parser parser = Parser.xmlParser().setTrackErrors(10);
        String xml = "<div id='1'/><p/><div>Foo</div><div></div><foo></foo>";
        Document document = Jsoup.parse(xml, "", parser);
        ParseErrorList errors = parser.getErrors();
        
        assertEquals(0, errors.size());
        assertEquals("<div id=\"1\" /><p /><div>Foo</div><div /><foo></foo>", TextUtil.stripNewlines(document.outerHtml()));
    }
}