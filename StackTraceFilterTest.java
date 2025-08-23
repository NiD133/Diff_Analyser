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

public class StackTraceFilterTest extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void testFilterOutCglibGeneratedClasses() {
        // Arrange: Create a stack trace with CGLIB generated classes
        StackTraceElement[] stackTrace = new TraceBuilder()
                .classes("MockitoExampleTest", "List$$EnhancerByMockitoWithCGLIB$$2c406024")
                .toTraceArray();

        // Act: Filter the stack trace
        StackTraceElement[] filteredStackTrace = filter.filter(stackTrace, false);

        // Assert: Ensure only the relevant classes remain
        Assertions.assertThat(filteredStackTrace).has(onlyThoseClasses("MockitoExampleTest"));
    }

    @Test
    public void testFilterOutByteBuddyGeneratedClasses() {
        // Arrange: Create a stack trace with ByteBuddy generated classes
        StackTraceElement[] stackTrace = new TraceBuilder()
                .classes("MockitoExampleTest", "org.testcase.MockedClass$MockitoMock$1882975947.doSomething(Unknown Source)")
                .toTraceArray();

        // Act: Filter the stack trace
        StackTraceElement[] filteredStackTrace = filter.filter(stackTrace, false);

        // Assert: Ensure only the relevant classes remain
        Assertions.assertThat(filteredStackTrace).has(onlyThoseClasses("MockitoExampleTest"));
    }

    @Test
    public void testFilterOutMockitoPackageClasses() {
        // Arrange: Create a stack trace with Mockito package classes
        StackTraceElement[] stackTrace = new TraceBuilder()
                .classes("org.test.MockitoSampleTest", "org.mockito.Mockito")
                .toTraceArray();

        // Act: Filter the stack trace
        StackTraceElement[] filteredStackTrace = filter.filter(stackTrace, false);

        // Assert: Ensure only the relevant classes remain
        Assertions.assertThat(filteredStackTrace).has(onlyThoseClasses("org.test.MockitoSampleTest"));
    }

    @Test
    public void testPreserveMiddleGoodTraces() {
        // Arrange: Create a stack trace with good traces in the middle
        StackTraceElement[] stackTrace = new TraceBuilder()
                .classes("org.test.MockitoSampleTest", "org.test.TestSupport", "org.mockito.Mockito", "org.test.TestSupport", "org.mockito.Mockito")
                .toTraceArray();

        // Act: Filter the stack trace
        StackTraceElement[] filteredStackTrace = filter.filter(stackTrace, false);

        // Assert: Ensure the good traces in the middle are preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses("org.test.TestSupport", "org.test.TestSupport", "org.test.MockitoSampleTest"));
    }

    @Test
    public void testPreserveRunnerClasses() {
        // Arrange: Create a stack trace with runner classes
        StackTraceElement[] stackTrace = new TraceBuilder()
                .classes("org.mockito.runners.Runner", "junit.stuff", "org.test.MockitoSampleTest", "org.mockito.Mockito")
                .toTraceArray();

        // Act: Filter the stack trace
        StackTraceElement[] filteredStackTrace = filter.filter(stackTrace, false);

        // Assert: Ensure runner classes are preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses("org.test.MockitoSampleTest", "junit.stuff", "org.mockito.runners.Runner"));
    }

    @Test
    public void testPreserveElementsAboveMockitoJUnitRule() {
        // Arrange: Create a stack trace with elements above Mockito JUnit rule
        StackTraceElement[] stackTrace = new TraceBuilder()
                .classes("org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)", "org.mockito.runners.Runner", "junit.stuff", "org.test.MockitoSampleTest", "org.mockito.internal.MockitoCore.verifyNoMoreInteractions", "org.mockito.internal.debugging.LocationImpl")
                .toTraceArray();

        // Act: Filter the stack trace
        StackTraceElement[] filteredStackTrace = filter.filter(stackTrace, false);

        // Assert: Ensure elements above Mockito JUnit rule are preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses("org.test.MockitoSampleTest", "junit.stuff", "org.mockito.runners.Runner", "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)"));
    }

    @Test
    public void testPreserveInternalRunnerClasses() {
        // Arrange: Create a stack trace with internal runner classes
        StackTraceElement[] stackTrace = new TraceBuilder()
                .classes("org.mockito.internal.runners.Runner", "org.test.MockitoSampleTest")
                .toTraceArray();

        // Act: Filter the stack trace
        StackTraceElement[] filteredStackTrace = filter.filter(stackTrace, false);

        // Assert: Ensure internal runner classes are preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses("org.test.MockitoSampleTest", "org.mockito.internal.runners.Runner"));
    }

    @Test
    public void testStartFilteringAndKeepTopElements() {
        // Arrange: Create a stack trace with bad elements in the middle
        StackTraceElement[] stackTrace = new TraceBuilder()
                .classes("org.test.Good", "org.mockito.internal.Bad", "org.test.MockitoSampleTest")
                .toTraceArray();

        // Act: Filter the stack trace with keepTop flag
        StackTraceElement[] filteredStackTrace = filter.filter(stackTrace, true);

        // Assert: Ensure top elements are preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses("org.test.MockitoSampleTest", "org.test.Good"));
    }

    @Test
    public void testPreserveGoodTracesFromTop() {
        // Arrange: Create a stack trace with good traces at the top
        StackTraceElement[] stackTrace = new TraceBuilder()
                .classes("org.good.Trace", "org.yet.another.good.Trace", "org.mockito.internal.to.be.Filtered", "org.test.MockitoSampleTest")
                .toTraceArray();

        // Act: Filter the stack trace with keepTop flag
        StackTraceElement[] filteredStackTrace = filter.filter(stackTrace, true);

        // Assert: Ensure good traces from the top are preserved
        Assertions.assertThat(filteredStackTrace)
                .has(onlyThoseClasses("org.test.MockitoSampleTest", "org.yet.another.good.Trace", "org.good.Trace"));
    }

    @Test
    public void testReturnEmptyArrayWhenInputIsEmpty() {
        // Act: Filter an empty stack trace
        StackTraceElement[] filteredStackTrace = filter.filter(new StackTraceElement[0], false);

        // Assert: Ensure the result is an empty array
        assertEquals(0, filteredStackTrace.length);
    }
}