package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

/**
 * Contains tests for the {@link CompositeSet} class, focusing on specific edge cases.
 */
public class CompositeSetTest {

    /**
     * Tests that calling toArray(T[] array) with an array of an incompatible
     * component type throws an ArrayStoreException.
     *
     * The CompositeSet contains Integers, but the method attempts to place them
     * into an array of Strings, which is not allowed.
     */
    @Test(expected = ArrayStoreException.class)
    public void toArrayWithIncompatibleArrayTypeShouldThrowArrayStoreException() {
        // Arrange
        // Create a composite set containing a single integer element.
        Set<Integer> componentSet = Collections.singleton(42);
        CompositeSet<Integer> compositeSet = new CompositeSet<>(componentSet);

        // Create an array with a component type (String) that is not a supertype
        // of the set's element type (Integer).
        String[] incompatibleArray = new String[1];

        // Act & Assert
        // This call is expected to throw an ArrayStoreException because an Integer
        // cannot be stored in a String array.
        compositeSet.toArray(incompatibleArray);
    }
}