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
import org.junit.jupiter.api.Nested;

/**
 * Tests {@link Uncheck} utility class that converts checked IOExceptions to unchecked UncheckedIOExceptions.
 */
class UncheckTest {

    private static final byte[] TEST_BYTES = { 'a', 'b' };
    private static final String EXPECTED_CAUSE_MESSAGE = "CauseMessage";
    private static final String CUSTOM_ERROR_MESSAGE = "Custom message";

    // Test state holders for verifying side effects
    private AtomicInteger intHolder;
    private AtomicLong longHolder;
    private AtomicBoolean booleanHolder;
    private AtomicReference<String> stringHolder1;
    private AtomicReference<String> stringHolder2;
    private AtomicReference<String> stringHolder3;
    private AtomicReference<String> stringHolder4;

    @BeforeEach
    void setUp() {
        stringHolder1 = new AtomicReference<>();
        stringHolder2 = new AtomicReference<>();
        stringHolder3 = new AtomicReference<>();
        stringHolder4 = new AtomicReference<>();
        intHolder = new AtomicInteger();
        longHolder = new AtomicLong();
        booleanHolder = new AtomicBoolean();
    }

    private ByteArrayInputStream createTestInputStream() {
        return new ByteArrayInputStream(TEST_BYTES);
    }

    private void assertUncheckedIOExceptionWithCustomMessage(IOException originalException, UncheckedIOException actualException) {
        assertEquals(CUSTOM_ERROR_MESSAGE, actualException.getMessage());
        
        IOException cause = actualException.getCause();
        assertEquals(originalException.getClass(), cause.getClass());
        assertEquals(EXPECTED_CAUSE_MESSAGE, cause.getMessage());
    }

    private void assertIOExceptionIsWrappedAsUnchecked(Runnable operation) {
        assertThrows(UncheckedIOException.class, operation::run);
    }

    @Nested
    class AcceptTests {
        
        @Test
        void shouldAcceptSingleParameterConsumerSuccessfully() {
            ByteArrayInputStream stream = createTestInputStream();
            
            // Skip 1 byte using Uncheck.accept
            Uncheck.accept(n -> stream.skip(n), 1);
            
            // Verify the skip operation worked by reading next byte
            assertEquals('b', Uncheck.get(stream::read).intValue());
        }

