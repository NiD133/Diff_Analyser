package org.apache.commons.collections4.sequence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link SequencesComparator} focusing on the number of modifications in the edit script.
 */
class SequencesComparatorTest {

    /**
     * Converts a String to a List of Characters for use with SequencesComparator.
     *
     * @param str the string to convert
     * @return a list of characters from the string
     */
    private List<Character> toCharacterList(final String str) {
        final List<Character> list = new ArrayList<>();
        for (int i = 0; i < str.length(); ++i) {
            list.add(str.charAt(i));
        }
        return list;
    }

    /**
     * Provides test cases for calculating the number of modifications.
     * Each argument set consists of a "before" string, an "after" string,
     * and the expected number of modifications (edit distance).
     *
     * @return a stream of arguments for the parameterized test
     */
    private static Stream<Arguments> modificationCountTestData() {
        return Stream.of(
            // Test case: Basic substitutions and shared characters
            Arguments.of("bottle", "noodle", 6),
            // Test case: More complex, longer strings
            Arguments.of("nematode knowledge", "empty bottle", 16),
            // Test case: Two empty sequences should have zero modifications
            Arguments.of("", "", 0),
            // Test case: Complete replacement (all deletions, one insertion)
            Arguments.of("aa", "C", 3),
            // Test case: Deletion of a suffix
            Arguments.of("prefixed string", "prefix", 9),
            // Test case: Common subsequence with reordered characters
            Arguments.of("ABCABBA", "CBABAC", 5),
            // Test case: Insertion of a prefix and suffix
            Arguments.of("glop glop", "pas glop pas glop", 8),
            // Test case: Complete replacement of a short string
            Arguments.of("coq", "ane", 6),
            // Test case: No common characters
            Arguments.of("spider-man", "klingon", 13)
        );
    }

    @ParameterizedTest(name = "[{index}] Modifications from \"{0}\" to \"{1}\" should be {2}")
    @MethodSource("modificationCountTestData")
    void testGetScriptModifications(final String before, final String after, final int expectedModifications) {
        final List<Character> beforeSequence = toCharacterList(before);
        final List<Character> afterSequence = toCharacterList(after);

        final SequencesComparator<Character> comparator =
            new SequencesComparator<>(beforeSequence, afterSequence);

        final int actualModifications = comparator.getScript().getModifications();

        assertEquals(expectedModifications, actualModifications,
            () -> String.format("Incorrect modification count for sequences '%s' and '%s'", before, after));
    }
}