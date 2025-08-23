package org.apache.commons.lang3.exception;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests for DefaultExceptionContext focusing on:
 * - Adding and retrieving context values.
 * - Context labels and entries exposure.
 * - Formatting behavior with and without base messages.
 * - Edge cases (null label/value, self-referential values).
 *
 * These tests avoid EvoSuite-specific runtime scaffolding and use clear,
 * behavior-focused names and assertions for maintainability.
 */
public class DefaultExceptionContextTest {

    @Test
    public void addContextValue_thenGetFirst_returnsAddedValue() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();
        String label = "userId";
        String value = "42";

        // Act
        ctx.addContextValue(label, value);
        Object first = ctx.getFirstContextValue(label);

        // Assert
        assertEquals(value, first);
    }

    @Test
    public void addContextValue_thenLabelsContainLabel() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();
        String label = "operation";

        // Act
        ctx.addContextValue(label, "CREATE");
        Set<String> labels = ctx.getContextLabels();

        // Assert
        assertFalse(labels.isEmpty());
        assertTrue(labels.contains(label));
    }

    @Test
    public void addContextValue_thenEntriesListHasOnePair() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();

        // Act
        ctx.addContextValue("key", "value");
        List<Pair<String, Object>> entries = ctx.getContextEntries();

        // Assert
        assertEquals(1, entries.size());
        assertEquals("key", entries.get(0).getLeft());
        assertEquals("value", entries.get(0).getRight());
    }

    @Test
    public void getFormattedMessage_withNullBaseAndNullEntry_containsContextBlock() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();
        // Setting a null label and null value is allowed by the API
        ctx.setContextValue(null, null);

        // Act
        String formatted = ctx.getFormattedExceptionMessage(null);

        // Assert
        // Avoid brittle full-string comparison; check essential parts.
        assertNotNull(formatted);
        assertTrue(formatted.startsWith("Exception Context:"));
        assertTrue("Expected the single null entry to be present",
                formatted.contains("[1:null=null]"));
    }

    @Test
    public void getFormattedMessage_withBaseOnly_returnsBase() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();
        String base = "Something went wrong";

        // Act
        String formatted = ctx.getFormattedExceptionMessage(base);

        // Assert
        assertEquals(base, formatted);
    }

    @Test
    public void getFormattedMessage_withNullBaseAndNoContext_returnsEmptyString() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();

        // Act
        String formatted = ctx.getFormattedExceptionMessage(null);

        // Assert
        assertEquals("", formatted);
    }

    /**
     * Documents current behavior when a context value is self-referential.
     * The formatting walks object graphs and, with a self-referential value,
     * it currently overflows the stack. If the implementation becomes cycle-safe,
     * this test can be removed or updated to assert non-crashing behavior.
     */
    @Test(expected = StackOverflowError.class)
    public void getFormattedMessage_withSelfReferentialValue_causesStackOverflow_currentBehavior() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();
        // Get the live entries list and store it as a context value.
        // When formatting, rendering this list attempts to render the pair,
        // which references the same list, causing recursion.
        List<Pair<String, Object>> entries = ctx.getContextEntries();
        ctx.setContextValue("self", entries);

        // Act
        ctx.getFormattedExceptionMessage("self");
    }

    @Test
    public void getContextValues_forMissingLabel_returnsEmptyList() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();

        // Act
        List<Object> values = ctx.getContextValues("missing");

        // Assert
        assertNotNull(values);
        assertTrue(values.isEmpty());
    }

    @Test
    public void addContextValue_thenGetContextValuesReturnsThatValue() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();
        String label = "status";
        String value = "OK";

        // Act
        ctx.addContextValue(label, value);
        List<Object> values = ctx.getContextValues(label);

        // Assert
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void getFirstContextValue_forMissingLabel_returnsNull() {
        // Arrange
        DefaultExceptionContext ctx = new DefaultExceptionContext();

        // Act
        Object first = ctx.getFirstContextValue("unknown");

        // Assert
        assertNull(first);
    }
}