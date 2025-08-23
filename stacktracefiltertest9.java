package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

// The class name was simplified from StackTraceFilterTestTest9 to StackTraceFilterTest
// for better clarity and to remove redundancy.
public class StackTraceFilterTest extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void filter_shouldKeepUserCodeSurroundingInternalCode_whenKeepTopIsTrue() {
        // This test simulates a stack trace where user code (e.g., a test method)
        // is at the top, followed by Mockito-internal code, and then more user code.
        // This scenario is common with spies, where a real method implementation is called.
        // We expect the filter to remove the internal frame but preserve the user code
        // from both above and below it.

        // GIVEN: A stack trace with user code, internal Mockito code, and more user code.
        final String userCodeAtTop = "org.test.MockitoSampleTest";
        final String mockitoInternalCode = "org.mockito.internal.to.be.Filtered";
        final String userCodeBelowInternal = "org.yet.another.good.Trace";
        final String userCodeAtBottom = "org.good.Trace";

        // The TraceBuilder creates a stack trace from the bottom up.
        // The last class name represents the top of the stack.
        StackTraceElement[] originalStackTrace = new TraceBuilder().classes(
            userCodeAtBottom,
            userCodeBelowInternal,
            mockitoInternalCode,
            userCodeAtTop
        ).toTraceArray();

        // WHEN: The stack trace is filtered with the 'keepTop' flag set to true.
        StackTraceElement[] filteredStackTrace = filter.filter(originalStackTrace, true);

        // THEN: The Mockito internal frame is removed, and all surrounding user code
        // is preserved in the correct top-to-bottom order.
        assertThat(filteredStackTrace)
            .extracting(StackTraceElement::getClassName)
            .withFailMessage("The filtered stack trace should only contain user code frames.")
            .containsExactly(
                userCodeAtTop,
                userCodeBelowInternal,
                userCodeAtBottom
            );
    }
}