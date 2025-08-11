/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockitoutil.Conditions.onlyThoseClasses;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

public class StackTraceFilterTest extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldFilterOutCglibGeneratedClasses() {
        // Given: Stack trace containing test class and CGLIB-generated mock class
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
                .classes("MockitoExampleTest", "List$$EnhancerByMockitoWithCGLIB$$2c406024")
                .toTraceArray();

        // When: Filter is applied
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // Then: Only test class remains
        assertThat(filteredStackTrace).has(onlyThoseClasses("MockitoExampleTest"));
    }

    @Test
    public void shouldFilterOutByteBuddyGeneratedClasses() {
        // Given: Stack trace containing test class and ByteBuddy-generated mock class
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
                .classes(
                        "MockitoExampleTest",
                        "org.testcase.MockedClass$MockitoMock$1882975947.doSomething(Unknown Source)")
                .toTraceArray();

        // When: Filter is applied
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // Then: Only test class remains
        assertThat(filteredStackTrace).has(onlyThoseClasses("MockitoExampleTest"));
    }

    @Test
    public void shouldFilterOutClassesFromMockitoPackage() {
        // Given: Stack trace containing test class and Mockito internals
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
                .classes("org.test.MockitoSampleTest", "org.mockito.Mockito")
                .toTraceArray();

        // When: Filter is applied
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // Then: Only test class remains
        assertThat(filteredStackTrace).has(onlyThoseClasses("org.test.MockitoSampleTest"));
    }

    @Test
    public void shouldRetainNonMockitoClassesSurroundedByMockitoClasses() {
        // Given: Stack trace with non-Mockito classes surrounded by Mockito classes
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
                .classes(
                        "org.test.MockitoSampleTest",
                        "org.test.TestSupport",
                        "org.mockito.Mockito",
                        "org.test.TestSupport",
                        "org.mockito.Mockito")
                .toTraceArray();

        // When: Filter is applied
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // Then: Only non-Mockito classes are retained
        assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        "org.test.MockitoSampleTest",
                        "org.test.TestSupport",
                        "org.test.TestSupport"));
    }

    @Test
    public void shouldRetainJUnitRunnerClasses() {
        // Given: Stack trace containing JUnit runner and Mockito internals
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
                .classes(
                        "org.mockito.runners.Runner",
                        "junit.stuff",
                        "org.test.MockitoSampleTest",
                        "org.mockito.Mockito")
                .toTraceArray();

        // When: Filter is applied
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // Then: Runner and test classes are retained
        assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        "org.test.MockitoSampleTest",
                        "junit.stuff",
                        "org.mockito.runners.Runner"));
    }

    @Test
    public void shouldRetainElementsAboveJUnitRule() {
        // Given: Stack trace with elements above JUnitRule
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
                .classes(
                        "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)",
                        "org.mockito.runners.Runner",
                        "junit.stuff",
                        "org.test.MockitoSampleTest",
                        "org.mockito.internal.MockitoCore.verifyNoMoreInteractions",
                        "org.mockito.internal.debugging.LocationImpl")
                .toTraceArray();

        // When: Filter is applied
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // Then: Elements above JUnitRule are retained
        assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        "org.test.MockitoSampleTest",
                        "junit.stuff",
                        "org.mockito.runners.Runner",
                        "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)"));
    }

    @Test
    public void shouldRetainInternalRunnerClasses() {
        // Given: Stack trace containing internal Mockito runner
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
                .classes(
                        "org.mockito.internal.runners.Runner", 
                        "org.test.MockitoSampleTest")
                .toTraceArray();

        // When: Filter is applied
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // Then: Internal runner and test class are retained
        assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        "org.test.MockitoSampleTest",
                        "org.mockito.internal.runners.Runner"));
    }

    @Test
    public void shouldKeepTopElementsWhenKeepTopIsTrue() {
        // Given: Stack trace with good elements at top
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
                .classes(
                        "org.test.Good",
                        "org.mockito.internal.Bad",
                        "org.test.MockitoSampleTest")
                .toTraceArray();

        // When: Filter is applied with keepTop=true
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, true);

        // Then: Top good elements are retained
        assertThat(filteredStackTrace)
                .has(onlyThoseClasses("org.test.MockitoSampleTest", "org.test.Good"));
    }

    @Test
    public void shouldRetainGoodTopElementsForSpyImplementations() {
        // Given: Stack trace with good elements at top followed by Mockito internals
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
                .classes(
                        "org.good.Trace",
                        "org.yet.another.good.Trace",
                        "org.mockito.internal.to.be.Filtered",
                        "org.test.MockitoSampleTest")
                .toTraceArray();

        // When: Filter is applied with keepTop=true
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, true);

        // Then: All good elements are retained in order
        assertThat(filteredStackTrace)
                .has(onlyThoseClasses(
                        "org.test.MockitoSampleTest",
                        "org.yet.another.good.Trace",
                        "org.good.Trace"));
    }

    @Test
    public void shouldReturnEmptyArrayForEmptyInput() {
        // Given: Empty stack trace
        StackTraceElement[] emptyStackTrace = new StackTraceElement[0];

        // When: Filter is applied
        StackTraceElement[] filteredStackTrace = filter.filter(emptyStackTrace, false);

        // Then: Result is empty array
        assertThat(filteredStackTrace).isEmpty();
    }
}