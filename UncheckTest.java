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
import java.util.function.Supplier;

import org.apache.commons.io.input.BrokenInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Uncheck}.
 */
class UncheckTest {

    private static final byte[] BYTES = {'a', 'b'};
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
    void beforeEach() {
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

    @Nested
    @DisplayName("For Uncheck.accept(...) methods")
    class AcceptTests {

        @Test
        void ioConsumer_whenSuccessful_consumesArgument() {
            Uncheck.accept(t -> TestUtils.compareAndSetThrowsIO(ref1, t), "new1");
            assertEquals("new1", ref1.get());
        }

        @Test
        void ioConsumer_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.accept(t -> {
                throw new IOException();
            }, null));
        }

        @Test
        void ioBiConsumer_whenSuccessful_consumesArguments() {
            Uncheck.accept((t, u) -> {
                TestUtils.compareAndSetThrowsIO(ref1, t);
                TestUtils.compareAndSetThrowsIO(ref2, u);
            }, "new1", "new2");
            assertEquals("new1", ref1.get());
            assertEquals("new2", ref2.get());
        }



        @Test
        void ioBiConsumer_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.accept((t, u) -> {
                throw new IOException();
            }, null, null));
        }

        @Test
        void ioTriConsumer_whenSuccessful_consumesArguments() {
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
        void ioTriConsumer_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.accept((t, u, v) -> {
                throw new IOException();
            }, null, null, null));
        }

        @Test
        void concreteUseCase_acceptsConsumerAndSkipsBytesInStream() {
            final ByteArrayInputStream stream = newInputStream();
            Uncheck.accept(stream::skip, 1);
            assertEquals('b', Uncheck.get(stream::read));
        }
    }

    @Nested
    @DisplayName("For Uncheck.apply(...) methods")
    class ApplyTests {

        @Test
        void ioFunction_whenSuccessful_returnsValue() {
            final String result = Uncheck.apply(t -> TestUtils.compareAndSetThrowsIO(ref1, t), "new1");
            assertEquals("new1", result);
            assertEquals("new1", ref1.get());
        }

        @Test
        void ioFunction_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.apply(t -> {
                throw new IOException();
            }, null));
        }

        @Test
        void ioBiFunction_whenSuccessful_returnsValue() {
            final String result = Uncheck.apply((t, u) -> {
                TestUtils.compareAndSetThrowsIO(ref1, t);
                TestUtils.compareAndSetThrowsIO(ref2, u);
                return "new0";
            }, "new1", "new2");

            assertEquals("new0", result);
            assertEquals("new1", ref1.get());
            assertEquals("new2", ref2.get());
        }

        @Test
        void ioBiFunction_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.apply((t, u) -> {
                throw new IOException();
            }, null, null));
        }

        @Test
        void ioTriFunction_whenSuccessful_returnsValue() {
            final String result = Uncheck.apply((t, u, v) -> {
                TestUtils.compareAndSetThrowsIO(ref1, t);
                TestUtils.compareAndSetThrowsIO(ref2, u);
                TestUtils.compareAndSetThrowsIO(ref3, v);
                return "new0";
            }, "new1", "new2", "new3");

            assertEquals("new0", result);
            assertEquals("new1", ref1.get());
            assertEquals("new2", ref2.get());
            assertEquals("new3", ref3.get());
        }

        @Test
        void ioTriFunction_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.apply((t, u, v) -> {
                throw new IOException();
            }, null, null, null));
        }

        @Test
        void ioQuadFunction_whenSuccessful_returnsValue() {
            final String result = Uncheck.apply((t, u, v, w) -> {
                TestUtils.compareAndSetThrowsIO(ref1, t);
                TestUtils.compareAndSetThrowsIO(ref2, u);
                TestUtils.compareAndSetThrowsIO(ref3, v);
                TestUtils.compareAndSetThrowsIO(ref4, w);
                return "new0";
            }, "new1", "new2", "new3", "new4");

            assertEquals("new0", result);
            assertEquals("new1", ref1.get());
            assertEquals("new2", ref2.get());
            assertEquals("new3", ref3.get());
            assertEquals("new4", ref4.get());
        }

        @Test
        void ioQuadFunction_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.apply((t, u, v, w) -> {
                throw new IOException();
            }, null, null, null, null));
        }

        @Test
        void concreteUseCase_appliesFunctionAndSkipsBytesInStream() {
            final ByteArrayInputStream stream = newInputStream();
            assertEquals(1, Uncheck.apply(stream::skip, 1L));
            assertEquals('b', Uncheck.get(stream::read));
        }

        @Test
        void concreteUseCase_appliesBiFunctionAndReadsFromStream() {
            final ByteArrayInputStream stream = newInputStream();
            final byte[] buf = new byte[BYTES.length];
            assertEquals(1, Uncheck.apply(stream::read, buf, 0));
            assertEquals('a', buf[0]);
        }

        @Test
        void concreteUseCase_appliesTriFunctionAndReadsFromStream() {
            final ByteArrayInputStream stream = newInputStream();
            final byte[] buf = new byte[BYTES.length];
            assertEquals(1, Uncheck.apply(stream::read, buf, 0, 1));
            assertEquals('a', buf[0]);
        }
    }

    @Nested
    @DisplayName("For Uncheck.get(...) methods")
    class GetTests {

        @Test
        void get_whenSuccessful_returnsValue() {
            assertEquals("new1", Uncheck.get(() -> TestUtils.compareAndSetThrowsIO(ref1, "new1")));
            assertEquals("new1", ref1.get());
        }

        @Test
        void get_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.get(() -> {
                throw new IOException();
            }));
        }

        @Test
        void getWithMessage_whenSuccessful_returnsValue() {
            assertEquals('a', (int) Uncheck.get(() -> newInputStream().read(), () -> CUSTOM_MESSAGE));
        }

        @Test
        void getWithMessage_whenThrowsIOException_throwsUncheckedIOExceptionWithCustomMessage() {
            final IOException cause = new IOException(CAUSE_MESSAGE);
            final Supplier<String> messageSupplier = () -> CUSTOM_MESSAGE;

            final UncheckedIOException thrown = assertThrows(UncheckedIOException.class,
                () -> Uncheck.get(() -> new BrokenInputStream(cause).read(), messageSupplier));

            assertEquals(CUSTOM_MESSAGE, thrown.getMessage());
            assertEquals(cause, thrown.getCause());
        }

        @Test
        void getAsBoolean_whenSuccessful_returnsValue() {
            assertTrue(Uncheck.getAsBoolean(() -> TestUtils.compareAndSetThrowsIO(atomicBoolean, true)));
            assertTrue(atomicBoolean.get());
        }

        @Test
        void getAsBoolean_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.getAsBoolean(() -> {
                throw new IOException();
            }));
        }

        @Test
        void getAsInt_whenSuccessful_returnsValue() {
            assertEquals(1, Uncheck.getAsInt(() -> TestUtils.compareAndSetThrowsIO(atomicInt, 1)));
            assertEquals(1, atomicInt.get());
        }

        @Test
        void getAsInt_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.getAsInt(() -> {
                throw new IOException();
            }));
        }

        @Test
        void getAsIntWithMessage_whenSuccessful_returnsValue() {
            assertEquals(1, Uncheck.getAsInt(() -> TestUtils.compareAndSetThrowsIO(atomicInt, 1), () -> CUSTOM_MESSAGE));
            assertEquals(1, atomicInt.get());
        }

        @Test
        void getAsIntWithMessage_whenThrowsIOException_throwsUncheckedIOExceptionWithCustomMessage() {
            final IOException cause = new IOException(CAUSE_MESSAGE);
            final UncheckedIOException thrown = assertThrows(UncheckedIOException.class,
                () -> Uncheck.getAsInt(() -> new BrokenInputStream(cause).read(), () -> CUSTOM_MESSAGE));

            assertEquals(CUSTOM_MESSAGE, thrown.getMessage());
            assertEquals(cause, thrown.getCause());
        }

        @Test
        void getAsLong_whenSuccessful_returnsValue() {
            assertEquals(1L, Uncheck.getAsLong(() -> TestUtils.compareAndSetThrowsIO(atomicLong, 1L)));
            assertEquals(1L, atomicLong.get());
        }

        @Test
        void getAsLong_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.getAsLong(() -> {
                throw new IOException();
            }));
        }

        @Test
        void getAsLongWithMessage_whenSuccessful_returnsValue() {
            assertEquals(1L, Uncheck.getAsLong(() -> TestUtils.compareAndSetThrowsIO(atomicLong, 1L), () -> CUSTOM_MESSAGE));
            assertEquals(1L, atomicLong.get());
        }

        @Test
        void getAsLongWithMessage_whenThrowsIOException_throwsUncheckedIOExceptionWithCustomMessage() {
            final IOException cause = new IOException(CAUSE_MESSAGE);
            final UncheckedIOException thrown = assertThrows(UncheckedIOException.class,
                () -> Uncheck.getAsLong(() -> new BrokenInputStream(cause).read(), () -> CUSTOM_MESSAGE));

            assertEquals(CUSTOM_MESSAGE, thrown.getMessage());
            assertEquals(cause, thrown.getCause());
        }
    }

    @Nested
    @DisplayName("For Uncheck.run(...) methods")
    class RunTests {

        @Test
        void run_whenSuccessful_executesRunnable() {
            Uncheck.run(() -> TestUtils.compareAndSetThrowsIO(ref1, "new1"));
            assertEquals("new1", ref1.get());
        }

        @Test
        void run_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.run(() -> {
                throw new IOException();
            }));
        }

        @Test
        void runWithMessage_whenSuccessful_executesRunnable() {
            final ByteArrayInputStream stream = newInputStream();
            Uncheck.run(() -> stream.skip(1), () -> CUSTOM_MESSAGE);
            assertEquals('b', Uncheck.get(stream::read));
        }

        @Test
        void runWithMessage_whenThrowsIOException_throwsUncheckedIOExceptionWithCustomMessage() {
            final IOException cause = new IOException(CAUSE_MESSAGE);
            final UncheckedIOException thrown = assertThrows(UncheckedIOException.class,
                () -> Uncheck.run(() -> new BrokenInputStream(cause).read(), () -> CUSTOM_MESSAGE));

            assertEquals(CUSTOM_MESSAGE, thrown.getMessage());
            assertEquals(cause, thrown.getCause());
        }
    }

    @Nested
    @DisplayName("For Uncheck.test(...) methods")
    class TestTests {

        @Test
        void test_whenSuccessful_returnsBoolean() {
            assertTrue(Uncheck.test(t -> TestUtils.compareAndSetThrowsIO(ref1, t).equals(t), "new1"));
            assertEquals("new1", ref1.get());
        }

        @Test
        void test_whenThrowsIOException_throwsUncheckedIOException() {
            assertThrows(UncheckedIOException.class, () -> Uncheck.test(t -> {
                throw new IOException();
            }, null));
        }
    }
}