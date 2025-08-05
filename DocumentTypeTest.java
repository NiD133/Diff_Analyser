package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the DocumentType node
 *
 * @author Jonathan Hedley, http://jonathanhedley.com/
 */
class DocumentTypeTest {
    private static final String HTML5_DOCTYPE = "<!DOCTYPE html>";
    private static final String PUBLIC_DOCTYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
    private static final String SYSTEM_DOCTYPE = "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">";
    private static final String LEGACY_DOCTYPE = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";

    // Tests for DocumentType constructor ===================================================
    @Test
    void constructor_WithBlankName_ShouldSucceed() {
        new DocumentType("", "", "");
    }

    @Test
    void constructor_WithNullIds_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, 
            () -> new DocumentType("html", null, null));
    }

    @Test
    void constructor_WithBlankIds_ShouldSucceed() {
        new DocumentType("html", "", "");
    }

    // Tests for outerHtml generation ======================================================
    @Test
    void outerHtml_WithHtml5Doctype_GeneratesMinimalHtml() {
        DocumentType html5 = new DocumentType("html", "", "");
        assertEquals("<!doctype html>", html5.outerHtml());
    }

    @Test
    void outerHtml_WithPublicId_GeneratesPublicDoctype() {
        DocumentType publicDocType = new DocumentType("html", "-//IETF//DTD HTML//", "");
        assertEquals("<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML//\">", 
                     publicDocType.outerHtml());
    }

    @Test
    void outerHtml_WithSystemId_GeneratesSystemDoctype() {
        DocumentType systemDocType = new DocumentType("html", "", 
            "http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd");
        assertEquals("<!DOCTYPE html SYSTEM \"http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd\">", 
                     systemDocType.outerHtml());
    }

    @Test
    void outerHtml_WithPublicAndSystemIds_GeneratesFullDoctype() {
        DocumentType combo = new DocumentType("notHtml", "--public", "--system");
        assertEquals("<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">", 
                     combo.outerHtml());
    }

    @Test
    void documentTypeProperties_AfterConstruction_AreCorrectlySet() {
        DocumentType docType = new DocumentType("notHtml", "--public", "--system");
        assertEquals("notHtml", docType.name());
        assertEquals("--public", docType.publicId());
        assertEquals("--system", docType.systemId());
    }

    // Tests for round-trip parsing ========================================================
    @ParameterizedTest
    @MethodSource("roundTripCases")
    void roundTrip_ParsingDoctype_GeneratesExpectedOutput(String input, String expectedHtml, String expectedXml) {
        assertEquals(expectedHtml, htmlOutput(input));
        assertEquals(expectedXml, xmlOutput(input));
    }

    private static Stream<Arguments> roundTripCases() {
        return Stream.of(
            Arguments.of(HTML5_DOCTYPE, "<!doctype html>", HTML5_DOCTYPE),
            Arguments.of(PUBLIC_DOCTYPE, PUBLIC_DOCTYPE, PUBLIC_DOCTYPE),
            Arguments.of(SYSTEM_DOCTYPE, SYSTEM_DOCTYPE, SYSTEM_DOCTYPE),
            Arguments.of(LEGACY_DOCTYPE, LEGACY_DOCTYPE, LEGACY_DOCTYPE)
        );
    }

    private String htmlOutput(String in) {
        DocumentType type = (DocumentType) Jsoup.parse(in).childNode(0);
        return type.outerHtml();
    }

    private String xmlOutput(String in) {
        return Jsoup.parse(in, "", Parser.xmlParser()).childNode(0).outerHtml();
    }

    // Tests for attribute handling =======================================================
    @Test
    void attributes_WithHtml5Doctype_ReturnsExpectedValues() {
        Document doc = Jsoup.parse(HTML5_DOCTYPE);
        DocumentType doctype = doc.documentType();

        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.name());
        assertEquals("html", doctype.attr("name"));
        assertEquals("", doctype.publicId());
        assertEquals("", doctype.systemId());
    }

    @Test
    void attributes_WithPublicAndSystemIds_ReturnsExpectedValues() {
        String doctypeInput = "<!DOCTYPE notHtml PUBLIC \"--public\" \"--system\">";
        Document doc = Jsoup.parse(doctypeInput);
        DocumentType doctype = doc.documentType();

        assertEquals("#doctype", doctype.nodeName());
        assertEquals("nothtml", doctype.name());
        assertEquals("nothtml", doctype.attr("name"));
        assertEquals("--public", doctype.publicId());
        assertEquals("--system", doctype.systemId());
    }
}