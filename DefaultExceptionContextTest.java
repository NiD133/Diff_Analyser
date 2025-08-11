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
        // This method is called before each test, ensuring a fresh context.
        exceptionContext = new DefaultExceptionContext();
        super.setUp();
    }

    /**
     * Tests that the formatted message is generated correctly even when a context
     * value's toString() method throws an exception. The context label should
     * still be included.
     */
    @Test
    void getFormattedExceptionMessage_shouldSafelyFormatValue_whenValueToStringThrowsException() {
        // Arrange
        final String label1 = "Erroring Label 1";
        final String label2 = "Erroring Label 2";
        exceptionContext.addContextValue(label1, new ObjectToStringRuntimeException(label1));
        exceptionContext.addContextValue(label2, new ObjectToStringRuntimeException(label2));

        // Act
        final String message = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);

        // Assert
        assertTrue(message.startsWith(TEST_MESSAGE), "Message should start with the base message");
        assertTrue(message.contains(label1), "Message should contain the first label");
        assertTrue(message.contains(label2), "Message should contain the second label");
    }

    /**
     * Tests that an empty string is returned when the base message is null,
     * regardless of the context content.
     */
    @Test
    void getFormattedExceptionMessage_shouldReturnEmptyString_whenBaseMessageIsNull() {
        // Arrange
        exceptionContext.addContextValue("Some Label", "Some Value");

        // Act
        final String message = exceptionContext.getFormattedExceptionMessage(null);

        // Assert
        assertEquals("", message);
    }

    /**
     * Tests that labels for null context values are still included in the
     * formatted message.
     */
    @Test
    void getFormattedExceptionMessage_shouldIncludeLabels_whenContextValuesAreNull() {
        // Arrange
        final String label1 = "Label with null value 1";
        final String label2 = "Label with null value 2";
        exceptionContext.addContextValue(label1, null);
        exceptionContext.addContextValue(label2, null);

        // Act
        final String message = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);

        // Assert
        assertTrue(message.startsWith(TEST_MESSAGE), "Message should start with the base message");
        assertTrue(message.contains(label1), "Message should contain the first label");
        assertTrue(message.contains(label2), "Message should contain the second label");
    }

}