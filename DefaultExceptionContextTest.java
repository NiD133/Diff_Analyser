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
 * 
 * Tests the formatting of exception messages with contextual information,
 * including edge cases like null values and objects that throw exceptions
 * during toString() conversion.
 */
class DefaultExceptionContextTest extends AbstractExceptionContextTest<DefaultExceptionContext> {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        exceptionContext = new DefaultExceptionContext();
        super.setUp();
    }

    /**
     * Tests that formatted exception messages handle objects that throw exceptions
     * during toString() conversion gracefully.
     * 
     * The formatted message should still include the base message and context labels,
     * even when context values cannot be converted to strings.
     */
    @Test
    void testFormattedExceptionMessage_HandlesObjectToStringExceptions() {
        // Given: A fresh exception context
        exceptionContext = new DefaultExceptionContext();
        
        // And: Context values that throw exceptions when toString() is called
        final String problematicLabel1 = "throws 1";
        final String problematicLabel2 = "throws 2";
        final ObjectToStringRuntimeException problematicValue1 = new ObjectToStringRuntimeException(problematicLabel1);
        final ObjectToStringRuntimeException problematicValue2 = new ObjectToStringRuntimeException(problematicLabel2);
        
        exceptionContext.addContextValue(problematicLabel1, problematicValue1);
        exceptionContext.addContextValue(problematicLabel2, problematicValue2);
        
        // When: Getting the formatted exception message
        final String formattedMessage = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);
        
        // Then: The message should contain the base message and both labels
        assertTrue(formattedMessage.startsWith(TEST_MESSAGE), 
                  "Formatted message should start with the base test message");
        assertTrue(formattedMessage.contains(problematicLabel1), 
                  "Formatted message should contain the first problematic label");
        assertTrue(formattedMessage.contains(problematicLabel2), 
                  "Formatted message should contain the second problematic label");
    }

    /**
     * Tests that getFormattedExceptionMessage returns an empty string when given null input.
     * 
     * This ensures the method handles null base messages gracefully without throwing exceptions.
     */
    @Test
    void testFormattedExceptionMessage_WithNullBaseMessage_ReturnsEmptyString() {
        // Given: A fresh exception context
        exceptionContext = new DefaultExceptionContext();
        
        // When: Getting formatted message with null base message
        final String result = exceptionContext.getFormattedExceptionMessage(null);
        
        // Then: Should return empty string
        assertEquals("", result, "Formatted message should be empty string when base message is null");
    }

    /**
     * Tests that formatted exception messages handle null context values properly.
     * 
     * The formatted message should include the base message and context labels,
     * even when the context values are null.
     */
    @Test
    void testFormattedExceptionMessage_WithNullContextValues_IncludesLabels() {
        // Given: A fresh exception context
        exceptionContext = new DefaultExceptionContext();
        
        // And: Context entries with null values
        final String nullValueLabel1 = "throws 1";
        final String nullValueLabel2 = "throws 2";
        
        exceptionContext.addContextValue(nullValueLabel1, null);
        exceptionContext.addContextValue(nullValueLabel2, null);
        
        // When: Getting the formatted exception message
        final String formattedMessage = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);
        
        // Then: The message should contain the base message and both labels
        assertTrue(formattedMessage.startsWith(TEST_MESSAGE), 
                  "Formatted message should start with the base test message");
        assertTrue(formattedMessage.contains(nullValueLabel1), 
                  "Formatted message should contain the first null value label");
        assertTrue(formattedMessage.contains(nullValueLabel2), 
                  "Formatted message should contain the second null value label");
    }
}