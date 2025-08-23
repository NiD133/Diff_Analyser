package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the Metaphone class, focusing on its ability to identify phonetically similar strings.
 */
class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Provides a stream of names that are phonetically equivalent to "Mary".
     * According to the Metaphone algorithm, all these names should produce the code "MR".
     * Data was originally computed from http://www.lanw.com/java/phonetic/default.htm
     *
     * @return A stream of strings.
     */
    static Stream<String> phoneticEquivalentsOfMary() {
        return Stream.of(
            "Mary", "Mair", "Maire", "Mara", "Mareah", "Mari", "Maria",
            "Marie", "Maura", "Maure", "Meara", "Merrie", "Merry", "Mira",
            "Moira", "Mora", "Moria", "Moyra", "Muire", "Myra", "Myrah"
        );
    }

    @DisplayName("isMetaphoneEqual should return true for all phonetic equivalents of 'Mary'")
    @ParameterizedTest(name = "isMetaphoneEqual(\"Mary\", \"{0}\")")
    @MethodSource("phoneticEquivalentsOfMary")
    void testIsMetaphoneEqualWithPhoneticEquivalents(final String name) {
        // All names in the source stream are phonetically equivalent to "Mary".
        assertTrue(getStringEncoder().isMetaphoneEqual("Mary", name),
            () -> "Expected '" + name + "' to be phonetically equal to 'Mary'");
    }

    @DisplayName("metaphone should return 'MR' for all phonetic equivalents of 'Mary'")
    @ParameterizedTest(name = "metaphone(\"{0}\")")
    @MethodSource("phoneticEquivalentsOfMary")
    void testMetaphoneEncodingOfPhoneticEquivalents(final String name) {
        // This is a more direct test that verifies the actual Metaphone code.
        // The core functionality of isMetaphoneEqual(a, b) is metaphone(a).equals(metaphone(b)).
        // Testing the direct output is more robust.
        assertEquals("MR", getStringEncoder().metaphone(name));
    }
}