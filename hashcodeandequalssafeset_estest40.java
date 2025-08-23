package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
// The original test class name "HashCodeAndEqualsSafeSet_ESTestTest40" was auto-generated.
// A more conventional name would be "HashCodeAndEqualsSafeSetTest".
public class HashCodeAndEqualsSafeSetTest {

    /**
     * Verifies that addAll() throws an IllegalArgumentException when passed a null collection,
     * as this is a violation of the method's contract.
     */
    @Test
    public void addAll_shouldThrowIllegalArgumentException_whenCollectionIsNull() {
        // Arrange
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();

        // Act & Assert
        try {
            safeSet.addAll((Collection<?>) null);
            fail("Expected an IllegalArgumentException to be thrown for a null collection.");
        } catch (IllegalArgumentException e) {
            // This is the expected outcome.
            // We can also verify the exception message for more precise testing.
            assertEquals("Passed collection should not be null", e.getMessage());
        }
    }
}