package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the {@code isMetaphoneEqual} method of the {@link Metaphone} class.
 * This class focuses on verifying that phonetically similar words are correctly
 * identified as being equal.
 */
public class MetaphoneEqualityTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Test
    void isMetaphoneEqualShouldReturnTrueForSameWord() {
        assertTrue(getStringEncoder().isMetaphoneEqual("Xalan", "Xalan"),
            "A word should be metaphone-equal to itself.");
    }

    /**
     * Tests that a set of phonetically similar words are all considered equal by the Metaphone algorithm.
     * The reference word is "Xalan", and it is compared against a list of other names that should be phonetically equivalent.
     * <p>
     * Match data was originally computed from an external website: http://www.lanw.com/java/phonetic/default.htm
     * </p>
     *
     * @param nameToCompare a name that should be phonetically equivalent to "Xalan"
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "Celene", "Celina", "Celine", "Selena", "Selene", "Selina", "Seline", "Suellen", "Xylina"
    })
    void isMetaphoneEqualShouldReturnTrueForPhoneticallySimilarWords(final String nameToCompare) {
        final String referenceName = "Xalan";

        assertTrue(
            getStringEncoder().isMetaphoneEqual(referenceName, nameToCompare),
            () -> "Expected '" + referenceName + "' to be metaphone-equal with '" + nameToCompare + "'"
        );
    }
}