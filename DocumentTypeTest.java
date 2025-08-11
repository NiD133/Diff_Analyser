package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

/**
 * Readable, intention-revealing tests for DocumentType behavior.
 */
public class DocumentTypeTest {

    // Common sample values used across tests
    private static final String HTML = "html";
    private static final String EMPTY = "";
    private static final String SAMPLE_PUBLIC_ID = "-//IETF//DTD HTML//";
    private static final String SAMPLE_SYSTEM_ID = "http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd";
    private static final String COMBO_PUBLIC_ID = "--public";
    private static final String COMBO_SYSTEM_ID = "--system";

    // Helpers

    private static DocumentType parseHtmlDoctype(String input) {
        // The first child node of a document containing only a doctype is the doctype node
        return (DocumentType) Jsoup.parse(input).childNode(0);
    }

    private static String htmlOuter(String input) {
        return parseHtmlDoctype(input).outerHtml();
    }

    private static String xmlOuter(String input) {
        return Jsoup.parse(input, "", Parser.xmlParser()).childNode(0).outerHtml();
    }

    @Nested
    class ConstructorValidation {

        @Test
        void allowsBlankName() {
            // Given/When: constructing with blank name and blank IDs
            new DocumentType(EMPTY, EMPTY, EMPTY);
            // Then: no exception is thrown
        }

        @Test
        void throwsOnNullPublicAndSystemIds() {
            // Given/When/Then
            assertThrows(IllegalArgumentException.class,
                () -> new DocumentType(HTML, null, null),
                "Null publicId and systemId should be rejected");
        }

        @Test
        void allowsBlankPublicAndSystemIds() {
            // Given/When: blank IDs are allowed
            new DocumentType(HTML, EMPTY, EMPTY);
            // Then: no exception is thrown
        }
    }

    @Nested
    class RenderingOuterHtml {

        @Test
        void rendersHtml5DoctypeCompact() {
            // Given
            DocumentType html5 = new DocumentType(HTML, EMPTY, EMPTY);

            // When/Then
            assertEquals("<!doctype html>", html5.outerHtml(),
                "HTML5 doctype renders with lowercase keyword and no IDs");
        }

        @Test
        void rendersPublicDoctype() {
            // Given
            DocumentType publicDocType = new DocumentType(HTML, SAMPLE_PUBLIC_ID, EMPTY);

            // When/Then
            assertEquals("<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML//\">", publicDocType.outerHtml(),
                "Public doctype should include PUBLIC and the publicId");
        }

        @Test
        void rendersSystemDoctype() {
            // Given
            DocumentType systemDocType = new DocumentType(HTML, EMPTY, SAMPLE_SYSTEM_ID);

            // When/Then
            assertEquals("<!DOCTYPE html SYSTEM \"http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd\">",
                systemDocType.outerHtml(),
                "System doctype should include SYSTEM and the systemId");
        }

        @Test
        void rendersCombinedPublicAndSystemDoctypeAndRetainsCasing() {
            // Given
            DocumentType combo = new DocumentType("notHtml", COMBO_PUBLIC_ID, COMBO_SYSTEM_ID);

            // When/Then
            assertEquals("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">", combo.outerHtml());
            assertEquals("notHtml", combo.name(), "Name casing is preserved when directly constructed");
            assertEquals(COMBO_PUBLIC_ID, combo.publicId());
            assertEquals(COMBO_SYSTEM_ID, combo.systemId());
        }
    }

    @Nested
    class RoundTripParsing {

        static Stream<Arguments> roundTripCases() {
            String base = "<!DOCTYPE html>";
            String publicDoc = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
            String systemDoc = "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">";
            String legacyDoc = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";

            return Stream.of(
                // input, expected HTML outerHtml, expected XML outerHtml
                Arguments.of(base, "<!doctype html>", base),
                Arguments.of(publicDoc, publicDoc, publicDoc),
                Arguments.of(systemDoc, systemDoc, systemDoc),
                Arguments.of(legacyDoc, legacyDoc, legacyDoc)
            );
        }

        @ParameterizedTest(name = "HTML outerHtml of {0}")
        @MethodSource("roundTripCases")
        void htmlOutputMatchesExpected(String input, String expectedHtml, String ignoredXml) {
            assertEquals(expectedHtml, htmlOuter(input));
        }

        @ParameterizedTest(name = "XML outerXml of {0}")
        @MethodSource("roundTripCases")
        void xmlOutputMatchesExpected(String input, String ignoredHtml, String expectedXml) {
            assertEquals(expectedXml, xmlOuter(input));
        }
    }

    @Nested
    class AttributesView {

        @Test
        void attributesForHtml5Doctype() {
            // Given
            Document doc = Jsoup.parse("<!DOCTYPE html>");
            DocumentType doctype = doc.documentType();

            // Then
            assertEquals("#doctype", doctype.nodeName());
            assertEquals("html", doctype.name());
            assertEquals("html", doctype.attr("name"));
            assertEquals("", doctype.publicId());
            assertEquals("", doctype.systemId());
        }

        @Test
        void attributesForPublicAndSystemDoctype() {
            // Given
            Document doc = Jsoup.parse("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">");
            DocumentType doctype = doc.documentType();

            // The HTML parser normalizes the name to lowercase
            assertEquals("#doctype", doctype.nodeName());
            assertEquals("nothtml", doctype.name(), "Parsed doctype name should be lowercase in HTML mode");
            assertEquals("nothtml", doctype.attr("name"));
            assertEquals("--public", doctype.publicId());
            assertEquals("--system", doctype.systemId());
        }
    }
}