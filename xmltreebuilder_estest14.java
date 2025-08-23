package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the XML tree builder's handling of namespaces.
 */
public class XmlTreeBuilderNamespaceTest {

    /**
     * Tests that the XmlTreeBuilder correctly identifies the default namespace from the
     * parent context when parsing a new XML fragment. This is crucial for ensuring
     * that newly inserted nodes inherit the correct namespace.
     */
    @Test
    public void builderTracksDefaultNamespaceFromContextWhenParsingFragment() {
        // Arrange:
        // 1. Create a specific XmlTreeBuilder instance so we can inspect its state after parsing.
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);

        // 2. Define an XML document with a default namespace ("urn:example:ns1")
        //    and select a context element within it.
        String xmlWithNamespace = "<doc xmlns='urn:example:ns1'><parent/></doc>";
        Document doc = Jsoup.parse(xmlWithNamespace, "", Parser.xmlParser());
        Element context = doc.selectFirst("parent");

        // Act:
        // 3. Parse a new XML fragment within the prepared context. This action will
        //    configure the builder, causing it to read and store the namespace
        //    from the context element's hierarchy.
        parser.parseXmlFragment("<child/>", context);

        // Assert:
        // 4. Verify that the builder's internal state correctly reflects the default
        //    namespace inherited from the parsing context.
        assertEquals("urn:example:ns1", builder.defaultNamespace());
    }
}