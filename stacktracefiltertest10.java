package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockitoutil.TestBase;

/**
 * Tests for {@link StackTraceFilter}.
 */
public class StackTraceFilterTest extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldReturnEmptyArray_whenFilteringEmptyStackTrace() {
        // Arrange
        StackTraceElement[] emptyStackTrace = new StackTraceElement[0];
        boolean keepTop = false; // This flag has no effect on an empty stack trace

        // Act
        StackTraceElement[] filteredStackTrace = filter.filter(emptyStackTrace, keepTop);

        // Assert
        assertThat(filteredStackTrace).isEmpty();
    }
}