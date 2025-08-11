package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link JsonReadContext}.
 */
class JsonReadContextTest extends JUnit5TestBase
{
    // Test helper class to verify JsonReadContext can be extended
    static class TestableJsonReadContext extends JsonReadContext {
        public TestableJsonReadContext(JsonReadContext parent, int nestingDepth, DupDetector dups,
                                     int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    @Test
    void shouldThrowExceptionWhenSettingSameDuplicateFieldName() throws Exception {
        // Given: A JsonReadContext with duplicate detection enabled
        DupDetector duplicateDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext context = createContextWithDuplicateDetection(duplicateDetector);
        
        // When: Setting the same field name twice
        String duplicateFieldName = "dupField";
        context.setCurrentName(duplicateFieldName);
        
        // Then: Should throw JsonParseException for duplicate field
        JsonParseException exception = assertThrows(JsonParseException.class, () -> {
            context.setCurrentName(duplicateFieldName);
        });
        
        verifyException(exception, "Duplicate field 'dupField'");
    }

    @Test
    void shouldSetAndGetCurrentNameCorrectly() throws Exception {
        // Given: A root JsonReadContext without duplicate detection
        JsonReadContext context = JsonReadContext.createRootContext(0, 0, null);
        
        // When: Setting a field name
        String fieldName = "abc";
        context.setCurrentName(fieldName);
        
        // Then: Should return the same field name
        assertEquals(fieldName, context.getCurrentName());
        
        // When: Setting name to null
        context.setCurrentName(null);
        
        // Then: Should return null
        assertNull(context.getCurrentName());
    }

    @Test
    void shouldResetContextStateCorrectly() {
        // Given: A root context with duplicate detection
        DupDetector duplicateDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext context = JsonReadContext.createRootContext(duplicateDetector);
        ContentReference contentReference = ContentReference.unknown();

        // Verify initial root context state
        assertTrue(context.inRoot());
        assertEquals("root", context.typeDesc());
        assertEquals(1, context.startLocation(contentReference).getLineNr());
        assertEquals(0, context.startLocation(contentReference).getColumnNr());

        // When: Resetting context with new position values
        int newType = 200;
        int newLineNumber = 500;
        int newColumnNumber = 200;
        context.reset(newType, newLineNumber, newColumnNumber);

        // Then: Context should reflect the new state
        assertFalse(context.inRoot());
        assertEquals("?", context.typeDesc()); // Unknown type description
        assertEquals(newLineNumber, context.startLocation(contentReference).getLineNr());
        assertEquals(newColumnNumber, context.startLocation(contentReference).getColumnNr());
    }

    /**
     * Test for issue #1421 - Verifies that JsonReadContext can be extended
     */
    @Test
    void shouldAllowContextExtension() {
        // Given: Parameters for creating an extended context
        JsonReadContext parentContext = null;
        int nestingDepth = 0;
        DupDetector duplicateDetector = null;
        int contextType = 0;
        int lineNumber = 0;
        int columnNumber = 0;
        
        // When: Creating an extended JsonReadContext
        TestableJsonReadContext extendedContext = new TestableJsonReadContext(
            parentContext, nestingDepth, duplicateDetector, 
            contextType, lineNumber, columnNumber
        );
        
        // Then: Should successfully create the extended context
        assertNotNull(extendedContext);
    }

    // Helper method to create a context with duplicate detection for cleaner test setup
    private JsonReadContext createContextWithDuplicateDetection(DupDetector duplicateDetector) {
        return new JsonReadContext(
            null,           // parent context
            0,              // nesting depth
            duplicateDetector,
            2441,           // context type
            2441,           // line number
            2441            // column number
        );
    }
}