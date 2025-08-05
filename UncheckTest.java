package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.*;

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

/**
 * Unit tests for {@link Uncheck}.
 */
class UncheckTest {

    private static final byte[] TEST_BYTES = { 'a', 'b' };
    private static final String IO_EXCEPTION_MESSAGE = "CauseMessage";
    private static final String CUSTOM_EXCEPTION_MESSAGE = "Custom message";

    private AtomicInteger atomicInt;
    private AtomicLong atomicLong;
    private AtomicBoolean atomicBoolean;
    private AtomicReference<String> ref1;
    private AtomicReference<String> ref2;
    private AtomicReference<String> ref3;
    private AtomicReference<String> ref4;

    @BeforeEach
    void setUp() {
        ref1 = new AtomicReference<>();
        ref2 = new AtomicReference<>();
        ref3 = new AtomicReference<>();
        ref4 = new AtomicReference<>();
        atomicInt = new AtomicInteger();
        atomicLong = new AtomicLong();
        atomicBoolean = new AtomicBoolean();
    }

    private ByteArrayInputStream createInputStream() {
        return new ByteArrayInputStream(TEST_BYTES);
    }

    private void verifyUncheckedIOException(IOException expected, UncheckedIOException actual) {
        assertEquals(CUSTOM_EXCEPTION_MESSAGE, actual.getMessage());
        assertEquals(expected.getClass(), actual.getCause().getClass());
        assertEquals(IO_EXCEPTION_MESSAGE, actual.getCause().getMessage());
    }

    @Test
    void testAcceptWithIOConsumer() {
        ByteArrayInputStream stream = createInputStream();
        Uncheck.accept(n -> stream.skip(n), 1);
        assertEquals('b', Uncheck.get(stream::read).intValue());
    }

    @Test
    void testAcceptWithIOBiConsumer() {
        assertThrows(UncheckedIOException.class, () -> Uncheck.accept((t, u) -> {
            throw new IOException();
        }, null, null));

        assertThrows(UncheckedIOException.class, () -> Uncheck.accept(TestConstants.THROWING_IO_BI_CONSUMER, null, null));

        Uncheck.accept((t, u) -> {
            TestUtils.compareAndSetThrowsIO(ref1, t);
            TestUtils.compareAndSetThrowsIO(ref2, u);
        }, "new1", "new2");

        assertEquals("new1", ref1.get());
        assertEquals("new2", ref2.get());
    }

    @Test
    void testAcceptWithIOTriConsumer() {
        assertThrows(UncheckedIOException.class, () -> Uncheck.accept((t, u, v) -> {
            throw new IOException();
        }, null, null, null));

        assertThrows(UncheckedIOException.class, () -> Uncheck.accept(TestConstants.THROWING_IO_TRI_CONSUMER, null, null, null));

        Uncheck.accept((t, u, v) -> {
            TestUtils.compareAndSetThrowsIO(ref1, t);
            TestUtils.compareAndSetThrowsIO(ref2, u);
            TestUtils.compareAndSetThrowsIO(ref3, v);
        }, "new1", "new2", "new3");

        assertEquals("new1", ref1.get());
        assertEquals("new2", ref2.get());
        assertEquals("new3", ref3.get());
    }

    @Test
    void testApplyWithIOFunction() {
        ByteArrayInputStream stream = createInputStream();
        assertEquals(1, Uncheck.apply(n -> stream.skip(n), 1).intValue());
        assertEquals('b', Uncheck.get(stream::read).intValue());
    }

    @Test
    void testApplyWithIOBiFunction() {
        ByteArrayInputStream stream = createInputStream();
        byte[] buffer = new byte[TEST_BYTES.length];
        assertEquals(1, Uncheck.apply((o, l) -> stream.read(buffer, o, l), 0, 1).intValue());
        assertEquals('a', buffer[0]);
    }

    @Test
    void testApplyWithIOTriFunction() {
        ByteArrayInputStream stream = createInputStream();
        byte[] buffer = new byte[TEST_BYTES.length];
        assertEquals(1, Uncheck.apply((b, o, l) -> stream.read(b, o, l), buffer, 0, 1).intValue());
        assertEquals('a', buffer[0]);
    }

    @Test
    void testGetWithIOSupplier() {
        assertEquals('a', Uncheck.get(() -> createInputStream().read()).intValue());

        assertThrows(UncheckedIOException.class, () -> Uncheck.get(() -> {
            throw new IOException();
        }));

        assertThrows(UncheckedIOException.class, () -> Uncheck.get(TestConstants.THROWING_IO_SUPPLIER));

        assertEquals("new1", Uncheck.get(() -> TestUtils.compareAndSetThrowsIO(ref1, "new1")));
        assertEquals("new1", ref1.get());
    }

    @Test
    void testGetAsBooleanWithIOSupplier() {
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsBoolean(() -> {
            throw new IOException();
        }));

        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsBoolean(TestConstants.THROWING_IO_BOOLEAN_SUPPLIER));

        assertTrue(Uncheck.getAsBoolean(() -> TestUtils.compareAndSetThrowsIO(atomicBoolean, true)));
        assertTrue(atomicBoolean.get());
    }

    @Test
    void testGetAsIntWithIOSupplier() {
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsInt(() -> {
            throw new IOException();
        }));

        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsInt(TestConstants.THROWING_IO_INT_SUPPLIER));

        assertEquals(1, Uncheck.getAsInt(() -> TestUtils.compareAndSetThrowsIO(atomicInt, 1)));
        assertEquals(1, atomicInt.get());
    }

    @Test
    void testGetAsLongWithIOSupplier() {
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsLong(() -> {
            throw new IOException();
        }));

        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsLong(TestConstants.THROWING_IO_LONG_SUPPLIER));

        assertEquals(1L, Uncheck.getAsLong(() -> TestUtils.compareAndSetThrowsIO(atomicLong, 1L)));
        assertEquals(1L, atomicLong.get());
    }

    @Test
    void testRunWithIORunnable() {
        ByteArrayInputStream stream = createInputStream();
        Uncheck.run(() -> stream.skip(1));
        assertEquals('b', Uncheck.get(stream::read).intValue());

        assertThrows(UncheckedIOException.class, () -> Uncheck.run(() -> {
            throw new IOException();
        }));

        assertThrows(UncheckedIOException.class, () -> Uncheck.run(TestConstants.THROWING_IO_RUNNABLE));

        Uncheck.run(() -> TestUtils.compareAndSetThrowsIO(ref1, "new1"));
        assertEquals("new1", ref1.get());
    }

    @Test
    void testTestWithIOPredicate() {
        assertThrows(UncheckedIOException.class, () -> Uncheck.test(t -> {
            throw new IOException();
        }, null));

        assertThrows(UncheckedIOException.class, () -> Uncheck.test(TestConstants.THROWING_IO_PREDICATE, null));

        assertTrue(Uncheck.test(t -> TestUtils.compareAndSetThrowsIO(ref1, t).equals(t), "new1"));
        assertEquals("new1", ref1.get());
    }
}