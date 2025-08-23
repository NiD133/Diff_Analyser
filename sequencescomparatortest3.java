package org.apache.commons.collections4.sequence;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests a key property of the Myers algorithm used in {@link SequencesComparator}:
 * the number of modifications in the generated edit script should be minimal.
 */
class SequencesComparatorMinimalChangesTest {

    /**
     * This test verifies that the number of modifications in the calculated
     * {@link EditScript} is less than or equal to the number of manual changes
     * applied to a sequence. The Myers algorithm is guaranteed to find the shortest
     * possible edit script, so the number of modifications it finds should not be
     * greater than the number of changes we actually made.
     *
     * @param numberOfAppliedChanges the number of random insertions or deletions to apply
     */
    @DisplayName("Script modifications should be less than or equal to the number of applied changes")
    @ParameterizedTest(name = "for {0} applied changes")
    @ValueSource(ints = {0, 5, 10, 15, 20, 25, 30, 35, 40})
    void getScriptModificationsShouldBeLessThanOrEqualToAppliedChanges(final int numberOfAppliedChanges) {
        // Arrange: Create an original sequence and a modified version with a specific number of changes.
        final String[] shadokAlphabet = {"GA", "BU", "ZO", "MEU"};
        final List<String> originalSequence = List.of(
            shadokAlphabet[0], shadokAlphabet[2], shadokAlphabet[3], shadokAlphabet[1],
            shadokAlphabet[0], shadokAlphabet[0], shadokAlphabet[2], shadokAlphabet[1],
            shadokAlphabet[3], shadokAlphabet[0], shadokAlphabet[2], shadokAlphabet[1],
            shadokAlphabet[3], shadokAlphabet[2], shadokAlphabet[2], shadokAlphabet[0],
            shadokAlphabet[1], shadokAlphabet[3], shadokAlphabet[0], shadokAlphabet[3]
        );

        // Use a fixed seed for reproducible random changes.
        final Random random = new Random(4564634237452342L);
        final List<String> modifiedSequence = new ArrayList<>(originalSequence);

        for (int i = 0; i < numberOfAppliedChanges; i++) {
            if (random.nextBoolean()) {
                // Insert a random element at a random position.
                modifiedSequence.add(
                    random.nextInt(modifiedSequence.size() + 1),
                    shadokAlphabet[random.nextInt(shadokAlphabet.length)]
                );
            } else if (!modifiedSequence.isEmpty()) {
                // Delete a random element, if the list is not empty.
                modifiedSequence.remove(random.nextInt(modifiedSequence.size()));
            }
        }

        // Act: Compare the original and modified sequences to get the edit script.
        final SequencesComparator<String> comparator =
            new SequencesComparator<>(originalSequence, modifiedSequence);
        final EditScript<String> script = comparator.getScript();
        final int foundModifications = script.getModifications();

        // Assert: The number of modifications found by the algorithm should not exceed the number we applied.
        assertTrue(foundModifications <= numberOfAppliedChanges,
            () -> String.format(
                "The number of modifications should be minimal. Expected <= %d, but was %d.",
                numberOfAppliedChanges, foundModifications
            ));
    }
}