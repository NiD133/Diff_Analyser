package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link ComparatorChain}, focusing on its behavior when
 * dealing with comparators that throw exceptions.
 */
public class ComparatorChainExceptionTest {

    /**
     * Tests that a RuntimeException thrown by a contained comparator is correctly
     * propagated by the ComparatorChain's compare() method.
     */
    @Test
    public void compareShouldPropagateRuntimeExceptionFromWrappedComparator() {
        // Arrange
        final String expectedExceptionMessage = "Exception from a wrapped comparator";

        // Create a simple comparator that always throws a RuntimeException.
        // This simulates a scenario where a comparator in the chain fails during execution.
        final Comparator<Object> failingComparator = (obj1, obj2) -> {
            throw new RuntimeException(expectedExceptionMessage);
        };

        // Create the ComparatorChain under test, containing only the failing comparator.
        final ComparatorChain<Object> chainWithFailingComparator = new ComparatorChain<>(failingComparator);

        // The actual objects to be compared are irrelevant since the comparator will fail immediately.
        final Object dummyObject1 = new Object();
        final Object dummyObject2 = new Object();

        // Act & Assert
        try {
            chainWithFailingComparator.compare(dummyObject1, dummyObject2);
            fail("A RuntimeException was expected to be thrown and propagated.");
        } catch (final RuntimeException e) {
            // Verify that the exception from the wrapped comparator was caught.
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
}