package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertThrows;

import java.util.List;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.collections4.sequence.SequencesComparator}.
 */
public class SequencesComparatorTest {

    /**
     * Tests that the constructor of SequencesComparator throws a NullPointerException
     * when it is instantiated with null lists.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionForNullLists() {
        // The constructor attempts to access methods on the list arguments (e.g., .size())
        // without a null check, so a NullPointerException is the expected behavior.
        assertThrows(NullPointerException.class, () ->
            new SequencesComparator<Object>((List<Object>) null, (List<Object>) null)
        );
    }
}