/*
 * Test suite for DefaultExceptionContext class
 * Tests the functionality of adding, retrieving, and formatting context information
 * for exceptions in Apache Commons Lang3.
 */

package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.exception.DefaultExceptionContext;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultExceptionContextTest {

    @Test
    public void shouldReturnFirstContextValueWhenLabelExists() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        String label = "errorCode";
        String expectedValue = "E001";
        
        // When
        context.addContextValue(label, expectedValue);
        Object actualValue = context.getFirstContextValue(label);
        
        // Then
        assertEquals("Should return the first value for the given label", expectedValue, actualValue);
    }

    @Test
    public void shouldReturnAllContextLabelsWhenContextValuesExist() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        String label = "userId";
        String value = "12345";
        
        // When
        context.addContextValue(label, value);
        Set<String> labels = context.getContextLabels();
        
        // Then
        assertFalse("Context labels should not be empty when values are added", labels.isEmpty());
        assertTrue("Should contain the added label", labels.contains(label));
    }

    @Test
    public void shouldReturnContextEntriesWhenValuesAreAdded() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        String label = "operation";
        String value = "fileRead";
        
        // When
        context.addContextValue(label, value);
        List<Pair<String, Object>> entries = context.getContextEntries();
        
        // Then
        assertEquals("Should contain exactly one context entry", 1, entries.size());
        assertEquals("Entry should have correct label", label, entries.get(0).getLeft());
        assertEquals("Entry should have correct value", value, entries.get(0).getRight());
    }

    @Test
    public void shouldFormatExceptionMessageWithNullContext() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        String nullLabel = null;
        Object nullValue = null;
        
        // When
        context.setContextValue(nullLabel, nullValue);
        String formattedMessage = context.getFormattedExceptionMessage(null);
        
        // Then
        String expectedMessage = "Exception Context:\n\t[1:null=null]\n---------------------------------";
        assertEquals("Should format message correctly with null values", expectedMessage, formattedMessage);
    }

    @Test
    public void shouldReturnBaseMessageWhenNoContextExists() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        String baseMessage = "File not found";
        
        // When
        String formattedMessage = context.getFormattedExceptionMessage(baseMessage);
        
        // Then
        assertEquals("Should return base message unchanged when no context exists", 
                    baseMessage, formattedMessage);
    }

    @Test
    public void shouldReturnEmptyStringWhenNoMessageAndNoContext() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        
        // When
        String formattedMessage = context.getFormattedExceptionMessage(null);
        
        // Then
        assertEquals("Should return empty string when no message and no context", "", formattedMessage);
    }

    @Test
    public void shouldThrowStackOverflowErrorWhenCircularReferenceExists() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        List<Pair<String, Object>> entries = context.getContextEntries();
        String label = "circularRef";
        
        // When
        context.setContextValue(label, entries); // Creates circular reference
        
        // Then
        try {
            context.getFormattedExceptionMessage("Test message");
            fail("Should throw StackOverflowError due to circular reference");
        } catch (StackOverflowError e) {
            // Expected behavior - circular reference causes infinite recursion
        }
    }

    @Test
    public void shouldReturnEmptyListWhenLabelDoesNotExist() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        String nonExistentLabel = "nonExistentKey";
        
        // When
        List<Object> values = context.getContextValues(nonExistentLabel);
        
        // Then
        assertNotNull("Should return non-null list", values);
        assertTrue("Should return empty list for non-existent label", values.isEmpty());
        assertFalse("Should not contain the label itself", values.contains(nonExistentLabel));
    }

    @Test
    public void shouldReturnAllValuesForGivenLabel() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        String label = "attempts";
        Set<String> contextLabels = context.getContextLabels();
        
        // When
        context.addContextValue(label, contextLabels);
        List<Object> values = context.getContextValues(label);
        
        // Then
        assertEquals("Should return exactly one value for the label", 1, values.size());
        assertEquals("Should return the correct value", contextLabels, values.get(0));
    }

    @Test
    public void shouldReturnNullWhenFirstContextValueDoesNotExist() {
        // Given
        DefaultExceptionContext context = new DefaultExceptionContext();
        String nonExistentLabel = "missingKey";
        
        // When
        Object value = context.getFirstContextValue(nonExistentLabel);
        
        // Then
        assertNull("Should return null for non-existent label", value);
    }
}