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

    /**
     * Sets up a new DefaultExceptionContext before each test.
     */
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        exceptionContext = new DefaultExceptionContext();
        super.setUp();
    }

    /**
     * Tests that the formatted exception message includes the base message and context labels.
     */
    @Test
    void testFormattedExceptionMessageWithContextValues() {
        exceptionContext = new DefaultExceptionContext();
        final String contextLabel1 = "contextLabel1";
        final String contextLabel2 = "contextLabel2";

        // Add context values that throw exceptions when converted to string
        exceptionContext.addContextValue(contextLabel1, new ObjectToStringRuntimeException(contextLabel1));
        exceptionContext.addContextValue(contextLabel2, new ObjectToStringRuntimeException(contextLabel2));

        // Retrieve the formatted exception message
        final String formattedMessage = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);

        // Verify that the formatted message starts with the base message and contains both context labels
        assertTrue(formattedMessage.startsWith(TEST_MESSAGE), "Formatted message should start with the base message.");
        assertTrue(formattedMessage.contains(contextLabel1), "Formatted message should contain the first context label.");
        assertTrue(formattedMessage.contains(contextLabel2), "Formatted message should contain the second context label.");
    }

    /**
     * Tests that a null base message returns an empty formatted message.
     */
    @Test
    void testFormattedExceptionMessageWithNullBaseMessage() {
        exceptionContext = new DefaultExceptionContext();

        // Verify that a null base message returns an empty string
        assertEquals("", exceptionContext.getFormattedExceptionMessage(null), "Formatted message should be empty for null base message.");
    }

    /**
     * Tests that the formatted exception message includes context labels even if their values are null.
     */
    @Test
    void testFormattedExceptionMessageWithNullContextValues() {
        exceptionContext = new DefaultExceptionContext();
        final String contextLabel1 = "contextLabel1";
        final String contextLabel2 = "contextLabel2";

        // Add context values that are null
        exceptionContext.addContextValue(contextLabel1, null);
        exceptionContext.addContextValue(contextLabel2, null);

        // Retrieve the formatted exception message
        final String formattedMessage = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);

        // Verify that the formatted message starts with the base message and contains both context labels
        assertTrue(formattedMessage.startsWith(TEST_MESSAGE), "Formatted message should start with the base message.");
        assertTrue(formattedMessage.contains(contextLabel1), "Formatted message should contain the first context label.");
        assertTrue(formattedMessage.contains(contextLabel2), "Formatted message should contain the second context label.");
    }
}