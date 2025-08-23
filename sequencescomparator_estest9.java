package org.apache.commons.collections4.sequence;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Tests the constructor of {@link SequencesComparator}.
 * This class focuses on ensuring the constructor correctly handles invalid arguments.
 */
public class SequencesComparatorConstructorTest {

    /**
     * Tests that the constructor throws a NullPointerException if the first sequence is null.
     * The constructor is expected to fail fast when provided with invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionWhenFirstSequenceIsNull() {
        // The constructor for SequencesComparator requires two non-null lists.
        // This test verifies that a NullPointerException is thrown when the first list is null.
        // An empty list is used for the second argument to isolate the failure condition.
        new SequencesComparator<Object>(null, Collections.emptyList());
    }

    /**
     * Tests that the constructor throws a NullPointerException if the second sequence is null.
     * The constructor is expected to fail fast when provided with invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionWhenSecondSequenceIsNull() {
        // This test verifies that a NullPointerException is thrown when the second list is null.
        new SequencesComparator<Object>(Collections.emptyList(), null);
    }
}