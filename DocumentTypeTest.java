package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the DocumentType node.
 *
 * @author Jonathan Hedley, http://jonathanhedley.com/
 */
@DisplayName("A DocumentType Node")
public class DocumentTypeTest {

    @Nested
    @DisplayName("when constructed")
    class ConstructorTests {

        @Test
        @DisplayName("should allow a blank name and IDs")
        void allowsBlankNameAndIds() {
            DocumentType doctype = new DocumentType("", "", "");
            assertAll(
                () -> assertEquals("", doctype.name()),
                () -> assertEquals("", doctype.publicId()),
                () -> assertEquals("", doctype.systemId())
            );
        }

        @Test
        @DisplayName("should allow blank public and system IDs with a name")
        void allowsBlankIds() {
            DocumentType doctype = new DocumentType("html", "", "");
            assertAll(
                () -> assertEquals("html", doctype.name()),
                () -> assertEquals("", doctype.publicId()),
                () -> assertEquals("", doctype.systemId())
            );
        }

        @Test
        @DisplayName("should throw an exception for a null public ID")
        void throwsOnNullPublicId() {
            assertThrows(IllegalArgumentException.class, () -> new DocumentType("html", null, ""));
        }

        @Test
        @DisplayName("should throw an exception for a null system ID")
        void throwsOnNullSystemId() {
            assertThrows(IllegalArgumentException.class, () -> new DocumentType("html", "", null));
        }
    }

    @Nested
    @DisplayName("generating outer HTML")
    class OuterHtmlGeneration {

        @Test
        @DisplayName("should correctly format an HTML5 doctype")
        void formatsHtml5Doctype() {
            DocumentType html5 = new DocumentType("html", "", "");
            assertEquals("<!doctype html>", html5.outerHtml());
        }

        @Test
        @DisplayName("should correctly format a doctype with a public ID")
        void formatsPublicDoctype() {
            DocumentType publicDocType = new DocumentType("html", "-//IETF//DTD HTML//", "");
            assertEquals("<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML//\">", publicDocType.outerHtml());
        }

        @Test
        @DisplayName("should correctly format a doctype with a system ID")
        void formatsSystemDoctype() {
            DocumentType systemDocType = new DocumentType("html", "", "http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd");
            assertEquals("<!DOCTYPE html SYSTEM \"http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd\">", systemDocType.outerHtml());
        }

        @Test
        @DisplayName("should correctly format a doctype with both public and system IDs")
        void formatsCombinedDoctype() {
            DocumentType combo = new DocumentType("notHtml", "--public", "--system");
            assertEquals("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">", combo.outerHtml());
        }
    }

    @Nested
    @DisplayName("when parsed from a string")
    class ParsingTests {

        @ParameterizedTest(name = "[{index}] {0}")
        @ValueSource(strings = {
            "<!DOCTYPE html>",
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
            "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">",
            "<!DOCTYPE html SYSTEM \"about:legacy-compat\">"
        })
        @DisplayName("should be preserved after a parsing and serialization round-trip")
        void preservesDoctypeInRoundTrip(String doctypeString) {
            // HTML parser normalizes the simple doctype to lowercase
            String expectedHtmlOutput = doctypeString.equals("<!DOCTYPE html>") ? "<!doctype html>" : doctypeString;
            DocumentType htmlDoctype = (DocumentType) Jsoup.parse(doctypeString).childNode(0);
            assertEquals(expectedHtmlOutput, htmlDoctype.outerHtml(), "HTML round-trip should match");

            // XML parser should preserve the original case
            DocumentType xmlDoctype = (DocumentType) Jsoup.parse(doctypeString, "", Parser.xmlParser()).childNode(0);
            assertEquals(doctypeString, xmlDoctype.outerHtml(), "XML round-trip should match");
        }

        @Test
        @DisplayName("should correctly extract attributes from a simple doctype")
        void extractsAttributesFromSimpleDoctype() {
            Document doc = Jsoup.parse("<!DOCTYPE html>");
            DocumentType doctype = doc.documentType();

            assertAll("Simple Doctype Attributes",
                () -> assertEquals("#doctype", doctype.nodeName()),
                () -> assertEquals("html", doctype.name()),
                () -> assertEquals("html", doctype.attr("name")),
                () -> assertEquals("", doctype.publicId()),
                () -> assertEquals("", doctype.systemId())
            );
        }

        @Test
        @DisplayName("should correctly extract attributes from a complex doctype and normalize its name")
        void extractsAttributesFromComplexDoctype() {
            Document doc = Jsoup.parse("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">");
            DocumentType doctype = doc.documentType();

            assertAll("Complex Doctype Attributes",
                () -> assertEquals("#doctype", doctype.nodeName()),
                () -> assertEquals("nothtml", doctype.name(), "Name should be lowercased on parsing"),
                () -> assertEquals("nothtml", doctype.attr("name")),
                () -> assertEquals("--public", doctype.publicId()),
                () -> assertEquals("--system", doctype.systemId())
            );
        }
    }
}