        @Test
        void shouldWrapIOExceptionFromSingleParameterConsumer() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.accept(t -> { throw new IOException(); }, null));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.accept(TestUtils.throwingIOConsumer(), null));
        }

        @Test
        void shouldExecuteSingleParameterConsumerWithSideEffects() {
            String testValue = "testValue";
            
            Uncheck.accept(value -> TestUtils.compareAndSetThrowsIO(stringHolder1, value), testValue);
            
            assertEquals(testValue, stringHolder1.get());
        }

        @Test
        void shouldWrapIOExceptionFromBiConsumer() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.accept((t, u) -> { throw new IOException(); }, null, null));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.accept(TestConstants.THROWING_IO_BI_CONSUMER, null, null));
        }

        @Test
        void shouldExecuteBiConsumerWithSideEffects() {
            String value1 = "value1";
            String value2 = "value2";
            
            Uncheck.accept((t, u) -> {
                TestUtils.compareAndSetThrowsIO(stringHolder1, t);
                TestUtils.compareAndSetThrowsIO(stringHolder2, u);
            }, value1, value2);
            
            assertEquals(value1, stringHolder1.get());
            assertEquals(value2, stringHolder2.get());
        }

        @Test
        void shouldWrapIOExceptionFromTriConsumer() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.accept((t, u, v) -> { throw new IOException(); }, null, null, null));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.accept(TestConstants.THROWING_IO_TRI_CONSUMER, null, null, null));
        }

        @Test
        void shouldExecuteTriConsumerWithSideEffects() {
            String value1 = "value1";
            String value2 = "value2";
            String value3 = "value3";
            
            Uncheck.accept((t, u, v) -> {
                TestUtils.compareAndSetThrowsIO(stringHolder1, t);
                TestUtils.compareAndSetThrowsIO(stringHolder2, u);
                TestUtils.compareAndSetThrowsIO(stringHolder3, v);
            }, value1, value2, value3);
            
            assertEquals(value1, stringHolder1.get());
            assertEquals(value2, stringHolder2.get());
            assertEquals(value3, stringHolder3.get());
        }
    }

    @Nested
    class ApplyTests {

        @Test
        void shouldApplySingleParameterFunctionSuccessfully() {
            ByteArrayInputStream stream = createTestInputStream();
            
            long bytesSkipped = Uncheck.apply(n -> stream.skip(n), 1);
            
            assertEquals(1, bytesSkipped);
            assertEquals('b', Uncheck.get(stream::read).intValue());
        }

        @Test
        void shouldApplyBiFunctionSuccessfully() {
            ByteArrayInputStream stream = createTestInputStream();
            byte[] buffer = new byte[TEST_BYTES.length];
            
            int bytesRead = Uncheck.apply((offset, length) -> stream.read(buffer, offset, length), 0, 1);
            
            assertEquals(1, bytesRead);
            assertEquals('a', buffer[0]);
        }

        @Test
        void shouldApplyTriFunctionSuccessfully() {
            ByteArrayInputStream stream = createTestInputStream();
            byte[] buffer = new byte[TEST_BYTES.length];
            
            int bytesRead = Uncheck.apply((buf, offset, length) -> stream.read(buf, offset, length), buffer, 0, 1);
            
            assertEquals(1, bytesRead);
            assertEquals('a', buffer[0]);
        }

        @Test
        void shouldWrapIOExceptionFromSingleParameterFunction() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.apply(t -> { throw new IOException(); }, null));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.apply(TestConstants.THROWING_IO_FUNCTION, null));
        }

        @Test
        void shouldExecuteSingleParameterFunctionWithSideEffects() {
            String testValue = "testValue";
            
            Uncheck.apply(value -> TestUtils.compareAndSetThrowsIO(stringHolder1, value), testValue);
            
            assertEquals(testValue, stringHolder1.get());
        }

        @Test
        void shouldWrapIOExceptionFromBiFunction() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.apply((t, u) -> { throw new IOException(); }, null, null));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.apply(TestConstants.THROWING_IO_BI_FUNCTION, null, null));
        }

        @Test
        void shouldExecuteBiFunctionWithSideEffectsAndReturnValue() {
            String value1 = "value1";
            String value2 = "value2";
            String expectedResult = "result";
            
            String actualResult = Uncheck.apply((t, u) -> {
                TestUtils.compareAndSetThrowsIO(stringHolder1, t);
                TestUtils.compareAndSetThrowsIO(stringHolder2, u);
                return expectedResult;
            }, value1, value2);
            
            assertEquals(expectedResult, actualResult);
            assertEquals(value1, stringHolder1.get());
            assertEquals(value2, stringHolder2.get());
        }

        @Test
        void shouldWrapIOExceptionFromTriFunction() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.apply((t, u, v) -> { throw new IOException(); }, null, null, null));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.apply(TestConstants.THROWING_IO_TRI_FUNCTION, null, null, null));
        }

        @Test
        void shouldExecuteTriFunctionWithSideEffectsAndReturnValue() {
            String value1 = "value1";
            String value2 = "value2";
            String value3 = "value3";
            String expectedResult = "result";
            
            String actualResult = Uncheck.apply((t, u, v) -> {
                TestUtils.compareAndSetThrowsIO(stringHolder1, t);
                TestUtils.compareAndSetThrowsIO(stringHolder2, u);
                TestUtils.compareAndSetThrowsIO(stringHolder3, v);
                return expectedResult;
            }, value1, value2, value3);
            
            assertEquals(expectedResult, actualResult);
            assertEquals(value1, stringHolder1.get());
            assertEquals(value2, stringHolder2.get());
            assertEquals(value3, stringHolder3.get());
        }

        @Test
        void shouldWrapIOExceptionFromQuadFunction() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.apply((t, u, v, w) -> { throw new IOException(); }, null, null, null, null));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.apply(TestConstants.THROWING_IO_QUAD_FUNCTION, null, null, null, null));
        }

        @Test
        void shouldExecuteQuadFunctionWithSideEffectsAndReturnValue() {
            String value1 = "value1";
            String value2 = "value2";
            String value3 = "value3";
            String value4 = "value4";
            String expectedResult = "result";
            
            String actualResult = Uncheck.apply((t, u, v, w) -> {
                TestUtils.compareAndSetThrowsIO(stringHolder1, t);
                TestUtils.compareAndSetThrowsIO(stringHolder2, u);
                TestUtils.compareAndSetThrowsIO(stringHolder3, v);
                TestUtils.compareAndSetThrowsIO(stringHolder4, w);
                return expectedResult;
            }, value1, value2, value3, value4);
            
            assertEquals(expectedResult, actualResult);
            assertEquals(value1, stringHolder1.get());
            assertEquals(value2, stringHolder2.get());
            assertEquals(value3, stringHolder3.get());
            assertEquals(value4, stringHolder4.get());
        }
    }

    @Nested
    class GetTests {

        @Test
        void shouldGetValueFromSupplierSuccessfully() {
            int expectedValue = Uncheck.get(() -> createTestInputStream().read());
            
            assertEquals('a', expectedValue);
        }

        @Test
        void shouldWrapIOExceptionFromSupplier() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.get(() -> { throw new IOException(); }));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.get(TestConstants.THROWING_IO_SUPPLIER));
        }

        @Test
        void shouldExecuteSupplierWithSideEffects() {
            String testValue = "testValue";
            
            String result = Uncheck.get(() -> TestUtils.compareAndSetThrowsIO(stringHolder1, testValue));
            
            assertEquals(testValue, result);
            assertEquals(testValue, stringHolder1.get());
        }

        @Test
        void shouldGetValueFromSupplierWithCustomMessageWhenNoException() {
            int expectedValue = Uncheck.get(() -> createTestInputStream().read(), () -> CUSTOM_ERROR_MESSAGE);
            
            assertEquals('a', expectedValue);
        }

        @Test
        void shouldWrapIOExceptionFromSupplierWithCustomMessage() {
            IOException originalException = new IOException(EXPECTED_CAUSE_MESSAGE);
            
            try {
                Uncheck.get(() -> new BrokenInputStream(originalException).read(), () -> CUSTOM_ERROR_MESSAGE);
                fail("Expected UncheckedIOException to be thrown");
            } catch (UncheckedIOException e) {
                assertUncheckedIOExceptionWithCustomMessage(originalException, e);
            }
        }
    }

    @Nested
    class GetAsBooleanTests {

        @Test
        void shouldWrapIOExceptionFromBooleanSupplier() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsBoolean(() -> { throw new IOException(); }));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsBoolean(TestConstants.THROWING_IO_BOOLEAN_SUPPLIER));
        }

        @Test
        void shouldExecuteBooleanSupplierWithSideEffects() {
            boolean result = Uncheck.getAsBoolean(() -> TestUtils.compareAndSetThrowsIO(booleanHolder, true));
            
            assertTrue(result);
            assertTrue(booleanHolder.get());
        }
    }

    @Nested
    class GetAsIntTests {

        @Test
        void shouldWrapIOExceptionFromIntSupplier() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsInt(() -> { throw new IOException(); }));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsInt(TestConstants.THROWING_IO_INT_SUPPLIER));
        }

        @Test
        void shouldExecuteIntSupplierWithSideEffects() {
            int expectedValue = 42;
            
            int result = Uncheck.getAsInt(() -> TestUtils.compareAndSetThrowsIO(intHolder, expectedValue));
            
            assertEquals(expectedValue, result);
            assertEquals(expectedValue, intHolder.get());
        }

        @Test
        void shouldGetIntValueWithCustomMessageWhenNoException() {
            int expectedValue = 42;
            
            int result = Uncheck.getAsInt(() -> TestUtils.compareAndSetThrowsIO(intHolder, expectedValue), () -> CUSTOM_ERROR_MESSAGE);
            
            assertEquals(expectedValue, result);
            assertEquals(expectedValue, intHolder.get());
        }

        @Test
        void shouldWrapIOExceptionFromIntSupplierWithCustomMessage() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsInt(() -> { throw new IOException(); }, () -> CUSTOM_ERROR_MESSAGE));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsInt(TestConstants.THROWING_IO_INT_SUPPLIER, () -> CUSTOM_ERROR_MESSAGE));
        }

        @Test
        void shouldWrapIOExceptionWithCustomMessageFromBrokenInputStream() {
            IOException originalException = new IOException(EXPECTED_CAUSE_MESSAGE);
            
            try {
                Uncheck.getAsInt(() -> new BrokenInputStream(originalException).read(), () -> CUSTOM_ERROR_MESSAGE);
                fail("Expected UncheckedIOException to be thrown");
            } catch (UncheckedIOException e) {
                assertUncheckedIOExceptionWithCustomMessage(originalException, e);
            }
        }
    }

    @Nested
    class GetAsLongTests {

        @Test
        void shouldWrapIOExceptionFromLongSupplier() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsLong(() -> { throw new IOException(); }));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsLong(TestConstants.THROWING_IO_LONG_SUPPLIER));
        }

        @Test
        void shouldExecuteLongSupplierWithSideEffects() {
            long expectedValue = 42L;
            
            long result = Uncheck.getAsLong(() -> TestUtils.compareAndSetThrowsIO(longHolder, expectedValue));
            
            assertEquals(expectedValue, result);
            assertEquals(expectedValue, longHolder.get());
        }

        @Test
        void shouldGetLongValueWithCustomMessageWhenNoException() {
            long expectedValue = 42L;
            
            long result = Uncheck.getAsLong(() -> TestUtils.compareAndSetThrowsIO(longHolder, expectedValue), () -> CUSTOM_ERROR_MESSAGE);
            
            assertEquals(expectedValue, result);
            assertEquals(expectedValue, longHolder.get());
        }

        @Test
        void shouldWrapIOExceptionFromLongSupplierWithCustomMessage() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsLong(() -> { throw new IOException(); }, () -> CUSTOM_ERROR_MESSAGE));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.getAsLong(TestConstants.THROWING_IO_LONG_SUPPLIER, () -> CUSTOM_ERROR_MESSAGE));
        }

        @Test
        void shouldWrapIOExceptionWithCustomMessageFromBrokenInputStream() {
            IOException originalException = new IOException(EXPECTED_CAUSE_MESSAGE);
            
            try {
                Uncheck.getAsLong(() -> new BrokenInputStream(originalException).read(), () -> CUSTOM_ERROR_MESSAGE);
                fail("Expected UncheckedIOException to be thrown");
            } catch (UncheckedIOException e) {
                assertUncheckedIOExceptionWithCustomMessage(originalException, e);
            }
        }
    }

    @Nested
    class RunTests {

        @Test
        void shouldRunRunnableSuccessfully() {
            ByteArrayInputStream stream = createTestInputStream();
            
            Uncheck.run(() -> stream.skip(1));
            
            assertEquals('b', Uncheck.get(stream::read).intValue());
        }

        @Test
        void shouldWrapIOExceptionFromRunnable() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.run(() -> { throw new IOException(); }));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.run(TestConstants.THROWING_IO_RUNNABLE));
        }

        @Test
        void shouldExecuteRunnableWithSideEffects() {
            String testValue = "testValue";
            
            Uncheck.run(() -> TestUtils.compareAndSetThrowsIO(stringHolder1, testValue));
            
            assertEquals(testValue, stringHolder1.get());
        }

        @Test
        void shouldRunRunnableWithCustomMessageWhenNoException() {
            ByteArrayInputStream stream = createTestInputStream();
            
            Uncheck.run(() -> stream.skip(1), () -> CUSTOM_ERROR_MESSAGE);
            
            assertEquals('b', Uncheck.get(stream::read).intValue());
        }

        @Test
        void shouldWrapIOExceptionFromRunnableWithCustomMessage() {
            IOException originalException = new IOException(EXPECTED_CAUSE_MESSAGE);
            
            try {
                Uncheck.run(() -> new BrokenInputStream(originalException).read(), () -> CUSTOM_ERROR_MESSAGE);
                fail("Expected UncheckedIOException to be thrown");
            } catch (UncheckedIOException e) {
                assertUncheckedIOExceptionWithCustomMessage(originalException, e);
            }
        }
    }

    @Nested
    class TestTests {

        @Test
        void shouldWrapIOExceptionFromPredicate() {
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.test(t -> { throw new IOException(); }, null));
            
            assertIOExceptionIsWrappedAsUnchecked(() -> 
                Uncheck.test(TestConstants.THROWING_IO_PREDICATE, null));
        }

        @Test
        void shouldExecutePredicateWithSideEffectsAndReturnResult() {
            String testValue = "testValue";
            
            boolean result = Uncheck.test(value -> TestUtils.compareAndSetThrowsIO(stringHolder1, value).equals(value), testValue);
            
            assertTrue(result);
            assertEquals(testValue, stringHolder1.get());
        }
    }
}