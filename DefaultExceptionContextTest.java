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
package org.apache.commons.lang3.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.ObjectToStringRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for DefaultExceptionContext.
 */
class DefaultExceptionContextTest extends AbstractExceptionContextTest<DefaultExceptionContext> {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        exceptionContext = new DefaultExceptionContext();
        super.setUp();
    }

    @Test
    void testGetFormattedExceptionMessage_WithExceptionContextValues() {
        // Given
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String label1 = "throws 1";
        final String label2 = "throws 2";
        final Object value1 = new ObjectToStringRuntimeException(label1);
        final Object value2 = new ObjectToStringRuntimeException(label2);

        // When
        context.addContextValue(label1, value1);
        context.addContextValue(label2, value2);
        final String message = context.getFormattedExceptionMessage(TEST_MESSAGE);

        // Then
        assertTrue(message.startsWith(TEST_MESSAGE), 
            "Message should start with base message");
        assertTrue(message.contains(label1), 
            () -> "Message should contain label: " + label1);
        assertTrue(message.contains(label2), 
            () -> "Message should contain label: " + label2);
    }

    @Test
    void testGetFormattedExceptionMessage_WithNullBaseMessage() {
        // Given
        final DefaultExceptionContext context = new DefaultExceptionContext();

        // When
        final String message = context.getFormattedExceptionMessage(null);

        // Then
        assertEquals("", message, 
            "Formatted message should be empty for null base message");
    }

    @Test
    void testGetFormattedExceptionMessage_WithNullContextValues() {
        // Given
        final DefaultExceptionContext context = new DefaultExceptionContext();
        final String label1 = "throws 1";
        final String label2 = "throws 2";

        // When
        context.addContextValue(label1, null);
        context.addContextValue(label2, null);
        final String message = context.getFormattedExceptionMessage(TEST_MESSAGE);

        // Then
        assertTrue(message.startsWith(TEST_MESSAGE), 
            "Message should start with base message");
        assertTrue(message.contains(label1), 
            () -> "Message should contain label: " + label1);
        assertTrue(message.contains(label2), 
            () -> "Message should contain label: " + label2);
    }

}