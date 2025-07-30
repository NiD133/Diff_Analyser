package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link JsonReadContext}.
 */
class JsonReadContextTest extends JUnit5TestBase {

    /**
     * Custom context class extending JsonReadContext for testing purposes.
     */
    static class MyContext extends JsonReadContext {
        public MyContext(JsonReadContext parent, int nestingDepth, DupDetector dups,
                         int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    /**
     * Test that setting the same field name twice raises a JsonParseException.
     */
    @Test
    void testSetCurrentNameTwiceWithSameNameRaisesJsonParseException() throws Exception {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext jsonReadContext = new JsonReadContext(null, 0, dupDetector, 0, 0, 0);

        jsonReadContext.setCurrentName("dupField");

        Exception exception = assertThrows(JsonParseException.class, () -> {
            jsonReadContext.setCurrentName("dupField");
        });

        verifyException(exception, "Duplicate field 'dupField'");
    }

    /**
     * Test setting and getting the current field name.
     */
    @Test
    void testSetCurrentName() throws Exception {
        JsonReadContext jsonReadContext = JsonReadContext.createRootContext(0, 0, null);

        jsonReadContext.setCurrentName("abc");
        assertEquals("abc", jsonReadContext.getCurrentName());

        jsonReadContext.setCurrentName(null);
        assertNull(jsonReadContext.getCurrentName());
    }

    /**
     * Test resetting the context and verifying its state.
     */
    @Test
    void testResetContext() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext jsonReadContext = JsonReadContext.createRootContext(dupDetector);
        final ContentReference unknownSource = ContentReference.unknown();

        assertTrue(jsonReadContext.inRoot());
        assertEquals("root", jsonReadContext.typeDesc());
        assertEquals(1, jsonReadContext.startLocation(unknownSource).getLineNr());
        assertEquals(0, jsonReadContext.startLocation(unknownSource).getColumnNr());

        jsonReadContext.reset(200, 500, 200);

        assertFalse(jsonReadContext.inRoot());
        assertEquals("?", jsonReadContext.typeDesc());
        assertEquals(500, jsonReadContext.startLocation(unknownSource).getLineNr());
        assertEquals(200, jsonReadContext.startLocation(unknownSource).getColumnNr());
    }

    /**
     * Test creating an instance of the custom context class.
     */
    @Test
    void testCustomContextExtension() {
        MyContext context = new MyContext(null, 0, null, 0, 0, 0);
        assertNotNull(context);
    }
}