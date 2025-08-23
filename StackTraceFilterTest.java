/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;

/**
 * Tests for StackTraceFilter.
 *
 * Conventions used in these tests:
 * - We build artificial stack traces from strings using TraceBuilder. Each string represents
 *   a "frame identifier" that ends up as the StackTraceElement#getClassName().
 * - We assert on the exact sequence of those identifiers after filtering.
 * - TraceBuilder constructs frames in call order (first argument is the bottom of the stack,
 *   last argument is the top). The filter returns frames ordered from the topmost user-relevant
 *   frame downwards (hence the expectations look reversed compared to the inputs).
 */
public class StackTraceFilterTest {

    private static final String TEST_CLASS = "org.test.MockitoSampleTest";
    private static final String JUNIT_FRAME = "junit.stuff";
    private static final String PUBLIC_RUNNER = "org.mockito.runners.Runner";
    private static final String INTERNAL_RUNNER = "org.mockito.internal.runners.Runner";

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void filters_out_cglib_generated_frames() {
        StackTraceElement[] input =
                trace("MockitoExampleTest", "List$$EnhancerByMockitoWithCGLIB$$2c406024");

        StackTraceElement[] filtered = filter.filter(input, false);

        assertKept(filtered, "MockitoExampleTest");
    }

    @Test
    public void filters_out_byte_buddy_generated_frames() {
        StackTraceElement[] input =
                trace(
                        "MockitoExampleTest",
                        "org.testcase.MockedClass$MockitoMock$1882975947.doSomething(Unknown Source)");

        StackTraceElement[] filtered = filter.filter(input, false);

        assertKept(filtered, "MockitoExampleTest");
    }

    @Test
    public void filters_out_mockito_package_frames() {
        StackTraceElement[] input = trace("org.test.MockitoSampleTest", "org.mockito.Mockito");

        StackTraceElement[] filtered = filter.filter(input, false);

        assertKept(filtered, TEST_CLASS);
    }

    @Test
    public void keeps_good_frames_in_between_filtered_ones() {
        StackTraceElement[] input =
                trace(
                        TEST_CLASS,
                        "org.test.TestSupport",
                        "org.mockito.Mockito",
                        "org.test.TestSupport",
                        "org.mockito.Mockito");

        StackTraceElement[] filtered = filter.filter(input, false);

        assertKept(filtered, "org.test.TestSupport", "org.test.TestSupport", TEST_CLASS);
    }

    @Test
    public void keeps_junit_and_runner_frames() {
        StackTraceElement[] input =
                trace(PUBLIC_RUNNER, JUNIT_FRAME, TEST_CLASS, "org.mockito.Mockito");

        StackTraceElement[] filtered = filter.filter(input, false);

        assertKept(filtered, TEST_CLASS, JUNIT_FRAME, PUBLIC_RUNNER);
    }

    @Test
    public void does_not_filter_frames_above_mockito_junit_rule() {
        StackTraceElement[] input =
                trace(
                        "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)",
                        PUBLIC_RUNNER,
                        JUNIT_FRAME,
                        TEST_CLASS,
                        "org.mockito.internal.MockitoCore.verifyNoMoreInteractions",
                        "org.mockito.internal.debugging.LocationImpl");

        StackTraceElement[] filtered = filter.filter(input, false);

        assertKept(
                filtered,
                TEST_CLASS,
                JUNIT_FRAME,
                PUBLIC_RUNNER,
                "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)");
    }

    @Test
    public void keeps_internal_runners() {
        StackTraceElement[] input = trace(INTERNAL_RUNNER, TEST_CLASS);

        StackTraceElement[] filtered = filter.filter(input, false);

        assertKept(filtered, TEST_CLASS, INTERNAL_RUNNER);
    }

    @Test
    public void when_keepTop_true_filters_from_first_mockito_frame_but_keeps_topmost_good() {
        StackTraceElement[] input = trace("org.test.Good", "org.mockito.internal.Bad", TEST_CLASS);

        StackTraceElement[] filtered = filter.filter(input, true);

        assertKept(filtered, TEST_CLASS, "org.test.Good");
    }

    @Test
    public void when_keepTop_true_keeps_real_implementation_frames_thrown_by_spies() {
        StackTraceElement[] input =
                trace(
                        "org.good.Trace",
                        "org.yet.another.good.Trace",
                        "org.mockito.internal.to.be.Filtered",
                        TEST_CLASS);

        StackTraceElement[] filtered = filter.filter(input, true);

        assertKept(filtered, TEST_CLASS, "org.yet.another.good.Trace", "org.good.Trace");
    }

    @Test
    public void returns_empty_array_for_empty_input() {
        StackTraceElement[] filtered = filter.filter(new StackTraceElement[0], false);

        assertThat(filtered).isEmpty();
    }

    // Helpers

    private static StackTraceElement[] trace(String... frames) {
        return new TraceBuilder().classes(frames).toTraceArray();
    }

    private static void assertKept(StackTraceElement[] actual, String... expectedFrameIds) {
        assertThat(frameIds(actual)).containsExactly(expectedFrameIds);
    }

    private static List<String> frameIds(StackTraceElement[] trace) {
        return Arrays.stream(trace).map(StackTraceElement::getClassName).collect(Collectors.toList());
    }
}