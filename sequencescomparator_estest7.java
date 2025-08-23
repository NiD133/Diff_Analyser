package org.apache.commons.collections4.sequence;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * This test suite focuses on the behavior of SequencesComparator, particularly
 * how it handles invalid constructor arguments.
 *
 * Note: The original class name "SequencesComparator_ESTestTest7" from the
 * auto-generated test was kept to show a direct improvement. In a real-world
 * scenario, this class would be renamed to something more descriptive,
 * e.g., "SequencesComparatorTest".
 */
public class SequencesComparator_ESTestTest7 {

    /**
     * Verifies that getScript() throws a NullPointerException if the comparator
     * was constructed with a null Equator. The sequences must be non-empty
     * to ensure the comparison logic, which uses the equator, is triggered.
     */
    @Test(expected = NullPointerException.class)
    public void getScriptShouldThrowNullPointerExceptionWhenUsingNullEquator() {
        // Arrange
        // Create two identical, non-empty sequences. This setup ensures that the
        // comparison algorithm will execute and attempt to use the provided Equator.
        final List<Object> sequence = Collections.singletonList("a");

        // Instantiate the comparator with a null Equator. This is the specific
        // invalid configuration we want to test.
        final SequencesComparator<Object> comparator = new SequencesComparator<>(sequence, sequence, null);

        // Act & Assert
        // Calling getScript() triggers the comparison. Since the Equator is null,
        // an attempt to call a method on it will result in a NullPointerException.
        // The @Test(expected) annotation asserts that this exception is thrown.
        comparator.getScript();
    }
}