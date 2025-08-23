package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

/**
 * Tests for {@link StackTraceFilter}.
 */
// The test class was renamed from StackTraceFilterTestTest7 to StackTraceFilterTest
// for clarity and to remove redundancy.
public class StackTraceFilterTest extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    // The test method was renamed to clearly describe the specific behavior being tested
    // using a "when_then" or "should" convention.
    public void filter_shouldKeepMockitoInternalRunners() {
        // Arrange: Create a synthetic stack trace with a Mockito runner and a user test class.
        // The filter should normally remove Mockito-internal classes, but runners are a
        // special case and should be kept.
        final String mockitoRunnerClassName = "org.mockito.internal.runners.Runner";
        final String userTestClassName = "org.test.MockitoSampleTest";

        StackTraceElement[] originalStackTrace = new TraceBuilder()
            .classes(mockitoRunnerClassName, userTestClassName)
            .toTraceArray();

        // Act: Filter the stack trace. The 'keepTop' flag is false, meaning no special
        // handling for the top-of-stack element.
        StackTraceElement[] filteredStackTrace = filter.filter(originalStackTrace, false);

        // Assert: Verify that both the runner and the user test class remain in the
        // filtered trace, in their original order. This confirms the special rule for runners.
        // The assertion was changed to a standard, more expressive AssertJ assertion.
        assertThat(filteredStackTrace)
            .extracting(StackTraceElement::getClassName)
            .containsExactly(mockitoRunnerClassName, userTestClassName);
    }
}