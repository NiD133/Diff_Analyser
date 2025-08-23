package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the isMetaphoneEqual method of the {@link Metaphone} class.
 * <p>
 * Match data is based on an external reference implementation from
 * http://www.lanw.com/java/phonetic/default.htm
 * </p>
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Provides pairs of names that should be metaphonically equal. This data focuses on the
     * case where an initial 'A' is not followed by an 'E'.
     *
     * @return A stream of arguments, where each argument is a pair of strings to compare.
     */
    private static Stream<Arguments> metaphonicallyEqualNamesProvider() {
        final String source = "Albert";
        final String[] similarNames = {"Ailbert", "Alberik", "Albert", "Alberto", "Albrecht"};

        // Create pairs of (source, similarName) for the test.
        return Stream.of(similarNames)
                .map(similarName -> Arguments.of(source, similarName));
    }

    @DisplayName("isMetaphoneEqual should return true for phonetically similar names")
    @ParameterizedTest(name = "[{index}] isMetaphoneEqual(\"{0}\", \"{1}\")")
    @MethodSource("metaphonicallyEqualNamesProvider")
    void testIsMetaphoneEqual(final String name1, final String name2) {
        // The isMetaphoneEqual method should be symmetrical.
        // We test both directions to ensure correctness.
        assertTrue(getStringEncoder().isMetaphoneEqual(name1, name2),
                () -> String.format("Expected '%s' and '%s' to be metaphonically equal", name1, name2));

        assertTrue(getStringEncoder().isMetaphoneEqual(name2, name1),
                () -> String.format("Symmetry check failed: Expected '%s' and '%s' to be metaphonically equal", name2, name1));
    }
}