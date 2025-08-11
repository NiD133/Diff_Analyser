package org.apache.commons.lang3.exception;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link DefaultExceptionContext}.
 * This test suite focuses on verifying the context's ability to add, set, and retrieve
 * values, as well as correctly format exception messages.
 */
public class DefaultExceptionContextTest {

    @Test
    public void addContextValue_shouldStoreValueAndLabel() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();

        // Act
        context.addContextValue("label1", "value1");
        final List<Object> values = context.getContextValues("label1");

        // Assert
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void setContextValue_shouldReplaceAllExistingValuesForLabel() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext()
                .addContextValue("label1", "initialValue1")
                .addContextValue("label1", "initialValue2");

        // Act
        context.setContextValue("label1", "finalValue");
        final List<Object> values = context.getContextValues("label1");

        // Assert
        assertEquals(1, values.size());
        assertEquals("finalValue", values.get(0));
    }

    @Test
    public void getFirstContextValue_shouldReturnFirstValueAddedForLabel() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext()
                .addContextValue("label", "first")
                .addContextValue("label", "second");

        // Act
        final Object firstValue = context.getFirstContextValue("label");

        // Assert
        assertEquals("first", firstValue);
    }

    @Test
    public void getFirstContextValue_shouldReturnNull_whenLabelDoesNotExist() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();

        // Act
        final Object firstValue = context.getFirstContextValue("nonexistent_label");

        // Assert
        assertNull(firstValue);
    }

    @Test
    public void getContextValues_shouldReturnAllValuesForLabel() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext()
                .addContextValue("label", "value1")
                .addContextValue("label", "value2");

        // Act
        final List<Object> values = context.getContextValues("label");

        // Assert
        assertEquals(2, values.size());
        assertTrue(values.containsAll(Arrays.asList("value1", "value2")));
    }

    @Test
    public void getContextValues_shouldReturnEmptyList_whenLabelDoesNotExist() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();

        // Act
        final List<Object> values = context.getContextValues("nonexistent_label");

        // Assert
        assertTrue(values.isEmpty());
    }

    @Test
    public void getContextLabels_shouldReturnAllUniqueLabels() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext()
                .addContextValue("label1", "valueA")
                .addContextValue("label2", "valueB")
                .addContextValue("label1", "valueC"); // Duplicate label

        // Act
        final Set<String> labels = context.getContextLabels();

        // Assert
        assertEquals(2, labels.size());
        assertTrue(labels.containsAll(Arrays.asList("label1", "label2")));
    }

    @Test
    public void getContextEntries_shouldReturnAllLabelValuePairsInOrder() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext()
                .addContextValue("label1", "valueA")
                .addContextValue("label2", "valueB");

        // Act
        final List<Pair<String, Object>> entries = context.getContextEntries();

        // Assert
        assertEquals(2, entries.size());
        assertEquals(Pair.of("label1", "valueA"), entries.get(0));
        assertEquals(Pair.of("label2", "valueB"), entries.get(1));
    }

    @Test
    public void getFormattedExceptionMessage_shouldReturnBaseMessage_whenContextIsEmpty() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String baseMessage = "An error occurred.";

        // Act
        final String formattedMessage = context.getFormattedExceptionMessage(baseMessage);

        // Assert
        assertEquals(baseMessage, formattedMessage);
    }

    @Test
    public void getFormattedExceptionMessage_shouldReturnEmptyString_whenBaseMessageIsNullAndContextIsEmpty() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();

        // Act
        final String formattedMessage = context.getFormattedExceptionMessage(null);

        // Assert
        assertEquals("", formattedMessage);
    }

    @Test
    public void getFormattedExceptionMessage_shouldAppendContextDetails() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext()
                .addContextValue("User ID", 123)
                .addContextValue("Request URL", "/api/resource");
        final String baseMessage = "Operation failed";

        // Act
        final String formattedMessage = context.getFormattedExceptionMessage(baseMessage);

        // Assert
        final String expectedMessage = "Operation failed\n" +
                "Exception Context:\n" +
                "\t[1:User ID=123]\n" +
                "\t[2:Request URL=/api/resource]\n" +
                "---------------------------------";
        assertEquals(expectedMessage, formattedMessage.replace(System.lineSeparator(), "\n"));
    }

    @Test
    public void getFormattedExceptionMessage_shouldCorrectlyFormatNullLabelAndValue() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();
        context.setContextValue(null, null);

        // Act
        final String formattedMessage = context.getFormattedExceptionMessage(null);

        // Assert
        final String expectedMessage = "Exception Context:\n" +
                "\t[1:null=null]\n" +
                "---------------------------------";
        assertEquals(expectedMessage, formattedMessage.replace(System.lineSeparator(), "\n"));
    }

    @Test
    public void getFormattedExceptionMessage_shouldThrowStackOverflowError_whenContextIsSelfReferential() {
        // Arrange
        final DefaultExceptionContext context = new DefaultExceptionContext();
        // Create a circular reference by adding the context's internal list to itself.
        final List<Pair<String, Object>> internalList = context.getContextEntries();
        context.addContextValue("recursive reference", internalList);

        // Act & Assert
        try {
            context.getFormattedExceptionMessage("Error with self-reference");
            fail("Expected a StackOverflowError to be thrown");
        } catch (final StackOverflowError e) {
            // This is the expected outcome.
        }
    }
}