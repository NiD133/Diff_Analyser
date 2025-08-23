package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static org.jsoup.TextUtil.normalizeSpaces;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("W3CDom Conversion for Doctypes and Entities")
public class W3CDomDoctypeAndEntityTest {

    /**
     * Performs a round-trip conversion: parses an input string with Jsoup, converts to a W3C Document,
     * and then serializes it back to a string.
     *
     * @param inputHtml  The HTML/XML string to parse.
     * @param isHtmlMode True to use HTML output properties, false for XML.
     * @return The normalized string representation of the W3C Document.
     */
    private String roundTripToW3cString(String inputHtml, boolean isHtmlMode) {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(inputHtml);
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        Map<String, String> properties = isHtmlMode ? W3CDom.OutputHtml() : W3CDom.OutputXml();
        return normalizeSpaces(W3CDom.asString(w3cDoc, properties));
    }

    private void assertEqualsIgnoreCase(String expected, String actual) {
        assertEquals(expected.toLowerCase(Locale.ROOT), actual.toLowerCase(Locale.ROOT));
    }

    // Provider for parameterized security test
    public static Stream<Arguments> parserProvider() {
        return Stream.of(Arguments.of(Parser.htmlParser()), Arguments.of(Parser.xmlParser()));
    }

    @Nested
    @DisplayName("Security Vulnerability Checks")
    class SecurityTests {
        @ParameterizedTest
        @MethodSource("org.jsoup.helper.W3CDomDoctypeAndEntityTest#parserProvider")
        @DisplayName("Should not expand XML entities to prevent Billion Laughs / XXE attacks")
        void doesNotExpandEntities(Parser parser) {
            // This test confirms that Jsoup does not parse and expand entities within a DOCTYPE,
            // which prevents Billion Laughs and XXE vulnerabilities from being introduced during W3C conversion.
            String billionLaughsXml = "<?xml version=\"1.0\"?>\n" +
                "<!DOCTYPE lolz [\n" +
                " <!ENTITY lol \"lol\">\n" +
                " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
                "]>\n" +
                "<html><body><p>&lol1;</p></body></html>";

            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughsXml, parser);
            W3CDom w3cDom = new W3CDom();
            Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

            assertNotNull(w3cDoc);

            // Verify the entity in the <p> tag is not expanded
            NodeList pElements = w3cDoc.getElementsByTagName("p");
            assertEquals(1, pElements.getLength());
            assertEquals("&lol1;", pElements.item(0).getTextContent());

            // Verify the final string output is not expanded and the entity is escaped
            String outputString = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
            assertFalse(outputString.contains("lololol"));
            assertTrue(outputString.contains("&amp;lol1;"));
        }
    }

    @Nested
    @DisplayName("Doctype Round-trip Conversion")
    class DoctypeRoundTripTests {
        // Note: Assertions use case-insensitive comparisons because different XML transformers
        // (e.g., the default OpenJDK vs. Saxon) may produce different casing for tags like <META>.

        @Test
        @DisplayName("Should handle simple HTML5 doctype")
        void simpleHtml5Doctype() {
            String input = "<!DOCTYPE html><p>One</p>";

            String expectedHtmlOutput = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>";
            assertEqualsIgnoreCase(expectedHtmlOutput, roundTripToW3cString(input, true));

            String expectedXmlOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><p>One</p></body></html>";
            assertEqualsIgnoreCase(expectedXmlOutput, roundTripToW3cString(input, false));
        }

        @Test
        @DisplayName("Should handle doctype with PUBLIC and SYSTEM identifiers")
        void publicIdDoctype() {
            String input = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

            String expectedHtmlOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
            assertEqualsIgnoreCase(expectedHtmlOutput, roundTripToW3cString(input, true));

            // Note: different XML impls format empty tags differently (<body/> vs <body />), so we just check the start.
            String xmlOutput = roundTripToW3cString(input, false);
            assertTrue(xmlOutput.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC"));
        }

        @Test
        @DisplayName("Should handle doctype with only a SYSTEM identifier")
        void systemIdDoctype() {
            String input = "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\">";

            String expectedHtmlOutput = "<!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
            assertEqualsIgnoreCase(expectedHtmlOutput, roundTripToW3cString(input, true));

            String expectedXmlOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"exampledtdfile.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body/></html>";
            assertEqualsIgnoreCase(expectedXmlOutput, roundTripToW3cString(input, false));
        }

        @Test
        @DisplayName("Should handle legacy-compat doctype string")
        void legacyCompatDoctype() {
            String input = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";

            String expectedHtmlOutput = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>";
            assertEqualsIgnoreCase(expectedHtmlOutput, roundTripToW3cString(input, true));

            String expectedXmlOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body/></html>";
            assertEqualsIgnoreCase(expectedXmlOutput, roundTripToW3cString(input, false));
        }

        @Test
        @DisplayName("Should handle documents without a doctype")
        void noDoctype() {
            String input = "<p>One</p>";

            String expectedHtmlOutput = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>";
            assertEqualsIgnoreCase(expectedHtmlOutput, roundTripToW3cString(input, true));

            String expectedXmlOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><p>One</p></body></html>";
            assertEqualsIgnoreCase(expectedXmlOutput, roundTripToW3cString(input, false));
        }
    }
}