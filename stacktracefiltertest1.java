package org.mockito.internal.exceptions.stacktrace;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

import static org.mockitoutil.Conditions.onlyThoseClasses;

/**
 * Tests for {@link StackTraceFilter} to ensure it correctly removes
 * Mockito-internal and framework-specific stack trace elements.
 */
public class StackTraceFilterTest extends TestBase {

    private static final String USER_CODE_CLASS = "MockitoExampleTest";
    private static final String CGLIB_ENHANCED_CLASS = "List$$EnhancerByMockitoWithCGLIB$$2c406024";

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldRemoveCglibEnhancedClassFromStackTrace() {
        // Arrange: Create a stack trace containing both user code and a CGLIB-generated class.
        StackTraceElement[] originalStackTrace = new TraceBuilder()
            .classes(USER_CODE_CLASS, CGLIB_ENHANCED_CLASS)
            .toTraceArray();

        // Act: Filter the stack trace. The 'keepTop' flag is false, indicating a full filtering pass.
        StackTraceElement[] filteredStackTrace = filter.filter(originalStackTrace, false);

        // Assert: Verify that only the user code class remains in the stack trace.
        Assertions.assertThat(filteredStackTrace)
            .as("Filtered stack trace should only contain user code, not CGLIB-generated classes.")
            .has(onlyThoseClasses(USER_CODE_CLASS));
    }
}