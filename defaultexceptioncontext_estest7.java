package org.apache.commons.lang3.exception;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail; // fail is not needed with @Test(expected=...)

/**
 * Test suite for {@link DefaultExceptionContext}.
 */
// The original class name DefaultExceptionContext_ESTestTest7 suggests it's part of a larger,
// generated test suite. A more conventional name would be DefaultExceptionContextTest.
public class DefaultExceptionContext_ESTestTest7 extends DefaultExceptionContext_ESTest_scaffolding {

    /**
     * Tests that getFormattedExceptionMessage() throws a StackOverflowError
     * when a context value creates a circular reference.
     *
     * The formatting mechanism internally calls toString() on context values. If a value
     * is a collection that contains itself, this leads to infinite recursion.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void getFormattedExceptionMessageWithSelfReferentialValueShouldThrowStackOverflowError() {
        // Arrange: Create a context where a value is the context's own entry list,
        // creating a circular reference.
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String labelForRecursiveValue = "recursive.list";

        // 1. Get the live list of context entries from the context object.
        final List<Pair<String, Object>> contextEntries = context.getContextEntries();

        // 2. Add that same list as a value back into the context.
        // Now, `contextEntries` contains itself as an element via the context.
        context.setContextValue(labelForRecursiveValue, contextEntries);

        // Act: Attempt to format the exception message. This should cause infinite
        // recursion when the formatter tries to convert `contextEntries` to a string.
        context.getFormattedExceptionMessage("Base error message");

        // Assert: The test will fail automatically if a StackOverflowError is not thrown.
        // The 'expected' parameter in the @Test annotation handles the assertion.
    }
}