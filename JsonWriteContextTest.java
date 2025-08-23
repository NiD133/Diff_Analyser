package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests focused on understandability of how JsonWriteContext can be subclassed and used.
 * We create a small test-only subclass to access the protected constructor and then verify
 * basic, easy-to-reason-about invariants via the public API.
 */
public class JsonWriteContextTest extends JUnit5TestBase {

    /**
     * Minimal, test-only subclass to access the protected constructor of JsonWriteContext.
     * Provides clear factory methods to avoid magic numbers and to make intent explicit.
     */
    static final class TestableWriteContext extends JsonWriteContext {
        private TestableWriteContext(int type, JsonWriteContext parent, DupDetector dups, Object currValue) {
            super(type, parent, dups, currValue);
        }

        static TestableWriteContext rootWith(Object currentValue) {
            return new TestableWriteContext(JsonStreamContext.TYPE_ROOT, null, null, currentValue);
        }

        static TestableWriteContext rootWith(DupDetector dups, Object currentValue) {
            return new TestableWriteContext(JsonStreamContext.TYPE_ROOT, null, dups, currentValue);
        }
    }

    @Test
    @DisplayName("Subclassing via protected constructor yields a valid root context")
    void subclassingProducesValidRootContext() {
        Object marker = new Object();

        TestableWriteContext ctx = TestableWriteContext.rootWith(marker);

        assertAll(
                () -> assertNotNull(ctx, "context should be created"),
                () -> assertTrue(ctx.inRoot(), "should represent the root context"),
                () -> assertNull(ctx.getParent(), "root context has no parent"),
                () -> assertSame(marker, ctx.getCurrentValue(), "current value should be preserved"),
                () -> assertFalse(ctx.hasCurrentName(), "root should not have a current field name"),
                () -> assertNull(ctx.getCurrentName(), "no current field name expected at root")
        );
    }

    @Test
    @DisplayName("DupDetector passed to constructor is exposed via getDupDetector()")
    void dupDetectorIsPreserved() {
        DupDetector dups = DupDetector.rootDetector((JsonGenerator) null);

        TestableWriteContext ctx = TestableWriteContext.rootWith(dups, "ignored");

        assertSame(dups, ctx.getDupDetector(), "DupDetector should be the same instance provided");
    }

    @Test
    @DisplayName("clearAndGetParent clears current value and returns parent (null for root)")
    void clearAndGetParentClearsCurrentValue() {
        Object marker = new Object();
        TestableWriteContext ctx = TestableWriteContext.rootWith(marker);

        JsonWriteContext parent = ctx.clearAndGetParent();

        assertAll(
                () -> assertNull(parent, "root has no parent to return"),
                () -> assertNull(ctx.getCurrentValue(), "current value should be cleared")
        );
    }
}