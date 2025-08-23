package org.apache.commons.collections4.properties;

import org.junit.Test;
import java.util.function.BiFunction;

/**
 * Tests for the merge() method in {@link OrderedProperties}.
 */
// The class name and inheritance are preserved from the original test structure.
public class OrderedProperties_ESTestTest17 extends OrderedProperties_ESTest_scaffolding {

    /**
     * Tests that calling merge() with a null remapping function throws a NullPointerException,
     * as specified by the java.util.Map interface contract.
     */
    @Test(expected = NullPointerException.class)
    public void mergeWithNullRemappingFunctionShouldThrowNullPointerException() {
        // Arrange: Create an instance of OrderedProperties and define test data.
        final OrderedProperties properties = new OrderedProperties();
        final String key = "anyKey";
        final String value = "anyValue";
        final BiFunction<Object, Object, Object> nullRemappingFunction = null;

        // Act: Call the merge method with a null function.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        properties.merge(key, value, nullRemappingFunction);
    }
}