package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.apache.commons.lang3.concurrent.BackgroundInitializer;
import org.apache.commons.lang3.concurrent.MultiBackgroundInitializer;
import org.apache.commons.lang3.concurrent.MultiBackgroundInitializer.MultiBackgroundInitializerResults;

/**
 * Test suite for MultiBackgroundInitializer functionality.
 * Tests the ability to manage multiple background initialization tasks.
 */
public class MultiBackgroundInitializerTest {

    private MultiBackgroundInitializer multiInitializer;
    private static final String INITIALIZER_NAME = "testInitializer";
    private static final String EMPTY_NAME = "";
    private static final String NON_EXISTENT_NAME = "nonExistent";

    @Before
    public void setUp() {
        multiInitializer = new MultiBackgroundInitializer();
    }

    // ========== Basic Functionality Tests ==========

    @Test
    public void testInitializeWithoutChildInitializers_ShouldReturnSuccessfulResults() throws Throwable {
        // Given: A MultiBackgroundInitializer with no child initializers
        assertFalse("Should not be started initially", multiInitializer.isStarted());

        // When: Initialize is called
        MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Then: Results should be successful
        assertTrue("Results should be successful when no child initializers", results.isSuccessful());
        assertTrue("Should be initialized after initialize() call", multiInitializer.isInitialized());
    }

    @Test
    public void testInitializeWithSingleChildInitializer_ShouldExecuteSuccessfully() throws Throwable {
        // Given: A MultiBackgroundInitializer with one child initializer
        BackgroundInitializer<String> childInitializer = new BackgroundInitializer<String>() {
            @Override
            protected String initialize() throws Exception {
                return "test result";
            }
        };
        multiInitializer.addInitializer(INITIALIZER_NAME, childInitializer);

        // When: Initialize is called
        MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Then: Results should contain the child initializer's result
        assertTrue("Results should be successful", results.isSuccessful());
        assertEquals("Should return correct result", "test result", results.getResultObject(INITIALIZER_NAME));
        assertFalse("Should not have exception", results.isException(INITIALIZER_NAME));
        assertNull("Exception should be null", results.getException(INITIALIZER_NAME));
    }

    @Test
    public void testInitializeWithCustomExecutorService_ShouldUseProvidedExecutor() throws Throwable {
        // Given: A MultiBackgroundInitializer with custom executor
        ExecutorService customExecutor = new ScheduledThreadPoolExecutor(2);
        MultiBackgroundInitializer customMultiInitializer = new MultiBackgroundInitializer(customExecutor);
        
        BackgroundInitializer<Object> childInitializer = new BackgroundInitializer<Object>();
        customMultiInitializer.addInitializer(INITIALIZER_NAME, childInitializer);

        // When: Initialize is called
        MultiBackgroundInitializerResults results = customMultiInitializer.initialize();

        // Then: Should complete successfully
        assertTrue("Results should be successful", results.isSuccessful());
        customExecutor.shutdown();
    }

    // ========== Child Initializer Management Tests ==========

    @Test
    public void testAddInitializer_AfterStart_ShouldThrowException() throws Throwable {
        // Given: A started MultiBackgroundInitializer
        multiInitializer.start();
        BackgroundInitializer<Object> childInitializer = new BackgroundInitializer<Object>();

        // When/Then: Adding initializer should throw exception
        try {
            multiInitializer.addInitializer(INITIALIZER_NAME, childInitializer);
            fail("Should throw IllegalStateException when adding initializer after start");
        } catch (IllegalStateException e) {
            assertEquals("addInitializer() must not be called after start()!", e.getMessage());
        }
    }

    @Test
    public void testAddInitializer_WithNullName_ShouldThrowException() throws Throwable {
        // Given: A valid child initializer but null name
        BackgroundInitializer<Object> childInitializer = new BackgroundInitializer<Object>();

        // When/Then: Should throw NullPointerException
        try {
            multiInitializer.addInitializer(null, childInitializer);
            fail("Should throw NullPointerException for null name");
        } catch (NullPointerException e) {
            assertEquals("name", e.getMessage());
        }
    }

    @Test
    public void testAddInitializer_WithNullInitializer_ShouldThrowException() throws Throwable {
        // When/Then: Should throw NullPointerException
        try {
            multiInitializer.addInitializer(INITIALIZER_NAME, null);
            fail("Should throw NullPointerException for null initializer");
        } catch (NullPointerException e) {
            assertEquals("backgroundInitializer", e.getMessage());
        }
    }

    // ========== Results Access Tests ==========

    @Test
    public void testGetResultObject_WithNonExistentName_ShouldThrowException() throws Throwable {
        // Given: Results with no child initializers
        MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // When/Then: Accessing non-existent result should throw exception
        try {
            results.getResultObject(NON_EXISTENT_NAME);
            fail("Should throw NoSuchElementException for non-existent name");
        } catch (NoSuchElementException e) {
            assertTrue("Should mention the missing name", 
                      e.getMessage().contains("No child initializer with name " + NON_EXISTENT_NAME));
        }
    }

    @Test
    public void testGetException_WithNonExistentName_ShouldThrowException() throws Throwable {
        // Given: Results with no child initializers
        MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // When/Then: Accessing non-existent exception should throw exception
        try {
            results.getException(NON_EXISTENT_NAME);
            fail("Should throw NoSuchElementException for non-existent name");
        } catch (NoSuchElementException e) {
            assertTrue("Should mention the missing name", 
                      e.getMessage().contains("No child initializer with name " + NON_EXISTENT_NAME));
        }
    }

