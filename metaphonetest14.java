package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Tests the {@link Metaphone#isMetaphoneEqual(String, String)} method.
 */
@DisplayName("Metaphone#isMetaphoneEqual")
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * Tests that a set of phonetically similar names are all considered equal by the Metaphone algorithm.
     * <p>
     * Match data was originally sourced from http://www.lanw.com/java/phonetic/default.htm
     * </p>
     */
    @Test
    @DisplayName("should return true for variations of the name 'Peter'")
    void isMetaphoneEqual_shouldReturnTrueForPhoneticallySimilarNames() {
        assertAllAreMetaphoneEqual(
            "Peter", "Peadar", "Peder", "Pedro", "Petr", "Peyter", "Pieter", "Pietro", "Piotr"
        );
    }

    /**
     * Asserts that all strings in the given array are phonetically equal to each other.
     * <p>
     * This is a thorough check that compares every string against every other string in the set,
     * ensuring the equivalence relation holds for the entire group.
     *
     * @param names A set of names that should all be phonetically equal.
     */
    private void assertAllAreMetaphoneEqual(final String... names) {
        final List<String> nameList = Arrays.asList(names);
        final Metaphone metaphone = getStringEncoder();

        // Create a stream of executables, one for each pair-wise comparison.
        final Stream<Executable> assertions = nameList.stream()
            .flatMap(name1 -> nameList.stream()
                .map(name2 -> () -> assertTrue(
                    metaphone.isMetaphoneEqual(name1, name2),
                    () -> String.format("Expected '%s' and '%s' to be phonetically equal, but they were not.", name1, name2)
                ))
            );

        // Execute all assertions and report any failures together.
        assertAll("All names in the set should be phonetically equal", assertions);
    }
}