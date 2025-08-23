package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttributeTestTest3 {

    @Test
    void handlesSupplementaryCharactersInKeyAndValue() {
        // A supplementary character is a Unicode character outside the Basic Multilingual Plane (BMP),
        // which is represented by a surrogate pair of chars in Java's UTF-16.
        // This test verifies that such characters are handled correctly in attribute keys and values.
        // The codepoint 135361 (U+210A1) represents a CJK Unified Ideograph.
        String supplementaryChar = new String(Character.toChars(135361));

        String key = supplementaryChar;
        String value = "A" + supplementaryChar + "B";
        Attribute attribute = new Attribute(key, value);

        String expectedHtml = supplementaryChar + "=\"" + "A" + supplementaryChar + "B" + "\"";

        // Verify that the HTML representation is correct
        assertEquals(expectedHtml, attribute.html());

        // Verify that toString() also returns the correct HTML representation
        assertEquals(attribute.html(), attribute.toString());
    }
}