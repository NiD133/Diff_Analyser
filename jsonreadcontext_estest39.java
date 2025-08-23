package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonReadContext} class.
 *
 * Note: The original test class name `JsonReadContext_ESTestTest39` and its
 * inheritance from a scaffolding class suggest it was auto-generated. This
 * refactored version uses a standard naming convention and is self-contained.
 */
public class JsonReadContext_ESTestTest39 {

    /**
     * Tests that `expectComma()` correctly returns true after the first element
     * in an object context has been processed.
     * <p>
     * The `expectComma()` method is used by the parser to determine if a comma
     * is expected before the next value. It should return false for the very
     * first element in an array or object, and true for all subsequent elements.
     */
    @Test
    public void expectCommaShouldReturnTrueForSecondElementInObjectContext() {
        // Arrange: Create a child object context.
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        // Use the constant for the object type instead of the magic number '2'.
        JsonReadContext objectContext = new JsonReadContext(rootContext, null, JsonStreamContext.TYPE_OBJECT, 2, 1);

        // Act:
        // 1. Simulate moving to the first element. The context's index starts at -1,
        //    so this call increments it to 0. `expectComma` returns false here,
        //    as no comma is needed before the first element.
        objectContext.expectComma();

        // 2. Simulate moving to the second element. This call increments the index to 1.
        //    A comma is expected before the second element, so the method should return true.
        boolean isCommaExpected = objectContext.expectComma();

        // Assert:
        // Verify that a comma is now expected and the context's index is correct.
        assertTrue("A comma should be expected before the second element in an object", isCommaExpected);
        assertEquals("The index should be 1 after advancing to the second element", 1, objectContext.getCurrentIndex());
    }
}