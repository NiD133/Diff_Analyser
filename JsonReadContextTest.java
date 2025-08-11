package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link JsonReadContext}.
 * 
 * Goals:
 * - Use clear Arrange-Act-Assert structure
 * - Prefer expressive assertions and names
 * - Remove magic numbers and unnecessary boilerplate
 * - Keep tests focused on behavior, not implementation details
 */
class JsonReadContextTest extends JUnit5TestBase {

    private static final String DUP_FIELD = "dupField";
    private static final ContentReference UNKNOWN_SRC = ContentReference.unknown();

    /**
     * Simple subclass used to validate that JsonReadContext remains extensible.
     */
    static class MyContext extends JsonReadContext {
        public MyContext(JsonReadContext parent, int nestingDepth, DupDetector dups,
                         int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    // Helper: object context with duplicate detection enabled
    private static JsonReadContext newObjectContextWithDupDetector() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext root = JsonReadContext.createRootContext(dupDetector);
        return root.createChildObjectContext(1, 1);
    }

    @Test
    void setCurrentName_whenCalledTwiceWithSameName_throwsJsonParseException() throws Exception {
        // Arrange
        JsonReadContext ctx = newObjectContextWithDupDetector();
        ctx.setCurrentName(DUP_FIELD);

        // Act + Assert
        JsonParseException ex = assertThrows(JsonParseException.class,
                () -> ctx.setCurrentName(DUP_FIELD),
                "Setting the same field name twice in the same object should be detected as duplicate");
        assertTrue(ex.getMessage().contains("Duplicate field 'dupField'"),
                "Exception message should mention the duplicate field name");
    }

    @Test
    void setCurrentName_updatesAndClearsName() throws Exception {
        // Arrange
        JsonReadContext ctx = JsonReadContext.createRootContext(0, 0, null);

        // Act + Assert
        ctx.setCurrentName("abc");
        assertEquals("abc", ctx.getCurrentName(), "Current name should reflect the last set value");

        ctx.setCurrentName(null);
        assertNull(ctx.getCurrentName(), "Current name should be cleared when set to null");
    }

    @Test
    void reset_updatesLocationAndTypeState() {
        // Arrange
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext ctx = JsonReadContext.createRootContext(dupDetector);

        // Assert initial root state
        assertTrue(ctx.inRoot(), "Freshly created context should be in root");
        assertEquals("root", ctx.typeDesc(), "Type description should be 'root' at start");
        JsonLocation start = ctx.startLocation(UNKNOWN_SRC);
        assertEquals(1, start.getLineNr(), "Root line number should default to 1");
        assertEquals(0, start.getColumnNr(), "Root column number should default to 0");

        // Act
        final int newType = 999; // arbitrary type value; not OBJECT/ARRAY, so desc becomes "?"
        final int newLine = 500;
        final int newCol = 200;
        ctx.reset(newType, newLine, newCol);

        // Assert post-reset state
        assertFalse(ctx.inRoot(), "Context should no longer be considered root after reset");
        assertEquals("?", ctx.typeDesc(), "Unknown/non-container type should render as '?'");
        JsonLocation afterReset = ctx.startLocation(UNKNOWN_SRC);
        assertEquals(newLine, afterReset.getLineNr(), "Line number should reflect reset value");
        assertEquals(newCol, afterReset.getColumnNr(), "Column number should reflect reset value");
    }

    // [core#1421] Ensure subclassing via public constructor remains possible
    @Test
    void supportsSubclassingViaPublicConstructor() {
        MyContext context = new MyContext(null, 0, null, 0, 0, 0);
        assertNotNull(context, "Subclass instance should be constructible");
    }
}