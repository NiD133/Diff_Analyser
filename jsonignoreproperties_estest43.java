package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test focuses on the serialization-related behavior of the {@link JsonIgnoreProperties.Value} class.
 */
// The original test class name and inheritance are kept to match the provided context.
public class JsonIgnoreProperties_ESTestTest43 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that the {@code readResolve()} method on the canonical {@code JsonIgnoreProperties.Value.EMPTY}
     * instance correctly returns the singleton instance itself.
     * <p>
     * The {@code readResolve()} method is a standard Java serialization mechanism used to ensure
     * that when an object is deserialized, a specific instance (like a singleton) is returned,
     * preventing duplicate instances.
     */
    @Test(timeout = 4000)
    public void readResolveOnEmptyValueShouldReturnCanonicalEmptyInstance() {
        // Arrange: The object under test is the canonical EMPTY singleton instance.
        JsonIgnoreProperties.Value emptyValue = JsonIgnoreProperties.Value.EMPTY;

        // Act: Call the readResolve() method, which simulates the final step of deserialization.
        Object resolvedObject = emptyValue.readResolve();

        // Assert: The method should return the exact same singleton instance.
        assertSame("readResolve() must return the canonical EMPTY singleton to preserve its identity.",
                JsonIgnoreProperties.Value.EMPTY, resolvedObject);

        // Further assertions to confirm the state of the returned instance is as expected.
        assertTrue("The resolved object must be an instance of JsonIgnoreProperties.Value",
                resolvedObject instanceof JsonIgnoreProperties.Value);
        JsonIgnoreProperties.Value resolvedValue = (JsonIgnoreProperties.Value) resolvedObject;

        assertFalse("The 'ignoreUnknown' property should be false for the EMPTY instance",
                resolvedValue.getIgnoreUnknown());
        assertTrue("The set of ignored properties should be empty for the EMPTY instance",
                resolvedValue.getIgnored().isEmpty());
    }
}