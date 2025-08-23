package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for the CompositeSet class.
 * The original test was automatically generated and has been refactored for clarity.
 */
public class CompositeSet_ESTestTest16 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that calling addAll() on a CompositeSet without a configured
     * SetMutator throws an UnsupportedOperationException.
     */
    @Test
    public void addAllShouldThrowUnsupportedOperationExceptionWhenNoMutatorIsSet() {
        // Arrange: Create a CompositeSet. By default, it has no SetMutator,
        // so modification operations like addAll() are unsupported.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>();
        final Collection<Integer> collectionToAdd = Collections.singleton(1);

        // Act & Assert: Verify that calling addAll throws the expected exception.
        try {
            compositeSet.addAll(collectionToAdd);
            fail("Expected an UnsupportedOperationException because no SetMutator was configured.");
        } catch (final UnsupportedOperationException e) {
            // The exception is expected. Now, verify the message is correct.
            final String expectedMessage = "addAll() is not supported on CompositeSet without a SetMutator strategy";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}