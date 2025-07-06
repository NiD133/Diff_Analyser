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

package org.apache.commons.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link DecoderException}.
 */
class DecoderExceptionTest {

    private static final String TEST_MESSAGE = "TEST";
    private static final Throwable THROWABLE_CAUSE = new Exception();

    /**
     * Test that the DecoderException constructor without parameters sets the message and cause correctly.
     */
    @Test
    void testDefaultConstructor() {
        // Arrange and Act
        final DecoderException exception = new DecoderException();

        // Assert
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Test that the DecoderException constructor with a message sets the message and cause correctly.
     */
    @Test
    void testMessageConstructor() {
        // Arrange
        final String expectedMessage = TEST_MESSAGE;

        // Act
        final DecoderException exception = new DecoderException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Test that the DecoderException constructor with a message and a cause sets the message and cause correctly.
     */
    @Test
    void testMessageAndCauseConstructor() {
        // Arrange
        final String expectedMessage = TEST_MESSAGE;
        final Throwable expectedCause = THROWABLE_CAUSE;

        // Act
        final DecoderException exception = new DecoderException(expectedMessage, expectedCause);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }

    /**
     * Test that the DecoderException constructor with a cause sets the message and cause correctly.
     */
    @Test
    void testCauseConstructor() {
        // Arrange
        final Throwable expectedCause = THROWABLE_CAUSE;

        // Act
        final DecoderException exception = new DecoderException(expectedCause);

        // Assert
        assertEquals(expectedCause.getClass().getName(), exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }
}