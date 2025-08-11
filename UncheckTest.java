/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.input.BrokenInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Tests for {@link Uncheck}.
 *
 * The tests follow a simple Arrange-Act-Assert style and use small helpers
 * to reduce duplication and make intent clear.
 */
class UncheckTest {

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

    // Helpers

    private ByteArrayInputStream newInputStream() {
        return new ByteArrayInputStream(BYTES);
    }

    private BrokenInputStream brokenInputStream(final IOException cause) {
        return new BrokenInputStream(cause);
    }

    private void assertUncheckedIOExceptionWithCustomMessage(final IOException expectedCause, final UncheckedIOException actual) {
        assertEquals(CUSTOM_MESSAGE, actual.getMessage());
        final IOException cause = actual.getCause();
        assertEquals(expectedCause.getClass(), cause.getClass());
        assertEquals(CAUSE_MESSAGE, cause.getMessage());
    }

    private void assertWrapsWithCustomMessage(final IOException expectedCause, final Executable executable) {
        final UncheckedIOException uio = assertThrows(UncheckedIOException.class, executable);
        assertUncheckedIOExceptionWithCustomMessage(expectedCause, uio);
    }

    // accept(...)

    @Test
    void accept_withIOConsumer_executesAndAllowsFurtherIO() {
        // Arrange
        final ByteArrayInputStream stream = newInputStream();

        // Act
        Uncheck.accept(n -> stream.skip(n), 1);

        // Assert
        assertEquals('b', Uncheck.get(stream::read).intValue());
    }

    @Test
    void accept_withIOConsumer_wrapsIOException() {
        assertThrows(UncheckedIOException.class, () -> Uncheck.accept(t -> { throw new IOException(); }, null));
        assertThrows(UncheckedIOException.class, () -> Uncheck.accept(TestUtils.throwingIOConsumer(), null));
    }

    @Test
    void accept_withIOBiConsumer_executesAndWrapsIOException() {
        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.accept((t, u) -> { throw new IOException(); }, null, null));
        assertThrows(UncheckedIOException.class, () -> Uncheck.accept(TestConstants.THROWING_IO_BI_CONSUMER, null, null));

        // Executes
        Uncheck.accept((t, u) -> {
            TestUtils.compareAndSetThrowsIO(ref1, t);
            TestUtils.compareAndSetThrowsIO(ref2, u);
        }, "new1", "new2");

