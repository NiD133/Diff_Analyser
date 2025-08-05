/*
 * Refactored test suite for MultiBackgroundInitializer
 * Focus: Readability and maintainability
 */
package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.*;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.apache.commons.lang3.concurrent.BackgroundInitializer;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.MultiBackgroundInitializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MultiBackgroundInitializer_ESTest {

    private MultiBackgroundInitializer initializer;
    private ScheduledThreadPoolExecutor executor;
    private ForkJoinPool forkJoinPool;

    @Before
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
        executor = new ScheduledThreadPoolExecutor(2);
        forkJoinPool = ForkJoinPool.commonPool();
    }

    @After
    public void tearDown() {
        initializer.close();
        executor.shutdownNow();
        forkJoinPool.shutdownNow();
    }

    // Helper method to add initializer with mock executor
    private BackgroundInitializer<Object> createMockExecutorInitializer() {
        return new BackgroundInitializer<>(forkJoinPool);
    }

    // Test successful initialization with multiple initializers
    @Test
    public void initialize_WithMultipleInitializers_ReturnsSuccessfulResults() throws Exception {
        // Setup
        BackgroundInitializer<Object> init1 = new BackgroundInitializer<>();
        BackgroundInitializer<Object> init2 = new BackgroundInitializer<>();
        
        initializer.addInitializer("init1", init1);
        initializer.addInitializer("init2", init2);

        // Execute
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.initialize();

        // Verify
        assertTrue("Should be successful", results.isSuccessful());
        assertNotNull("Should have result for init1", results.getResultObject("init1"));
        assertFalse("No exception for init1", results.isException("init1"));
    }

    // Test exception handling during initialization
    @Test
    public void getException_AfterFailedInitialization_ReturnsException() throws Exception {
        // Setup failing initializer
        BackgroundInitializer<Object> failingInitializer = new BackgroundInitializer<Object>() {
            @Override
            protected Object initialize() throws Exception {
                throw new ConcurrentException("Simulated failure");
            }
        };
        initializer.addInitializer("failing", failingInitializer);
        
        // Execute
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.initialize();
        
        // Verify
        assertNotNull("Should have exception", results.getException("failing"));
        assertTrue("Should have exception flag", results.isException("failing"));
        assertNull("Result object should be null", results.getResultObject("failing"));
    }

    // Test behavior when adding initializer after start
    @Test(expected = IllegalStateException.class)
    public void addInitializer_AfterStart_ThrowsIllegalStateException() {
        // Setup
        initializer.start();
        BackgroundInitializer<Object> lateInitializer = new BackgroundInitializer<>();
        
        // Should throw
        initializer.addInitializer("late", lateInitializer);
    }

    // Test unknown initializer name handling
    @Test(expected = NoSuchElementException.class)
    public void getInitializer_WithInvalidName_ThrowsNoSuchElementException() throws Exception {
        // Setup
        initializer.initialize();  // Empty initializer
        
        // Execute & Verify
        initializer.initialize().getInitializer("invalid_name");
    }

    // Test task count calculation
    @Test
    public void getTaskCount_WithChildInitializers_ReturnsCorrectCount() {
        // Setup
        initializer.addInitializer("child1", new BackgroundInitializer<>());
        initializer.addInitializer("child2", new BackgroundInitializer<>());
        
        // Verify
        assertEquals("Task count should include children + controller", 
                     3, initializer.getTaskCount());
    }

    // Test close method cleans up resources
    @Test
    public void close_WithChildInitializers_ReleasesResources() {
        // Setup
        BackgroundInitializer<Object> child = new BackgroundInitializer<>(executor);
        initializer.addInitializer("child", child);
        
        // Execute
        initializer.close();
        
        // Verify
        assertTrue("Executor should be shutdown", executor.isShutdown());
    }

    // Test initialization status
    @Test
    public void isInitialized_AfterInitializeCall_ReturnsTrue() throws Exception {
        // Setup
        initializer.addInitializer("test", new BackgroundInitializer<>());
        initializer.initialize();
        
        // Verify
        assertTrue("Should be initialized", initializer.isInitialized());
    }

    // Test executor sharing behavior
    @Test
    public void initialize_WithExternalExecutor_SharedWithChildren() throws Exception {
        // Setup
        MultiBackgroundInitializer customInitializer = new MultiBackgroundInitializer(executor);
        customInitializer.addInitializer("child", new BackgroundInitializer<>());
        
        // Execute
        customInitializer.initialize();
        
        // Verify
        assertTrue("Executor should be active", !executor.isShutdown());
    }

    // Test duplicate initialization calls
    @Test
    public void initialize_MultipleCalls_ReturnsSameResults() throws Exception {
        // Setup
        initializer.addInitializer("child", new BackgroundInitializer<>());
        
        // Execute
        MultiBackgroundInitializer.MultiBackgroundInitializerResults firstResults = initializer.initialize();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults secondResults = initializer.initialize();
        
        // Verify
        assertSame("Should return same results object", firstResults, secondResults);
    }

    // Test null parameters
    @Test(expected = NullPointerException.class)
    public void addInitializer_WithNullName_ThrowsNullPointerException() {
        initializer.addInitializer(null, new BackgroundInitializer<>());
    }

    @Test(expected = NullPointerException.class)
    public void addInitializer_WithNullInitializer_ThrowsNullPointerException() {
        initializer.addInitializer("name", null);
    }

    // Test results methods with empty initializer
    @Test
    public void getResults_WithNoInitializers_ReturnsEmptyCollections() throws Exception {
        // Execute
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.initialize();
        
        // Verify
        assertTrue("Should have no initializers", results.initializerNames().isEmpty());
        assertTrue("Should be successful", results.isSuccessful());
    }

    // Test executor cleanup after close
    @Test
    public void close_WithoutExternalExecutor_ClearsInternalExecutor() {
        // Setup
        initializer.addInitializer("child", new BackgroundInitializer<>());
        
        // Execute
        initializer.close();
        
        // Verify
        assertNull("Internal executor should be cleared", initializer.getExternalExecutor());
    }
}