package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Contains tests for the custom Java serialization logic of the {@link SerializedString} class.
 */
public class SerializedStringSerializationTest {

    /**
     * Verifies that the {@code readResolve} method correctly constructs a new
     * {@link SerializedString} instance from the transient {@code _jdkSerializeValue} field.
     *
     * <p><b>Background:</b> {@link SerializedString} uses a custom serialization approach
     * because its primary {@code _value} field is final. During deserialization, the
     * JVM calls {@code readObject()}, which reads the string value into the transient
     * {@code _jdkSerializeValue} field. Immediately after, the JVM calls {@code readResolve()},
     * which is responsible for creating the final, correctly initialized object.
     *
     * <p>This test simulates this process by manually setting {@code _jdkSerializeValue}
     * and then calling {@code readResolve()} to ensure it behaves as expected.
     */
    @Test
    public void readResolve_shouldCreateNewInstanceFromInternalValue() {
        // Arrange: Create an initial instance. The value "original" is a placeholder and
        // is irrelevant to the outcome, as readResolve() does not use it.
        SerializedString stringDuringDeserialization = new SerializedString("original");

        // Simulate the state of the object after readObject() has run during deserialization.
        // We set the transient field that readResolve() is expected to use.
        final String expectedValue = "";
        stringDuringDeserialization._jdkSerializeValue = expectedValue;

        // Act: Manually invoke readResolve() to get the final object, just as the
        // JVM would during the final step of deserialization.
        Object resolvedObject = stringDuringDeserialization.readResolve();

        // Assert: The resolved object should be a new, distinct SerializedString instance
        // containing the value from _jdkSerializeValue.
        SerializedString finalString = (SerializedString) resolvedObject;

        assertEquals(expectedValue, finalString.getValue());
        assertNotSame("readResolve should return a new instance",
                stringDuringDeserialization, finalString);
    }
}