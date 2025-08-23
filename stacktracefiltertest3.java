package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;

/**
 * Tests for {@link StackTraceFilter}.
 */
public class StackTraceFilterTest {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldRemoveMockitoClassesFromStackTrace() {
        // Arrange: Create a synthetic stack trace with a user class and a Mockito class.
        StackTraceElement[] originalStackTrace = new TraceBuilder()
            .classes(
                "org.test.MockitoSampleTest", // A user class, should be kept.
                "org.mockito.Mockito"         // A Mockito class, should be filtered out.
            )
            .toTraceArray();

        // Act: Filter the stack trace. The 'keepTop' flag is false.
        StackTraceElement[] filteredStackTrace = filter.filter(originalStackTrace, false);

        // Assert: Verify that only the user class remains in the stack trace.
        assertThat(filteredStackTrace)
            .extracting(StackTraceElement::getClassName)
            .containsExactly("org.test.MockitoSampleTest");
    }
}