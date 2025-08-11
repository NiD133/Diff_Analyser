package org.apache.commons.lang3.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.ObjectToStringRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for DefaultExceptionContext that focus on how formatted messages are produced
 * when context values are null or their toString() implementations throw exceptions.
 */
class DefaultExceptionContextTest extends AbstractExceptionContextTest<DefaultExceptionContext> {

    private static final String LABEL_THROWS_1 = "throws 1";
    private static final String LABEL_THROWS_2 = "throws 2";

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        // Ensure a fresh context for each test and let the superclass perform any shared setup.
        exceptionContext = new DefaultExceptionContext();
        super.setUp();
    }

    @Test
    void shouldIncludeLabelsWhenValueToStringThrows() {
        // Arrange: Add values whose toString() will throw at formatting time.
        exceptionContext.addContextValue(LABEL_THROWS_1, new ObjectToStringRuntimeException(LABEL_THROWS_1));
        exceptionContext.addContextValue(LABEL_THROWS_2, new ObjectToStringRuntimeException(LABEL_THROWS_2));

        // Act
        final String message = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);

        // Assert: The base message is preserved and labels are present, even if value toString() fails.
        assertMessageStartsWithAndContainsLabels(message, TEST_MESSAGE, LABEL_THROWS_1, LABEL_THROWS_2);
    }

    @Test
    void shouldReturnEmptyStringWhenBaseMessageIsNull() {
        // Act
        final String message = exceptionContext.getFormattedExceptionMessage(null);

        // Assert
        assertEquals("", message);
    }

    @Test
    void shouldIncludeLabelsWhenValuesAreNull() {
        // Arrange: Add labels with null values.
        exceptionContext.addContextValue(LABEL_THROWS_1, null);
        exceptionContext.addContextValue(LABEL_THROWS_2, null);

        // Act
        final String message = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);

        // Assert: The base message is preserved and labels are present for null values.
        assertMessageStartsWithAndContainsLabels(message, TEST_MESSAGE, LABEL_THROWS_1, LABEL_THROWS_2);
    }

    private static void assertMessageStartsWithAndContainsLabels(
            final String actual,
            final String expectedPrefix,
            final String... labels) {
        assertTrue(actual.startsWith(expectedPrefix), "Message must start with the base message");
        for (final String label : labels) {
            assertTrue(actual.contains(label), "Message must contain label: " + label);
        }
    }
}