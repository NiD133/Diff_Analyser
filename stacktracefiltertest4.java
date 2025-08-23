package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

public class StackTraceFilterTestTest4 extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    private static final String USER_CODE_CLASS = "org.test.MockitoSampleTest";
    private static final String USER_TEST_SUPPORT_CLASS = "org.test.TestSupport";
    private static final String MOCKITO_INTERNAL_CLASS = "org.mockito.Mockito";

    @Test
    public void shouldKeepUserCodeFramesWhenSurroundedByInternalFrames() {
        // Given a stack trace where "good" user frames are interleaved with "bad" Mockito frames.
        // This setup specifically tests that a good frame is not removed even if it's
        // between two bad frames.
        StackTraceElement[] unfilteredStackTrace = new TraceBuilder()
            .classes(
                USER_CODE_CLASS,          // Kept
                USER_TEST_SUPPORT_CLASS,  // Kept
                MOCKITO_INTERNAL_CLASS,   // Filtered
                USER_TEST_SUPPORT_CLASS,  // Kept (this is the key element for this test)
                MOCKITO_INTERNAL_CLASS    // Filtered
            )
            .toTraceArray();

        // When the stack trace is filtered
        StackTraceElement[] filteredStackTrace = filter.filter(unfilteredStackTrace, false);

        // Then only the Mockito internal frames should be removed, and user frames are kept in order
        assertThat(filteredStackTrace)
            .extracting(StackTraceElement::getClassName)
            .containsExactly(
                USER_CODE_CLASS,
                USER_TEST_SUPPORT_CLASS,
                USER_TEST_SUPPORT_CLASS
            );
    }
}