package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains an improved version of a generated test case
 * for the {@link CompositeSet} class.
 */
public class CompositeSet_ESTestTest47 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that the equals() method of a CompositeSet returns false when
     * compared with an object that is not a Set.
     *
     * This adheres to the contract of the java.util.Set interface, which
     * specifies that for equals() to be true, the other object must also be a Set.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithNonSetObject() {
        // Arrange: Create an empty CompositeSet and a non-Set object for comparison.
        CompositeSet<Object> compositeSet = new CompositeSet<>();
        Object nonSetObject = new Object();

        // Act: Call the equals() method.
        boolean result = compositeSet.equals(nonSetObject);

        // Assert: Verify that the result is false, as expected.
        assertFalse("A CompositeSet should not be equal to an object that is not a Set.", result);
    }
}