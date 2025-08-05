package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JsonReadContext}.
 */
class JsonReadContextTest extends JUnit5TestBase {
    static class MyContext extends JsonReadContext {
        public MyContext(JsonReadContext parent, int nestingDepth, DupDetector dups,
                         int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    private static final int ARBITRARY_LINE = 100;
    private static final int ARBITRARY_COLUMN = 200;
    private static final String DUPLICATE_FIELD_NAME = "dupField";
    private static final String TEST_FIELD_NAME = "abc";
    private static final ContentReference BOGUS_SOURCE = ContentReference.unknown();

    @Test
    void settingDuplicateFieldNameThrowsException() throws Exception {
        // Setup context with duplicate detection
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext context = new JsonReadContext(
            null, 
            0,
            dupDetector, 
            JsonToken.START_OBJECT, 
            ARBITRARY_LINE, 
            ARBITRARY_COLUMN
        );

        context.setCurrentName(DUPLICATE_FIELD_NAME);

        // Verify duplicate field detection
        JsonParseException exception = assertThrows(JsonParseException.class,
            () -> context.setCurrentName(DUPLICATE_FIELD_NAME),
            "Should throw when setting duplicate field name"
        );
        verifyException(exception, "Duplicate field '" + DUPLICATE_FIELD_NAME + "'");
    }

    @Test
    void settingCurrentNameUpdatesStateCorrectly() throws Exception {
        JsonReadContext context = JsonReadContext.createRootContext(0, 0, (DupDetector) null);
        
        // Set valid name and verify
        context.setCurrentName(TEST_FIELD_NAME);
        assertEquals(TEST_FIELD_NAME, context.getCurrentName(), 
            "Current name should match last set value");
        
        // Set null and verify
        context.setCurrentName(null);
        assertNull(context.getCurrentName(), 
            "Current name should be clearable with null");
    }

    @Test
    void resetUpdatesContextStateAndLocation() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);

        // Verify initial root state
        assertTrue(context.inRoot(), "Should be in root context initially");
        assertEquals("root", context.typeDesc(), "Root context type description");
        assertEquals(1, context.startLocation(BOGUS_SOURCE).getLineNr(), 
            "Initial root line number");
        assertEquals(0, context.startLocation(BOGUS_SOURCE).getColumnNr(), 
            "Initial root column number");

        // Reset to non-root state
        final int NEW_TYPE = JsonToken.START_ARRAY;
        final int NEW_LINE = 500;
        final int NEW_COLUMN = 200;
        context.reset(NEW_TYPE, NEW_LINE, NEW_COLUMN);

        // Verify updated state
        assertFalse(context.inRoot(), "Should not be in root after reset");
        assertEquals("?", context.typeDesc(), "Non-root context type description");
        assertEquals(NEW_LINE, context.startLocation(BOGUS_SOURCE).getLineNr(), 
            "Updated line number should match reset value");
        assertEquals(NEW_COLUMN, context.startLocation(BOGUS_SOURCE).getColumnNr(), 
            "Updated column number should match reset value");
    }

    @Test
    void contextExtensionCanBeInstantiated() {
        // Test for core#1421 - verify custom context creation
        MyContext context = new MyContext(null, 0, null, 0, 0, 0);
        assertNotNull(context, "Custom context extension should be instantiable");
    }
}