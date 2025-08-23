package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

// The test class was renamed from StackTraceFilterTestTest2 to remove redundancy.
public class StackTraceFilterTest extends TestBase {

    // Descriptive constants replace "magic strings", clarifying the role of each class name.
    private static final String USER_CODE_CLASS = "MockitoExampleTest";
    private static final String MOCKITO_INTERNAL_CLASS = "org.testcase.MockedClass$MockitoMock$1882975947";

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    // The test method was renamed to be more specific and professional.
    public void shouldRemoveMockitoImplementationClassesFromStackTrace() {
        // --- Arrange ---
        // Create a stack trace containing one element from user code and one from Mockito's internals.
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
            .classes(
                USER_CODE_CLASS,        // This element represents user code and should be kept.
                MOCKITO_INTERNAL_CLASS  // This element is an internal Mockito class and should be filtered out.
            )
            .toTraceArray();

        // --- Act ---
        // The 'keepTop' argument is false, so the filter removes unwanted lines from the entire trace.
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // --- Assert ---
        // Verify that only the user code element remains after filtering.
        assertThat(filteredStackTrace)
            .as("The filtered stack trace should contain exactly one element")
            .hasSize(1);

        assertThat(filteredStackTrace[0].getClassName())
            .as("The remaining element should be the user's code")
            .isEqualTo(USER_CODE_CLASS);
    }
}