        assertEquals("new1", ref1.get());
        assertEquals("new2", ref2.get());
    }

    @Test
    void accept_withIOTriConsumer_executesAndWrapsIOException() {
        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.accept((t, u, v) -> { throw new IOException(); }, null, null, null));
        assertThrows(UncheckedIOException.class, () -> Uncheck.accept(TestConstants.THROWING_IO_TRI_CONSUMER, null, null, null));

        // Executes
        Uncheck.accept((t, u, v) -> {
            TestUtils.compareAndSetThrowsIO(ref1, t);
            TestUtils.compareAndSetThrowsIO(ref2, u);
            TestUtils.compareAndSetThrowsIO(ref3, v);
        }, "new1", "new2", "new3");

        assertEquals("new1", ref1.get());
        assertEquals("new2", ref2.get());
        assertEquals("new3", ref3.get());
    }

    // apply(...)

    @Test
    void apply_withIOFunction_returnsResultAndAllowsFurtherIO() {
        // Arrange
        final ByteArrayInputStream stream = newInputStream();

        // Act + Assert
        assertEquals(1, Uncheck.apply(n -> stream.skip(n), 1).intValue());
        assertEquals('b', Uncheck.get(stream::read).intValue());
    }

    @Test
    void apply_withIOBiFunction_readsIntoBuffer() {
        final ByteArrayInputStream stream = newInputStream();
        final byte[] buf = new byte[BYTES.length];

        assertEquals(1, Uncheck.apply((offset, len) -> stream.read(buf, offset, len), 0, 1).intValue());
        assertEquals('a', buf[0]);
    }

    @Test
    void apply_withIOTriFunction_readsIntoBuffer() {
        final ByteArrayInputStream stream = newInputStream();
        final byte[] buf = new byte[BYTES.length];

        assertEquals(1, Uncheck.apply((b, o, l) -> stream.read(b, o, l), buf, 0, 1).intValue());
        assertEquals('a', buf[0]);
    }

    @Test
    void apply_withIOFunction_wrapsIOException() {
        assertThrows(UncheckedIOException.class, () -> Uncheck.apply(t -> { throw new IOException(); }, null));
        assertThrows(UncheckedIOException.class, () -> Uncheck.apply(TestConstants.THROWING_IO_FUNCTION, null));

        Uncheck.apply(t -> TestUtils.compareAndSetThrowsIO(ref1, t), "new1");
        assertEquals("new1", ref1.get());
    }

    @Test
    void apply_withIOBiFunction_executesAndWrapsIOException() {
        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.apply((t, u) -> { throw new IOException(); }, null, null));
        assertThrows(UncheckedIOException.class, () -> Uncheck.apply(TestConstants.THROWING_IO_BI_FUNCTION, null, null));

        // Executes
        assertEquals("new0", Uncheck.apply((t, u) -> {
            TestUtils.compareAndSetThrowsIO(ref1, t);
            TestUtils.compareAndSetThrowsIO(ref2, u);
            return "new0";
        }, "new1", "new2"));

        assertEquals("new1", ref1.get());
        assertEquals("new2", ref2.get());
    }

    @Test
    void apply_withIOTriFunction_executesAndWrapsIOException() {
        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.apply((t, u, v) -> { throw new IOException(); }, null, null, null));
        assertThrows(UncheckedIOException.class, () -> Uncheck.apply(TestConstants.THROWING_IO_TRI_FUNCTION, null, null, null));

        // Executes
        assertEquals("new0", Uncheck.apply((t, u, v) -> {
            TestUtils.compareAndSetThrowsIO(ref1, t);
            TestUtils.compareAndSetThrowsIO(ref2, u);
            TestUtils.compareAndSetThrowsIO(ref3, v);
            return "new0";
        }, "new1", "new2", "new3"));

        assertEquals("new1", ref1.get());
        assertEquals("new2", ref2.get());
        assertEquals("new3", ref3.get());
    }

    @Test
    void apply_withIOQuadFunction_executesAndWrapsIOException() {
        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.apply((t, u, v, w) -> { throw new IOException(); }, null, null, null, null));
        assertThrows(UncheckedIOException.class, () -> Uncheck.apply(TestConstants.THROWING_IO_QUAD_FUNCTION, null, null, null, null));

        // Executes
        assertEquals("new0", Uncheck.apply((t, u, v, w) -> {
            TestUtils.compareAndSetThrowsIO(ref1, t);
            TestUtils.compareAndSetThrowsIO(ref2, u);
            TestUtils.compareAndSetThrowsIO(ref3, v);
            TestUtils.compareAndSetThrowsIO(ref4, w);
            return "new0";
        }, "new1", "new2", "new3", "new4"));

        assertEquals("new1", ref1.get());
        assertEquals("new2", ref2.get());
        assertEquals("new3", ref3.get());
        assertEquals("new4", ref4.get());
    }

    // get(...)

    @Test
    void get_returnsSupplierValue_andWrapsIOException() {
        // Returns value
        assertEquals('a', Uncheck.get(() -> newInputStream().read()).intValue());

        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.get(() -> { throw new IOException(); }));
        assertThrows(UncheckedIOException.class, () -> Uncheck.get(TestConstants.THROWING_IO_SUPPLIER));

        // Executes and returns
        assertEquals("new1", Uncheck.get(() -> TestUtils.compareAndSetThrowsIO(ref1, "new1")));
        assertEquals("new1", ref1.get());
    }

    @Test
    void get_withMessage_wrapsIOExceptionAndUsesCustomMessage() {
        // No exception path still returns value
        assertEquals('a', Uncheck.get(() -> newInputStream().read()).intValue());

        // Exception path validates custom message and cause
        final IOException expected = new IOException(CAUSE_MESSAGE);
        assertWrapsWithCustomMessage(expected, () -> Uncheck.get(() -> brokenInputStream(expected).read(), () -> CUSTOM_MESSAGE));
    }

    // getAsBoolean(...)

    @Test
    void getAsBoolean_returnsValue_andWrapsIOException() {
        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsBoolean(() -> { throw new IOException(); }));
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsBoolean(TestConstants.THROWING_IO_BOOLEAN_SUPPLIER));

        // Returns value
        assertTrue(Uncheck.getAsBoolean(() -> TestUtils.compareAndSetThrowsIO(atomicBoolean, true)));
        assertTrue(atomicBoolean.get());
    }

    // getAsInt(...)

    @Test
    void getAsInt_returnsValue_andWrapsIOException() {
        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsInt(() -> { throw new IOException(); }));
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsInt(TestConstants.THROWING_IO_INT_SUPPLIER));

        // Returns value
        assertEquals(1, Uncheck.getAsInt(() -> TestUtils.compareAndSetThrowsIO(atomicInt, 1)));
        assertEquals(1, atomicInt.get());
    }

    @Test
    void getAsInt_withMessage_wrapsIOExceptionAndUsesCustomMessage() {
        // No exception
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsInt(() -> { throw new IOException(); }, () -> CUSTOM_MESSAGE));
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsInt(TestConstants.THROWING_IO_INT_SUPPLIER, () -> CUSTOM_MESSAGE));
        assertEquals(1, Uncheck.getAsInt(() -> TestUtils.compareAndSetThrowsIO(atomicInt, 1), () -> CUSTOM_MESSAGE));
        assertEquals(1, atomicInt.get());

        // Exception with custom message
        final IOException expected = new IOException(CAUSE_MESSAGE);
        assertWrapsWithCustomMessage(expected, () -> Uncheck.getAsInt(() -> brokenInputStream(expected).read(), () -> CUSTOM_MESSAGE));
    }

    // getAsLong(...)

    @Test
    void getAsLong_returnsValue_andWrapsIOException() {
        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsLong(() -> { throw new IOException(); }));
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsLong(TestConstants.THROWING_IO_LONG_SUPPLIER));

        // Returns value
        assertEquals(1L, Uncheck.getAsLong(() -> TestUtils.compareAndSetThrowsIO(atomicLong, 1L)));
        assertEquals(1L, atomicLong.get());
    }

    @Test
    void getAsLong_withMessage_wrapsIOExceptionAndUsesCustomMessage() {
        // No exception
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsLong(() -> { throw new IOException(); }, () -> CUSTOM_MESSAGE));
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsLong(TestConstants.THROWING_IO_LONG_SUPPLIER, () -> CUSTOM_MESSAGE));
        assertEquals(1L, Uncheck.getAsLong(() -> TestUtils.compareAndSetThrowsIO(atomicLong, 1L), () -> CUSTOM_MESSAGE));
        assertEquals(1L, atomicLong.get());

        // Exception with custom message
        final IOException expected = new IOException(CAUSE_MESSAGE);
        assertWrapsWithCustomMessage(expected, () -> Uncheck.getAsLong(() -> brokenInputStream(expected).read(), () -> CUSTOM_MESSAGE));
    }

    // run(...)

    @Test
    void run_executesRunnable_andWrapsIOException() {
        // Executes
        final ByteArrayInputStream stream = newInputStream();
        Uncheck.run(() -> stream.skip(1));
        assertEquals('b', Uncheck.get(stream::read).intValue());

        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.run(() -> { throw new IOException(); }));
        assertThrows(UncheckedIOException.class, () -> Uncheck.run(TestConstants.THROWING_IO_RUNNABLE));

        // Executes and sets value
        Uncheck.run(() -> TestUtils.compareAndSetThrowsIO(ref1, "new1"));
        assertEquals("new1", ref1.get());
    }

    @Test
    void run_withMessage_wrapsIOExceptionAndUsesCustomMessage() {
        // Executes
        final ByteArrayInputStream stream = newInputStream();
        Uncheck.run(() -> stream.skip(1), () -> CUSTOM_MESSAGE);
        assertEquals('b', Uncheck.get(stream::read).intValue());

        // Exception with custom message
        final IOException expected = new IOException(CAUSE_MESSAGE);
        assertWrapsWithCustomMessage(expected, () -> Uncheck.run(() -> brokenInputStream(expected).read(), () -> CUSTOM_MESSAGE));
    }

    // test(...)

    @Test
    void test_withIOPredicate_returnsAndWrapsIOException() {
        // Wraps
        assertThrows(UncheckedIOException.class, () -> Uncheck.test(t -> { throw new IOException(); }, null));
        assertThrows(UncheckedIOException.class, () -> Uncheck.test(TestConstants.THROWING_IO_PREDICATE, null));

        // Returns
        assertTrue(Uncheck.test(t -> TestUtils.compareAndSetThrowsIO(ref1, t).equals(t), "new1"));
        assertEquals("new1", ref1.get());
    }
}