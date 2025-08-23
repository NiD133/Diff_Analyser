package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XmlTreeBuilder} focusing on XML namespace handling.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the XML parser correctly processes attributes with and without namespaces.
     * It checks that for a namespaced attribute, the prefix, local name, and namespace URI are
     * correctly identified. For a regular attribute, it confirms that namespace-related
     * properties are empty.
     */
    @Test
    void parsesNamespacedAttributesCorrectly() {
        // Arrange: Define an XML string with a namespace declaration and two attributes:
        // one namespaced ('edi:taxClass') and one regular ('other').
        String xml = """
            <x xmlns:edi='http://ecommerce.example.org/schema'>
              <lineItem edi:taxClass="exempt" other="foo">Baby food</lineItem>
            </x>""";

        // Act: Parse the XML string using the dedicated XML parser.
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Element lineItem = doc.expectFirst("lineItem");

        // Assert: Verify the properties of both attributes.

        // 1. Check the namespaced attribute 'edi:taxClass'
        Attribute taxClassAttr = lineItem.attribute("edi:taxClass");
        assertNotNull(taxClassAttr, "The 'edi:taxClass' attribute should exist.");
        assertAll("Properties of namespaced attribute 'edi:taxClass'",
            () -> assertEquals("edi", taxClassAttr.prefix(), "Prefix should be 'edi'"),
            () -> assertEquals("taxClass", taxClassAttr.localName(), "Local name should be 'taxClass'"),
            () -> assertEquals("http://ecommerce.example.org/schema", taxClassAttr.namespace(), "Namespace URI should match the declaration")
        );

        // 2. Check the regular attribute 'other'
        Attribute otherAttr = lineItem.attribute("other");
        assertNotNull(otherAttr, "The 'other' attribute should exist.");
        assertAll("Properties of regular attribute 'other'",
            () -> assertEquals("foo", otherAttr.getValue(), "Value should be 'foo'"),
            () -> assertEquals("", otherAttr.prefix(), "Prefix should be empty"),
            () -> assertEquals("other", otherAttr.localName(), "Local name should be 'other'"),
            () -> assertEquals("", otherAttr.namespace(), "Namespace should be empty")
        );
    }
}