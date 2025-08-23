package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for XML namespace handling in the {@link XmlTreeBuilder}, specifically when
 * appending HTML/XML fragments to an existing document.
 */
public class XmlTreeBuilderTest {

    // This helper method was in the original class but is not used by the refactored test below.
    // It is kept for potential use by other tests in the suite.
    private static void assertXmlNamespace(Element el) {
        assertEquals(Parser.NamespaceXml, el.tag().namespace(), String.format("Element %s not in XML namespace", el.tagName()));
    }

    @Test
    @DisplayName("Appended XML fragments should correctly inherit and override namespaces")
    void appendShouldInheritAndOverrideNamespaces() {
        // The goal of this test is to verify that when an XML fragment is appended to an existing
        // element, its nodes correctly handle namespaces in two ways:
        // 1. Inheritance: Nodes in the fragment inherit namespaces from their new parent context.
        // 2. Overriding: The fragment can define its own namespaces, which take precedence within its scope.

        // ARRANGE: Create a base document with a default namespace and two prefixed namespaces.
        String initialXml = "<out xmlns='/out'>" +
                            "  <bk:book xmlns:bk='/books' xmlns:edi='/edi'>" +
                            "    <bk:title>Test</bk:title>" +
                            "  </bk:book>" +
                            "</out>";
        Document doc = Jsoup.parse(initialXml, Parser.xmlParser());
        Element bookElement = doc.expectFirst("bk|book");

        // --- PART 1: Test Namespace Inheritance ---

        // ACT: Append a fragment that uses prefixes ('bk', 'edi') defined in the parent context.
        String inheritingFragment = "<bk:content edi:foo='qux'>Content</bk:content>";
        bookElement.append(inheritingFragment);

        // ASSERT: The new element and its attribute should have inherited the correct namespaces.
        Element contentElement = bookElement.expectFirst("bk|content");
        assertEquals("/books", contentElement.tag().namespace(),
            "Appended element should inherit the 'bk' namespace from its parent.");
        assertEquals("/edi", contentElement.attribute("edi:foo").namespace(),
            "Attribute on appended element should inherit the 'edi' namespace.");

        // --- PART 2: Test Namespace Overriding and Scoping ---

        // ACT: Append a second, more complex fragment. This fragment:
        // - Is a sibling to a <data> tag, which should remain in the original default namespace.
        // - Defines a new default namespace for <html> and its children.
        // - Overrides the 'bk' prefix for <bk:news> and its children.
        String overridingFragment = "<data>Data</data>" +
                                    "<html xmlns='/html' xmlns:bk='/update'>" +
                                    "  <p>Foo</p>" +
                                    "  <bk:news>News</bk:news>" +
                                    "</html>";
        contentElement.append(overridingFragment);

        // ASSERT: Check that namespaces are correctly applied according to the new scopes.
        Element pElement = contentElement.expectFirst("p");
        assertEquals("/html", pElement.tag().namespace(),
            "'p' element should be in the new default '/html' namespace defined by its parent.");

        Element newsElement = contentElement.expectFirst("bk|news");
        assertEquals("/update", newsElement.tag().namespace(),
            "'bk:news' element should use the overridden '/update' namespace.");

        Element dataElement = contentElement.expectFirst("data");
        assertEquals("/out", dataElement.tag().namespace(),
            "'data' element should retain the original document's default '/out' namespace as it is outside the new scope.");
    }
}