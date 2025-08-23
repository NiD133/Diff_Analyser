package org.apache.commons.collections4.properties;

import org.junit.Test;

import java.util.Set;
import java.util.function.BiConsumer;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Test suite for {@link OrderedProperties}.
 * This class contains a test case focusing on edge-case behavior.
 */
// The original test class name is preserved for context.
public class OrderedProperties_ESTestTest18 extends OrderedProperties_ESTest_scaffolding {

    /**
     * Tests that calling forEach on an OrderedProperties instance containing its own keySet as a key
     * results in a StackOverflowError.
     *
     * <p>This scenario creates a self-referential data structure. When forEach is called,
     * an operation on the key/value pair (like toString() or hashCode(), which might be invoked
     * by the underlying implementation or a mocking framework) can lead to infinite recursion,
     * culminating in a StackOverflowError.</p>
     */
    @Test(timeout = 4000)
    public void forEachOnPropertiesWithSelfReferentialKeyShouldCauseStackOverflow() {
        // Arrange: Create an OrderedProperties instance and make it self-referential
        // by adding its own keySet as a key.
        OrderedProperties properties = new OrderedProperties();
        Set<Object> keySet = properties.keySet();
        properties.putIfAbsent(keySet, keySet); // The keySet now contains itself as an element.

        @SuppressWarnings("unchecked")
        BiConsumer<Object, Object> mockConsumer = mock(BiConsumer.class);

        // Act & Assert: Expect a StackOverflowError when forEach is called on the
        // self-referential properties object.
        try {
            properties.forEach(mockConsumer);
            fail("Expected a StackOverflowError due to the self-referential keySet.");
        } catch (StackOverflowError e) {
            // This is the expected outcome. The operation on the self-referential
            // structure causes infinite recursion.
        }
    }
}