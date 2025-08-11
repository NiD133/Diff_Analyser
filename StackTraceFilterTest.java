/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.stacktrace;

import static org.junit.Assert.assertEquals;
import static org.mockitoutil.Conditions.onlyThoseClasses;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

/**
 * Tests for StackTraceFilter which removes Mockito-internal and framework-generated
 * classes from stack traces to show only user-relevant code.
 */
public class StackTraceFilterTest extends TestBase {

    private final StackTraceFilter stackTraceFilter = new StackTraceFilter();

    // Constants for commonly used test class names
    private static final String USER_TEST_CLASS = "MockitoExampleTest";
    private static final String SAMPLE_TEST_CLASS = "org.test.MockitoSampleTest";
    private static final String TEST_SUPPORT_CLASS = "org.test.TestSupport";
    private static final String MOCKITO_CORE_CLASS = "org.mockito.Mockito";

    @Test
    public void should_filter_out_cglib_generated_proxy_classes() {
        // Given: Stack trace containing user code and CGLIB-generated proxy class
        StackTraceElement[] originalStackTrace = createStackTrace(
                USER_TEST_CLASS,
                "List$$EnhancerByMockitoWithCGLIB$$2c406024" // CGLIB proxy class
        );

        // When: Filtering the stack trace
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, false);

        // Then: Only user code should remain
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses(USER_TEST_CLASS));
    }

    @Test
    public void should_filter_out_bytebuddy_generated_proxy_classes() {
        // Given: Stack trace containing user code and ByteBuddy-generated proxy class
        StackTraceElement[] originalStackTrace = createStackTrace(
                USER_TEST_CLASS,
                "org.testcase.MockedClass$MockitoMock$1882975947.doSomething(Unknown Source)" // ByteBuddy proxy
        );

        // When: Filtering the stack trace
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, false);

        // Then: Only user code should remain
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses(USER_TEST_CLASS));
    }

    @Test
    public void should_filter_out_mockito_internal_classes() {
        // Given: Stack trace containing user test code and Mockito internal classes
        StackTraceElement[] originalStackTrace = createStackTrace(
                SAMPLE_TEST_CLASS,
                MOCKITO_CORE_CLASS // Mockito internal class
        );

        // When: Filtering the stack trace
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, false);

        // Then: Only user test code should remain
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses(SAMPLE_TEST_CLASS));
    }

    @Test
    public void should_preserve_user_code_that_appears_between_mockito_internal_calls() {
        // Given: Stack trace with user code interleaved between Mockito internal calls
        // Pattern: [user_test] -> [user_support] -> [mockito] -> [user_support] -> [mockito]
        StackTraceElement[] originalStackTrace = createStackTrace(
                SAMPLE_TEST_CLASS,    // User test code
                TEST_SUPPORT_CLASS,   // User support code
                MOCKITO_CORE_CLASS,   // Mockito internal (filtered out)
                TEST_SUPPORT_CLASS,   // User support code (should be kept)
                MOCKITO_CORE_CLASS    // Mockito internal (filtered out)
        );

        // When: Filtering the stack trace
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, false);

        // Then: All user code should be preserved, Mockito internals removed
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        TEST_SUPPORT_CLASS,   // Second occurrence
                        TEST_SUPPORT_CLASS,   // First occurrence  
                        SAMPLE_TEST_CLASS     // Test class
                ));
    }

    @Test
    public void should_preserve_test_runner_classes() {
        // Given: Stack trace containing test runners, JUnit, user code, and Mockito internals
        StackTraceElement[] originalStackTrace = createStackTrace(
                "org.mockito.runners.Runner", // Mockito test runner (should be kept)
                "junit.stuff",                // JUnit framework (should be kept)
                SAMPLE_TEST_CLASS,           // User test code
                MOCKITO_CORE_CLASS           // Mockito internal (filtered out)
        );

        // When: Filtering the stack trace
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, false);

        // Then: Test runners and user code should be preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        SAMPLE_TEST_CLASS,
                        "junit.stuff",
                        "org.mockito.runners.Runner"
                ));
    }

    @Test
    public void should_preserve_stack_trace_elements_above_junit_rule() {
        // Given: Stack trace that includes JUnit rule execution context
        StackTraceElement[] originalStackTrace = createStackTrace(
                "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)", // JUnit rule context
                "org.mockito.runners.Runner",                                          // Test runner
                "junit.stuff",                                                         // JUnit framework
                SAMPLE_TEST_CLASS,                                                    // User test code
                "org.mockito.internal.MockitoCore.verifyNoMoreInteractions",         // Mockito internal
                "org.mockito.internal.debugging.LocationImpl"                        // Mockito internal
        );

        // When: Filtering the stack trace
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, false);

        // Then: Everything above JUnit rule should be preserved, Mockito internals filtered
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        SAMPLE_TEST_CLASS,
                        "junit.stuff",
                        "org.mockito.runners.Runner",
                        "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)"
                ));
    }

    @Test
    public void should_preserve_mockito_internal_runner_classes() {
        // Given: Stack trace with Mockito internal runner (special case - should be kept)
        StackTraceElement[] originalStackTrace = createStackTrace(
                "org.mockito.internal.runners.Runner", // Internal runner (exception to filtering rule)
                SAMPLE_TEST_CLASS                      // User test code
        );

        // When: Filtering the stack trace
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, false);

        // Then: Both internal runner and user code should be preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        SAMPLE_TEST_CLASS,
                        "org.mockito.internal.runners.Runner"
                ));
    }

    @Test
    public void should_keep_top_elements_when_keepTop_flag_is_enabled() {
        // Given: Stack trace with good code at top, then bad (internal), then more good code
        StackTraceElement[] originalStackTrace = createStackTrace(
                "org.test.Good",              // User code at top
                "org.mockito.internal.Bad",   // Mockito internal (filtered)
                SAMPLE_TEST_CLASS            // User test code
        );

        // When: Filtering with keepTop=true (preserves elements from the top)
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, true);

        // Then: Top user code and bottom test code should be preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses(SAMPLE_TEST_CLASS, "org.test.Good"));
    }

    @Test
    public void should_preserve_multiple_good_traces_from_top_for_spy_real_method_calls() {
        // Given: Stack trace from spy calling real implementation (multiple good traces at top)
        // This scenario occurs when spies call real methods that may throw exceptions
        StackTraceElement[] originalStackTrace = createStackTrace(
                "org.good.Trace",                        // Real implementation code
                "org.yet.another.good.Trace",           // More real implementation code  
                "org.mockito.internal.to.be.Filtered",  // Mockito internal (filtered)
                SAMPLE_TEST_CLASS                       // User test code
        );

        // When: Filtering with keepTop=true
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(originalStackTrace, true);

        // Then: All good traces should be preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        SAMPLE_TEST_CLASS,
                        "org.yet.another.good.Trace",
                        "org.good.Trace"
                ));
    }

    @Test
    public void should_return_empty_array_when_input_is_empty() {
        // Given: Empty stack trace
        StackTraceElement[] emptyStackTrace = new StackTraceElement[0];

        // When: Filtering empty stack trace
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(emptyStackTrace, false);

        // Then: Result should also be empty
        assertEquals("Filtered empty stack trace should remain empty", 0, filteredStackTrace.length);
    }

    /**
     * Helper method to create stack traces for testing.
     * Improves readability by centralizing stack trace creation logic.
     */
    private StackTraceElement[] createStackTrace(String... classNames) {
        return new TraceBuilder()
                .classes(classNames)
                .toTraceArray();
    }
}