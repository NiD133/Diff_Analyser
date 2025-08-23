package org.apache.commons.collections4.properties;

import org.junit.Test;
import java.util.function.Function;

/**
 * Tests for {@link OrderedProperties} focusing on exception handling in compute methods.
 */
public class OrderedProperties_ESTestTest20 {

    /**
     * Tests that {@link OrderedProperties#computeIfAbsent(Object, Function)} propagates a
     * StackOverflowError thrown by the mapping function. This ensures that the method
     * correctly handles severe runtime errors originating from user-provided logic without
     * swallowing them or entering an inconsistent state.
     */
    @Test(expected = StackOverflowError.class)
    public void computeIfAbsentShouldPropagateStackOverflowErrorFromMappingFunction() {
        // Arrange
        OrderedProperties orderedProperties = new OrderedProperties();
        Object key = "anyKey";

        // Create a mapping function that will cause a StackOverflowError by calling itself indefinitely.
        // This simulates a faulty mapping function provided by a user.
        Function<Object, Object> recursiveMappingFunction = new Function<Object, Object>() {
            @Override
            public Object apply(Object o) {
                // This infinite recursion will trigger a StackOverflowError.
                return this.apply(o);
            }
        };

        // Act & Assert
        // The call to computeIfAbsent should execute the faulty mapping function because the key
        // is not present. The test expects the resulting StackOverflowError to be thrown.
        orderedProperties.computeIfAbsent(key, recursiveMappingFunction);
    }
}