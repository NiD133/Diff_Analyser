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

import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    private static final String TEST_MESSAGE = "Test I/O Exception";
    private static final IOException TEST_EXCEPTION = new IOException(TEST_MESSAGE);

    // =================================================================
    // Tests for Uncheck.run(...)
    // =================================================================

    @Test
    public void testRun_whenNoException_completesSuccessfully() {
        // Arrange
        final IORunnable noOpRunnable = IORunnable.noop();

        // Act & Assert (should not throw)
        Uncheck.run(noOpRunnable);
        Uncheck.run(noOpRunnable, () -> "This message should not be used");
    }

    @Test(expected = UncheckedIOException.class)
    public void testRun_whenIOException_throwsUncheckedIOException() {
        // Arrange
        final IORunnable throwingRunnable = () -> {
            throw TEST_EXCEPTION;
        };

        // Act
        Uncheck.run(throwingRunnable);
    }

    @Test
    public void testRun_whenIOExceptionWithMessageSupplier_throwsUncheckedIOExceptionWithCustomMessage() {
        // Arrange
        final IORunnable throwingRunnable = () -> {
            throw TEST_EXCEPTION;
        };
        final String customMessage = "Custom error message for run";
        final Supplier<String> messageSupplier = () -> customMessage;

        // Act
        try {
            Uncheck.run(throwingRunnable, messageSupplier);
            fail("Expected UncheckedIOException was not thrown.");
        } catch (final UncheckedIOException e) {
            // Assert
            assertEquals(customMessage, e.getMessage());
            assertSame(TEST_EXCEPTION, e.getCause());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testRun_withNullRunnable_throwsNullPointerException() {
        Uncheck.run(null);
    }

    // =================================================================
    // Tests for Uncheck.get(...)
    // =================================================================

    @Test
    public void testGet_whenNoException_returnsValue() {
        // Arrange
        final String expected = "success";
        final IOSupplier<String> supplier = () -> expected;

        // Act
        final String result = Uncheck.get(supplier);

        // Assert
        assertEquals(expected, result);
    }

    @Test(expected = UncheckedIOException.class)
    public void testGet_whenIOException_throwsUncheckedIOException() {
        // Arrange
        final IOSupplier<String> throwingSupplier = () -> {
            throw TEST_EXCEPTION;
        };

        // Act
        Uncheck.get(throwingSupplier);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGet_withNullSupplier_throwsNullPointerException() {
        Uncheck.get(null);
    }

    // =================================================================
    // Tests for Uncheck.apply(...)
    // =================================================================

    @Test
    public void testApply_withFunction_whenNoException_returnsValue() {
        // Arrange
        final IOFunction<String, Integer> lengthFunction = String::length;

        // Act
        final Integer result = Uncheck.apply(lengthFunction, "test");

        // Assert
        assertEquals(Integer.valueOf(4), result);
    }

    @Test(expected = UncheckedIOException.class)
    public void testApply_withFunction_whenIOException_throwsUncheckedIOException() {
        // Arrange
        final IOFunction<String, Integer> throwingFunction = t -> {
            throw TEST_EXCEPTION;
        };

        // Act
        Uncheck.apply(throwingFunction, "test");
    }
    
    @Test(expected = NullPointerException.class)
    public void testApply_withNullFunction_throwsNullPointerException() {
        Uncheck.apply((IOFunction<String, String>) null, "test");
    }

    // Similar tests would apply to IOBiFunction, IOTriFunction, and IOQuadFunction.
    // For brevity, only the single-argument IOFunction is shown.

    // =================================================================
    // Tests for Uncheck.accept(...)
    // =================================================================

    @Test
    public void testAccept_withConsumer_whenNoException_completesSuccessfully() {
        // Arrange
        final IOConsumer<String> noOpConsumer = IOConsumer.noop();

        // Act & Assert (should not throw)
        Uncheck.accept(noOpConsumer, "test");
    }

    @Test(expected = UncheckedIOException.class)
    public void testAccept_withConsumer_whenIOException_throwsUncheckedIOException() {
        // Arrange
        final IOConsumer<String> throwingConsumer = t -> {
            throw TEST_EXCEPTION;
        };

        // Act
        Uncheck.accept(throwingConsumer, "test");
    }
    
    @Test(expected = NullPointerException.class)
    public void testAccept_withNullConsumer_throwsNullPointerException() {
        Uncheck.accept((IOConsumer<String>) null, "test");
    }

    // Similar tests would apply to IOBiConsumer, IOTriConsumer, and IOIntConsumer.
    // For brevity, only the single-argument IOConsumer is shown.

    // =================================================================
    // Tests for Uncheck.test(...)
    // =================================================================

    @Test
    public void testTest_whenNoException_returnsBoolean() {
        // Arrange
        final IOPredicate<String> isNotEmpty = s -> !s.isEmpty();

        // Act & Assert
        assertTrue(Uncheck.test(isNotEmpty, "test"));
        assertFalse(Uncheck.test(isNotEmpty, ""));
    }

    @Test(expected = UncheckedIOException.class)
    public void testTest_whenIOException_throwsUncheckedIOException() {
        // Arrange
        final IOPredicate<String> throwingPredicate = t -> {
            throw TEST_EXCEPTION;
        };

        // Act
        Uncheck.test(throwingPredicate, "test");
    }
    
    @Test(expected = NullPointerException.class)
    public void testTest_withNullPredicate_throwsNullPointerException() {
        Uncheck.test(null, "test");
    }

    // =================================================================
    // Tests for Uncheck.getAs...(...)
    // =================================================================

    @Test
    public void testGetAsInt_whenNoException_returnsValue() {
        // Arrange
        final IOIntSupplier supplier = () -> 42;

        // Act
        final int result = Uncheck.getAsInt(supplier);

        // Assert
        assertEquals(42, result);
    }

    @Test(expected = UncheckedIOException.class)
    public void testGetAsInt_whenIOException_throwsUncheckedIOException() {
        // Arrange
        final IOIntSupplier throwingSupplier = () -> {
            throw TEST_EXCEPTION;
        };

        // Act
        Uncheck.getAsInt(throwingSupplier);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetAsInt_withNullSupplier_throwsNullPointerException() {
        Uncheck.getAsInt(null);
    }

    // Similar tests would apply to getAsLong() and getAsBoolean().
}