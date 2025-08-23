package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for exception handling in {@link MultiBackgroundInitializer}.
 *
 * This test focuses on the scenario where a child initializer throws a
 * RuntimeException during its execution.
 */
public class MultiBackgroundInitializerExceptionHandlingTest extends AbstractLangTest {

    /** A constant for the name of the child initializer used in tests. */
    private static final String CHILD_INITIALIZER_NAME = "childInitializer";

    /** The initializer instance under test. */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    @Test
    @DisplayName("get() should rethrow the RuntimeException from a child initializer")
    void getShouldRethrowRuntimeExceptionFromChild() {
        // Given: A child initializer configured to throw a RuntimeException.
        final var failingChild = new FailingChildInitializer();
        final var testException = new RuntimeException("Initialization failed!");
        failingChild.setExceptionToThrow(testException);
        initializer.addInitializer(CHILD_INITIALIZER_NAME, failingChild);

        // When: The main initializer is started.
        initializer.start();

        // Then: Calling get() on the main initializer should rethrow the original exception.
        final RuntimeException thrown = assertThrows(RuntimeException.class, initializer::get,
            "Expected a RuntimeException to be thrown.");
        assertEquals(testException, thrown,
            "The thrown exception should be the same instance as the one from the child.");
    }

    /**
     * A test-specific {@link BackgroundInitializer} that can be configured to throw an
     * exception during its {@code initialize()} method. The result type is {@code Object}
     * as it is not relevant for this exception test.
     */
    private static class FailingChildInitializer extends BackgroundInitializer<Object> {

        private Exception exceptionToThrow;

        /**
         * Configures the exception that this initializer will throw upon execution.
         * @param ex The exception to be thrown.
         */
        public void setExceptionToThrow(final Exception ex) {
            this.exceptionToThrow = ex;
        }

        /**
         * Throws the configured exception if one is set. Otherwise, returns a dummy object.
         */
        @Override
        protected Object initialize() throws Exception {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            // A result is required by the method signature, but it's not used in this test.
            return new Object();
        }
    }
}