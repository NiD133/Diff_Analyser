package org.mockito.internal.util.collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for {@link HashCodeAndEqualsSafeSet}.
 * Note: The original class name was preserved, but a more descriptive name like
 * HashCodeAndEqualsSafeSetTest would be preferable.
 */
public class HashCodeAndEqualsSafeSet_ESTestTest18 {

    /**
     * Tests that calling toString() on the set propagates exceptions thrown by an element's toString() method.
     * <p>
     * The set's toString() method internally calls toString() on each of its elements. If an element's
     * implementation is faulty and throws an exception, the set should not swallow that exception but
     * let it bubble up to the caller.
     */
    @Test(timeout = 4000)
    public void toStringShouldPropagateExceptionFromElementToString() {
        // Arrange: Create a set and add an element whose toString() method is designed to throw an exception.
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        final String expectedExceptionMessage = "toString() is designed to fail for this test.";

        Object problematicElement = new Object() {
            @Override
            public String toString() {
                throw new IllegalStateException(expectedExceptionMessage);
            }
        };
        safeSet.add(problematicElement);

        // Act & Assert: Verify that calling toString() on the set throws the expected exception.
        try {
            safeSet.toString();
            fail("Expected an IllegalStateException to be thrown, but no exception occurred.");
        } catch (IllegalStateException e) {
            // Verify that the caught exception is the one thrown from our problematic element.
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
}