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
 * Tests for the {@link DecoderException} class, ensuring its constructors behave as expected.
 */
class DecoderExceptionTest {

    private static final String TEST_MESSAGE = "Test Decoder Exception Message";
    private static final Throwable TEST_CAUSE = new Exception("Test Cause Exception");

    @Test
    void testDefaultConstructor() {
        // Arrange: Create a DecoderException using the default constructor.
        final DecoderException exception = new DecoderException();

        // Act & Assert: Verify that the message and cause are null.
        assertNull(exception.getMessage(), "Default constructor should create an exception with a null message.");
        assertNull(exception.getCause(), "Default constructor should create an exception with a null cause.");
    }

    @Test
    void testConstructorWithMessage() {
        // Arrange: Create a DecoderException with a message.
        final DecoderException exception = new DecoderException(TEST_MESSAGE);

        // Act & Assert: Verify that the message is set correctly and the cause is null.
        assertEquals(TEST_MESSAGE, exception.getMessage(), "Constructor with message should set the message correctly.");
        assertNull(exception.getCause(), "Constructor with message should create an exception with a null cause.");
    }

    @Test
    void testConstructorWithMessageAndCause() {
        // Arrange: Create a DecoderException with a message and a cause.
        final DecoderException exception = new DecoderException(TEST_MESSAGE, TEST_CAUSE);

        // Act & Assert: Verify that the message and cause are set correctly.
        assertEquals(TEST_MESSAGE, exception.getMessage(), "Constructor with message and cause should set the message correctly.");
        assertEquals(TEST_CAUSE, exception.getCause(), "Constructor with message and cause should set the cause correctly.");
    }

    @Test
    void testConstructorWithCause() {
        // Arrange: Create a DecoderException with a cause.
        final DecoderException exception = new DecoderException(TEST_CAUSE);

        // Act & Assert: Verify that the message is the cause's class name and the cause is set correctly.
        assertEquals(TEST_CAUSE.getClass().getName(), exception.getMessage(), "Constructor with cause should set the message to the cause's class name.");
        assertEquals(TEST_CAUSE, exception.getCause(), "Constructor with cause should set the cause correctly.");
    }

}