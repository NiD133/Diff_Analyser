package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// The test class is renamed from the generated 'TokeniserTestTest8' to a more conventional name.
// Unused imports (Elements, Charset, Arrays, BufferSize) have been removed to reduce clutter.
public class TokeniserTest {

    /**
     * Verifies that numeric character references in the C1 control range (e.g., &#128;)
     * are correctly mapped to their Windows-1252 equivalents. This behavior is specified
     * by the WHATWG HTML standard for backward compatibility with legacy content.
     *
     * @see <a href="https://html.spec.whatwg.org/multipage/parsing.html#numeric-character-reference-end-state">
     *     WHATWG HTML Standard: Numeric character reference end state</a>
     */
    @Test
    // The test name is changed from the cryptic 'cp1252Entities' to clearly describe its purpose.
    void parsesIllegalNumericCharacterReferencesAsWindows1252() {
        // The numeric entity for 128 (decimal) or 80 (hex) is an illegal C1 control character.
        // Per the spec, this is parsed as the Euro sign (€) from the Windows-1252 character set.
        String expectedEuro = "\u20AC";
        assertEquals(expectedEuro, Jsoup.parse("&#0128;").text()); // Decimal reference
        assertEquals(expectedEuro, Jsoup.parse("&#x80;").text());  // Hexadecimal reference

        // Similarly, the numeric entity for 130 is parsed as the single low-9 quotation mark (‚).
        String expectedSingleLow9Quote = "\u201A";
        assertEquals(expectedSingleLow9Quote, Jsoup.parse("&#0130;").text());
    }
}