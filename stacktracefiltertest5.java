package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockitoutil.Conditions.onlyThoseClasses;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

/**
 * Tests for {@link StackTraceFilter}.
 * Renamed from StackTraceFilterTestTest5 for clarity.
 */
public class StackTraceFilterTest extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldNotFilterOutMockitoRunnerClassesFromStackTrace() {
        // The StackTraceFilter is designed to remove Mockito-internal classes from stack traces
        // to make them cleaner for the user. However, it should NOT remove classes related
        // to Mockito runners (e.g., `org.mockito.runners.Runner`), as they are relevant to the user.
        // This test verifies that this exception to the filtering rule is correctly applied.

        // Arrange
        final String userTestClass = "org.test.MockitoSampleTest";
        final String junitClass = "junit.stuff";
        final String mockitoRunnerClass = "org.mockito.runners.Runner"; // This class should be kept.
        final String mockitoInternalClass = "org.mockito.Mockito";     // This class should be filtered out.

        StackTraceElement[] unfilteredStackTrace = new TraceBuilder().classes(
            mockitoRunnerClass,
            junitClass,
            userTestClass,
            mockitoInternalClass
        ).toTraceArray();

        // Act
        // The 'keepTop' argument is false, meaning we don't have special handling for the top element.
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // Assert
        assertThat(filteredStackTrace)
            .as("Filtered stack trace should retain user, JUnit, and Mockito runner classes, but exclude other Mockito internals")
            .has(onlyThoseClasses(
                userTestClass,
                junitClass,
                mockitoRunnerClass
            ));
    }
}