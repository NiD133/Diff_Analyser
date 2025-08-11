package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link JsonReadContext}.
 */
class JsonReadContextTest extends JUnit5TestBase {

    /**
     * Helper class to verify that JsonReadContext can be extended.
     */
    private static class TestJsonReadContext extends JsonReadContext {
        public TestJsonReadContext(JsonReadContext parent, int nestingDepth, DupDetector dups,
                                   int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    @Test
    void setCurrentName_withDuplicateName_shouldThrowJsonParseException() throws Exception {
        // Arrange
        final String DUPLICATE_FIELD_NAME = "dupField";
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        // Use TYPE_OBJECT as this is a realistic context for duplicate fields
        JsonReadContext context = new JsonReadContext(null, 0, dupDetector,
                JsonStreamContext.TYPE_OBJECT, 1, 10);
        context.setCurrentName(DUPLICATE_FIELD_NAME);

        // Act & Assert
        JsonParseException e = assertThrows(JsonParseException.class, () -> {
            context.setCurrentName(DUPLICATE_FIELD_NAME);
        });

        // Verify the exception message is as expected
        verifyException(e, "Duplicate field '" + DUPLICATE_FIELD_NAME + "'");
    }

    @Test
    void setCurrentName_shouldSetAndClearName() throws Exception {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(null);

        // Act & Assert for setting a name
        context.setCurrentName("abc");
        assertEquals("abc", context.getCurrentName());

        // Act & Assert for clearing the name
        context.setCurrentName(null);
        assertNull(context.getCurrentName());
    }

    @Test
    void reset_shouldUpdateTypeAndLocationState() {
        // Arrange
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);
        final ContentReference contentRef = ContentReference.unknown();

        // Assert initial state
        assertAll("Initial Root Context State",
                () -> assertTrue(context.inRoot()),
                () -> assertEquals("root", context.typeDesc()),
                () -> assertEquals(1, context.startLocation(contentRef).getLineNr()),
                () -> assertEquals(0, context.startLocation(contentRef).getColumnNr())
        );

        // Act
        final int NEW_TYPE = 200; // An arbitrary, non-standard type
        final int NEW_LINE_NR = 500;
        final int NEW_COL_NR = 200;
        context.reset(NEW_TYPE, NEW_LINE_NR, NEW_COL_NR);

        // Assert state after reset
        assertAll("Context State After Reset",
                () -> assertFalse(context.inRoot(), "Context should no longer be in root state"),
                () -> assertEquals("?", context.typeDesc(), "Type description for unknown types should be '?'"),
                () -> assertEquals(NEW_LINE_NR, context.startLocation(contentRef).getLineNr()),
                () -> assertEquals(NEW_COL_NR, context.startLocation(contentRef).getColumnNr())
        );
    }

    // [core#1421]: Verifies that JsonReadContext can be subclassed.
    @Test
    void shouldAllowSubclassing() {
        // Arrange & Act
        TestJsonReadContext context = new TestJsonReadContext(null, 0, null, 0, 0, 0);

        // Assert
        assertNotNull(context, "Subclass instance should be created successfully");
    }
}