    @Test
    public void testGetInitializer_WithNonExistentName_ShouldThrowException() throws Throwable {
        // Given: Results with no child initializers
        MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // When/Then: Accessing non-existent initializer should throw exception
        try {
            results.getInitializer(NON_EXISTENT_NAME);
            fail("Should throw NoSuchElementException for non-existent name");
        } catch (NoSuchElementException e) {
            assertTrue("Should mention the missing name", 
                      e.getMessage().contains("No child initializer with name " + NON_EXISTENT_NAME));
        }
    }

    @Test
    public void testIsException_WithNonExistentName_ShouldThrowException() throws Throwable {
        // Given: Results with no child initializers
        MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // When/Then: Checking exception status for non-existent name should throw exception
        try {
            results.isException(NON_EXISTENT_NAME);
            fail("Should throw NoSuchElementException for non-existent name");
        } catch (NoSuchElementException e) {
            assertTrue("Should mention the missing name", 
                      e.getMessage().contains("No child initializer with name " + NON_EXISTENT_NAME));
        }
    }

    // ========== Exception Handling Tests ==========

    @Test
    public void testInitializeWithFailingChildInitializer_ShouldCaptureException() throws Throwable {
        // Given: A child initializer that throws an exception
        BackgroundInitializer<String> failingInitializer = new BackgroundInitializer<String>() {
            @Override
            protected String initialize() throws Exception {
                throw new RuntimeException("Test exception");
            }
        };
        multiInitializer.addInitializer(INITIALIZER_NAME, failingInitializer);

        // When: Initialize is called
        MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Then: Exception should be captured in results
        assertFalse("Results should not be successful when child fails", results.isSuccessful());
        assertTrue("Should indicate exception occurred", results.isException(INITIALIZER_NAME));
        assertNotNull("Exception should be captured", results.getException(INITIALIZER_NAME));
        assertNull("Result object should be null when exception occurs", results.getResultObject(INITIALIZER_NAME));
    }

    // ========== State Management Tests ==========

    @Test
    public void testTaskCount_ShouldReturnCorrectValue() throws Throwable {
        // Given: A MultiBackgroundInitializer
        // When: Getting task count
        int taskCount = multiInitializer.getTaskCount();
        
        // Then: Should return 1 (for the control task)
        assertEquals("Task count should be 1 for empty initializer", 1, taskCount);
    }

    @Test
    public void testClose_ShouldCleanupResources() throws Throwable {
        // Given: An initialized MultiBackgroundInitializer
        multiInitializer.initialize();

        // When: Close is called
        multiInitializer.close();

        // Then: Should not be started anymore
        assertFalse("Should not be started after close", multiInitializer.isStarted());
    }

    @Test
    public void testMultipleInitializeCalls_AfterFirstCall_ShouldThrowException() throws Throwable {
        // Given: An already initialized MultiBackgroundInitializer
        BackgroundInitializer<Object> childInitializer = new BackgroundInitializer<Object>();
        multiInitializer.addInitializer(INITIALIZER_NAME, childInitializer);
        multiInitializer.initialize();

        // When/Then: Second initialize call should throw exception
        try {
            multiInitializer.initialize();
            fail("Should throw IllegalStateException on second initialize call");
        } catch (IllegalStateException e) {
            assertEquals("Cannot set ExecutorService after start()!", e.getMessage());
        }
    }

    // ========== Integration Tests ==========

    @Test
    public void testCompleteWorkflow_WithMultipleInitializers_ShouldExecuteAllSuccessfully() throws Throwable {
        // Given: Multiple child initializers
        BackgroundInitializer<String> stringInitializer = new BackgroundInitializer<String>() {
            @Override
            protected String initialize() throws Exception {
                return "string result";
            }
        };
        
        BackgroundInitializer<Integer> integerInitializer = new BackgroundInitializer<Integer>() {
            @Override
            protected Integer initialize() throws Exception {
                return 42;
            }
        };

        multiInitializer.addInitializer("string", stringInitializer);
        multiInitializer.addInitializer("integer", integerInitializer);

        // When: Initialize is called
        MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Then: All results should be available
        assertTrue("Results should be successful", results.isSuccessful());
        assertEquals("String result should be correct", "string result", results.getResultObject("string"));
        assertEquals("Integer result should be correct", Integer.valueOf(42), results.getResultObject("integer"));
        assertEquals("Should have 2 initializers", 2, results.initializerNames().size());
        assertTrue("Should contain string initializer", results.initializerNames().contains("string"));
        assertTrue("Should contain integer initializer", results.initializerNames().contains("integer"));
    }

    @Test
    public void testWithForkJoinPool_ShouldExecuteSuccessfully() throws Throwable {
        // Given: A MultiBackgroundInitializer with ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MultiBackgroundInitializer pooledInitializer = new MultiBackgroundInitializer(forkJoinPool);
        
        BackgroundInitializer<String> childInitializer = new BackgroundInitializer<String>() {
            @Override
            protected String initialize() throws Exception {
                return "pooled result";
            }
        };
        pooledInitializer.addInitializer(INITIALIZER_NAME, childInitializer);

        // When: Initialize is called
        MultiBackgroundInitializerResults results = pooledInitializer.initialize();

        // Then: Should complete successfully
        assertTrue("Results should be successful", results.isSuccessful());
        assertEquals("Should return correct result", "pooled result", results.getResultObject(INITIALIZER_NAME));
        
        // Cleanup
        forkJoinPool.shutdown();
    }
}