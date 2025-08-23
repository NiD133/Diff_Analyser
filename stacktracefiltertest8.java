package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class StackTraceFilterTestTest8 extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldRemoveInternalFramesLocatedBetweenUserCodeFrames() {
        // Given: A stack trace where an internal Mockito frame is surrounded by user code frames.
        final String userCodeClass = "org.test.Good";
        final String mockitoInternalClass = "org.mockito.internal.Bad"; // This class should be filtered out.
        final String userTestClass = "org.test.MockitoSampleTest";

        // The TraceBuilder creates a stack trace where the last class is the top of the stack.
        // Stack trace order: userTestClass -> mockitoInternalClass -> userCodeClass
        StackTraceElement[] originalStackTrace = new TraceBuilder()
            .classes(userCodeClass, mockitoInternalClass, userTestClass)
            .toTraceArray();

        // When: The stack trace is filtered.
        // The 'keepTop' parameter being true enables the standard filtering behavior.
        StackTraceElement[] filteredStackTrace = filter.filter(originalStackTrace, true);

        // Then: The internal Mockito frame should be removed, while preserving the user code frames.
        assertThat(filteredStackTrace)
            .extracting(StackTraceElement::getClassName)
            .as("The filtered stack trace should only contain user code classes in the correct order")
            .containsExactly(userTestClass, userCodeClass);
    }
}