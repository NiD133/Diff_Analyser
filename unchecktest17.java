package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import org.apache.commons.io.input.BrokenInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UncheckTestTest17 {

    private static final byte[] BYTES = { 'a', 'b' };

    private static final String CAUSE_MESSAGE = "CauseMessage";

    private static final String CUSTOM_MESSAGE = "Custom message";

    private AtomicInteger atomicInt;

    private AtomicLong atomicLong;

    private AtomicBoolean atomicBoolean;

    private AtomicReference<String> ref1;

    private AtomicReference<String> ref2;

    private AtomicReference<String> ref3;

    private AtomicReference<String> ref4;

    private void assertUncheckedIOException(final IOException expected, final UncheckedIOException e) {
        assertEquals(CUSTOM_MESSAGE, e.getMessage());
        final IOException cause = e.getCause();
        assertEquals(expected.getClass(), cause.getClass());
        assertEquals(CAUSE_MESSAGE, cause.getMessage());
    }

    @BeforeEach
    public void beforeEach() {
        ref1 = new AtomicReference<>();
        ref2 = new AtomicReference<>();
        ref3 = new AtomicReference<>();
        ref4 = new AtomicReference<>();
        atomicInt = new AtomicInteger();
        atomicLong = new AtomicLong();
        atomicBoolean = new AtomicBoolean();
    }

    private ByteArrayInputStream newInputStream() {
        return new ByteArrayInputStream(BYTES);
    }

    @Test
    void testGetAsLongMessage() {
        // No exception
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsLong(() -> {
            throw new IOException();
        }, () -> CUSTOM_MESSAGE));
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsLong(TestConstants.THROWING_IO_LONG_SUPPLIER, () -> CUSTOM_MESSAGE));
        assertEquals(1L, Uncheck.getAsLong(() -> TestUtils.compareAndSetThrowsIO(atomicLong, 1L), () -> CUSTOM_MESSAGE));
        assertEquals(1L, atomicLong.get());
        // exception
        final IOException expected = new IOException(CAUSE_MESSAGE);
        try {
            Uncheck.getAsLong(() -> new BrokenInputStream(expected).read(), () -> CUSTOM_MESSAGE);
            fail();
        } catch (final UncheckedIOException e) {
            assertUncheckedIOException(expected, e);
        }
    }
}
