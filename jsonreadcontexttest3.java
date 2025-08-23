package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for the {@link JsonReadContext#reset} method.
 */
// Renamed class for clarity and to remove redundancy ("TestTest").
class JsonReadContextTest extends JUnit5TestBase {

    @Test
    @DisplayName("reset() should correctly update the context's type and location")
    void shouldUpdateStateWhenResetIsCalled() {
        // Arrange: Create a root context and define a dummy content reference.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);
        final ContentReference contentReference = ContentReference.unknown();

        // Assert: Verify the initial state of the root context.
        assertAll("Initial Root Context State",
            () -> assertTrue(context.inRoot(), "Context should initially be in the root state"),
            () -> assertEquals("root", context.typeDesc(), "Initial type description should be 'root'"),
            () -> assertEquals(1, context.startLocation(contentReference).getLineNr(), "Initial line number should be 1"),
            () -> assertEquals(0, context.startLocation(contentReference).getColumnNr(), "Initial column number should be 0")
        );

        // Act: Reset the context with new values.
        final int newType = 200; // An arbitrary type that is not root, array, or object.
        final int newLine = 500;
        final int newCol = 200;
        context.reset(newType, newLine, newCol);

        // Assert: Verify the context state has been updated after the reset.
        assertAll("Context State After Reset",
            () -> assertFalse(context.inRoot(), "Context should no longer be in the root state"),
            () -> assertEquals("?", context.typeDesc(), "Type description for an unknown type should be '?'"),
            () -> assertEquals(newLine, context.startLocation(contentReference).getLineNr(), "Line number should be updated"),
            () -> assertEquals(newCol, context.startLocation(contentReference).getColumnNr(), "Column number should be updated")
        );
    }
}