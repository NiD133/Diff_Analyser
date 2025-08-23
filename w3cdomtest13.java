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

public class W3CDomTestTest13 {

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
    public void testRoundTripDoctype() {
        // because we have Saxon on the test classpath, the transformer will change to that, and so case may change (e.g. Java base is META, Saxon is meta for HTML)
        String base = "<!DOCTYPE html><p>One</p>";
        assertEqualsIgnoreCase("<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>", output(base, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><p>One</p></body></html>", output(base, false));
        String publicDoc = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEqualsIgnoreCase("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>", output(publicDoc, true));
        // different impls will have different XML formatting. OpenJDK 13 default gives this: <body /> but others have <body/>, so just check start
        assertTrue(output(publicDoc, false).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC"));
        String systemDoc = "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">";
        assertEqualsIgnoreCase("<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>", output(systemDoc, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body/></html>", output(systemDoc, false));
        String legacyDoc = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        assertEqualsIgnoreCase("<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>", output(legacyDoc, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body/></html>", output(legacyDoc, false));
        String noDoctype = "<p>One</p>";
        assertEqualsIgnoreCase("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>", output(noDoctype, true));
        assertEqualsIgnoreCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><p>One</p></body></html>", output(noDoctype, false));
    }
}
