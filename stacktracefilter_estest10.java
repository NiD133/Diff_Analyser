package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link StackTraceFilter}.
 */
public class StackTraceFilterTest {

    /**
     * This test verifies that the filterFirst() method correctly skips over
     * Mockito-internal stack frames and returns the first frame that belongs
     * to user code.
     */
    @Test
    public void filterFirst_shouldSkipInternalMockitoFrames_andReturnFirstUserFrame() {
        // Arrange
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        Throwable throwable = new Throwable();

        // A stack trace element from a Mockito-internal class that should be filtered out.
        StackTraceElement mockitoInternalFrame = new StackTraceElement(
                "org.mockito.internal.runners.Runner", "run", "Runner.java", 50);

        // A stack trace element from typical user test code that should be preserved.
        StackTraceElement userCodeFrame = new StackTraceElement(
                "com.mycompany.MyTest", "myFailingTest", "MyTest.java", 23);

        // A subsequent frame that should be ignored since we only want the first user frame.
        StackTraceElement anotherUserFrame = new StackTraceElement(
                "com.mycompany.MyService", "doSomething", "MyService.java", 101);

        throwable.setStackTrace(new StackTraceElement[]{
                mockitoInternalFrame,
                userCodeFrame,
                anotherUserFrame
        });

        // Act: Filter the stack trace to find the first relevant frame.
        StackTraceElement firstRelevantFrame = stackTraceFilter.filterFirst(throwable, true);

        // Assert: The returned frame should be the user's code, not the internal Mockito frame.
        assertSame("The first user code frame should be returned", userCodeFrame, firstRelevantFrame);
    }
}