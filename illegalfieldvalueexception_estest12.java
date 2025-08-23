package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * This test class focuses on the exception-handling behavior of the
 * IllegalFieldValueException's constructors.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Verifies that the constructor throws a NullPointerException when a null
     * DateTimeFieldType is provided.
     * <p>
     * The constructor is expected to fail because it internally attempts to call
     * {@code .getName()} on the null field type, which results in an NPE.
     */
    @Test
    public void constructor_shouldThrowNullPointerException_whenDateTimeFieldTypeIsNull() {
        // Arrange
        final Number dummyValue = 1580L;
        final Number dummyLowerBound = 1L;
        final Number dummyUpperBound = 2000L;
        final DateTimeFieldType nullDateTimeFieldType = null;

        // Act & Assert
        // We expect a NullPointerException because the constructor tries to access the
        // name of the null DateTimeFieldType.
        NullPointerException thrown = assertThrows(
            NullPointerException.class,
            () -> new IllegalFieldValueException(nullDateTimeFieldType, dummyValue, dummyLowerBound, dummyUpperBound)
        );

        // A standard NullPointerException thrown by the JVM in this way has no message.
        // This assertion confirms that expected behavior.
        assertNull("The NullPointerException should not have a message.", thrown.getMessage());
    }
}