package org.apache.commons.collections4.properties;

import org.junit.Test;

import java.util.Set;
import java.util.function.BiFunction;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link OrderedProperties} class.
 * Note: The original test class name and inheritance structure are preserved for context.
 */
public class OrderedProperties_ESTestTest16 extends OrderedProperties_ESTest_scaffolding {

    /**
     * Tests that a StackOverflowError is thrown when attempting to use the property set's
     * own keySet as a key in a merge operation.
     * <p>
     * This self-referential operation causes an infinite recursion. The merge operation
     * attempts to add the keySet to the underlying map. This requires calculating the
     * key's hash code. The hash code calculation for the keySet iterates over its
     * elements. If the keySet contains itself, this leads to a recursive call
     * to {@code hashCode()}, ultimately causing a StackOverflowError.
     */
    @Test(timeout = 4000)
    public void mergeWithKeySetAsKeyThrowsStackOverflowError() {
        // Arrange: Create properties and get its keySet, which will be used as a self-referential key.
        final OrderedProperties properties = new OrderedProperties();
        final Set<Object> keySetAsKey = properties.keySet();
        final String value = "anyValue";
        final BiFunction<Object, Object, Object> remappingFunction = (oldValue, newValue) -> newValue;

        // Act & Assert: Expect a StackOverflowError when merging the keySet into itself.
        try {
            properties.merge(keySetAsKey, value, remappingFunction);
            fail("A StackOverflowError was expected but not thrown.");
        } catch (final StackOverflowError e) {
            // This is the expected outcome, so the test passes.
        }
    }
}