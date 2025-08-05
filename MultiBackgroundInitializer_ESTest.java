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
package org.apache.commons.lang3.concurrent;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A test suite for {@link MultiBackgroundInitializer}.
 * The tests are structured to be clear, focused, and easy to maintain.
 */
public class MultiBackgroundInitializerTest {

    private MultiBackgroundInitializer multiInitializer;

    @Before
    public void setUp() {
        multiInitializer = new MultiBackgroundInitializer();
    }

    /**
     * A helper BackgroundInitializer that can be configured to succeed or fail.
     */
    private static class TestInitializer extends BackgroundInitializer<String> {
        private final boolean shouldThrow;
        private final String result;

        TestInitializer(String result) {
            this.result = result;
            this.shouldThrow = false;
        }

        TestInitializer(boolean shouldThrow) {
            this.result = null;
            this.shouldThrow = shouldThrow;
        }

        @Override
        protected String initialize() throws Exception {
            if (shouldThrow) {
                throw new IllegalStateException("Test exception");
            }
            return result;
        }
    }

    // --- Configuration and State Tests ---

    @Test
    public void testAddInitializerAfterStartThrowsException() {
        // Arrange: Start the initializer
        multiInitializer.start();

        // Act & Assert: Attempting to add another initializer should fail
        try {
            multiInitializer.addInitializer("child1", new TestInitializer("data"));
            fail("Expected IllegalStateException was not thrown.");
        } catch (final IllegalStateException e) {
            assertEquals("addInitializer() must not be called after start()!", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAddInitializerWithNullNameThrowsException() {
        // Act: Attempt to add an initializer with a null name
        multiInitializer.addInitializer(null, new TestInitializer("data"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddInitializerWithNullInitializerThrowsException() {
        // Act: Attempt to add a null initializer instance
        multiInitializer.addInitializer("child1", null);
    }

    @Test
    public void testTaskCountIsCorrect() {
        // Arrange: The main initializer has one task for managing children
        assertEquals("Initial task count should be 1", 1, multiInitializer.getTaskCount());

        // Act: Add two child initializers
        multiInitializer.addInitializer("child1", new TestInitializer("data1"));
        multiInitializer.addInitializer("child2", new TestInitializer("data2"));

        // Assert: Task count should be 1 (manager) + 2 (children) = 3
        assertEquals("Task count should include all children", 3, multiInitializer.getTaskCount());
    }

    // --- Execution Flow Tests ---

    @Test
    public void testInitializeWithNoChildrenIsSuccessful() throws ConcurrentException {
        // Act: Initialize with no children
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Assert: The operation should be successful and contain no results
        assertTrue("Initialization should be successful", results.isSuccessful());
        assertTrue("Initializer names should be empty", results.initializerNames().isEmpty());
    }

    @Test
    public void testInitializeWithSuccessfulChildrenIsSuccessful() throws ConcurrentException {
        // Arrange: Add multiple successful child initializers
        multiInitializer.addInitializer("child1", new TestInitializer("data1"));
        multiInitializer.addInitializer("child2", new TestInitializer("data2"));

        // Act: Initialize
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Assert: The overall result should be successful
        assertTrue("Initialization should be successful", results.isSuccessful());
        assertEquals("Result for child1 should be correct", "data1", results.getResultObject("child1"));
        assertEquals("Result for child2 should be correct", "data2", results.getResultObject("child2"));
    }

    @Test
    public void testInitializeWithFailingChildIsUnsuccessful() throws ConcurrentException {
        // Arrange: Add one successful and one failing initializer
        multiInitializer.addInitializer("successChild", new TestInitializer("data"));
        multiInitializer.addInitializer("failChild", new TestInitializer(true)); // This one will throw

        // Act: Initialize
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Assert: The overall result should be unsuccessful
        assertFalse("Initialization should be unsuccessful due to a failing child", results.isSuccessful());
        assertTrue("failChild should have an exception", results.isException("failChild"));
        assertNotNull("Exception for failChild should be available", results.getException("failChild"));
        assertFalse("successChild should not have an exception", results.isException("successChild"));
    }

    @Test
    public void testInitializeIsIdempotent() throws ConcurrentException {
        // Arrange
        multiInitializer.addInitializer("child1", new TestInitializer("data"));

        // Act: Call initialize() multiple times
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results1 = multiInitializer.initialize();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results2 = multiInitializer.initialize();

        // Assert: The same results object should be returned each time
        assertSame("Subsequent calls to initialize() should return the same result object", results1, results2);
        assertTrue(results1.isSuccessful());
    }

    @Test
    public void testConstructorWithExecutorServiceUsesProvidedExecutor() {
        // Arrange: Create a specific executor
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        
        // Act: Pass it to the constructor
        final MultiBackgroundInitializer initializerWithExecutor = new MultiBackgroundInitializer(executor);
        
        // Assert: The provided executor should be set
        assertSame("The external executor service should be used", executor, initializerWithExecutor.getExternalExecutor());
        
        // Cleanup
        executor.shutdown();
    }

    // --- Results Handling Tests ---

    @Test
    public void testResultsGettersForSuccessfulChild() throws ConcurrentException {
        // Arrange
        final BackgroundInitializer<String> child = new TestInitializer("data");
        multiInitializer.addInitializer("child", child);
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Assert
        assertFalse("isException should be false for successful child", results.isException("child"));
        assertNull("getException should be null for successful child", results.getException("child"));
        assertEquals("getResultObject should return the correct data", "data", results.getResultObject("child"));
        assertSame("getInitializer should return the original child instance", child, results.getInitializer("child"));
    }

    @Test
    public void testResultsGettersForFailingChild() throws ConcurrentException {
        // Arrange
        final BackgroundInitializer<String> child = new TestInitializer(true);
        multiInitializer.addInitializer("child", child);
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Assert
        assertTrue("isException should be true for failing child", results.isException("child"));
        assertNotNull("getException should not be null for failing child", results.getException("child"));
        assertTrue("The cause of the exception should be IllegalStateException",
                results.getException("child").getCause() instanceof IllegalStateException);
        assertNull("getResultObject should be null for failing child", results.getResultObject("child"));
    }

    @Test
    public void testResultsInitializerNamesReturnsAllKeys() throws ConcurrentException {
        // Arrange
        multiInitializer.addInitializer("child1", new TestInitializer("data1"));
        multiInitializer.addInitializer("child2", new TestInitializer("data2"));
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Act
        final Set<String> names = results.initializerNames();

        // Assert
        assertEquals("Should contain two names", 2, names.size());
        assertTrue("Should contain 'child1'", names.contains("child1"));
        assertTrue("Should contain 'child2'", names.contains("child2"));
    }

    // --- Exception Tests for Results Object ---

    @Test(expected = NoSuchElementException.class)
    public void testResultsGetInitializerForUnknownNameThrowsException() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();
        results.getInitializer("unknownName");
    }

    @Test(expected = NoSuchElementException.class)
    public void testResultsGetResultObjectForUnknownNameThrowsException() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();
        results.getResultObject("unknownName");
    }

    @Test(expected = NoSuchElementException.class)
    public void testResultsGetExceptionForUnknownNameThrowsException() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();
        results.getException("unknownName");
    }

    @Test(expected = NoSuchElementException.class)
    public void testResultsIsExceptionForUnknownNameThrowsException() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();
        results.isException("unknownName");
    }
}