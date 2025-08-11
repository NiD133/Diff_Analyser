package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;

import static org.junit.Assert.*;

public class StackTraceFilterTest {

    private final StackTraceFilter filter = new StackTraceFilter();

    // Helpers to make test intent explicit
    private static StackTraceElement appFrame(String className, String method, String file, int line) {
        return new StackTraceElement(className, method, file, line);
    }

    private static StackTraceElement internalMockitoFrame() {
        return new StackTraceElement(
                "org.mockito.internal.util.SomeInternal", "invoke", "SomeInternal.java", 10
        );
    }

    @Test
    public void findSourceFile_returnsDefault_whenNoFrames() {
        // given
        StackTraceElement[] frames = new StackTraceElement[0];

        // when
        String source = filter.findSourceFile(frames, "Unknown.java");

        // then
        assertEquals("Unknown.java", source);
    }

    @Test
    public void filterFirst_returnsNull_whenThrowableHasNoFrames() {
        // given
        Throwable t = new Throwable();
        t.setStackTrace(new StackTraceElement[0]);

        // when
        StackTraceElement first = filter.filterFirst(t, false);

        // then
        assertNull(first);
    }

    @Test
    public void filter_keepsApplicationFrames_andPreservesOrder() {
        // given
        StackTraceElement a = appFrame("com.example.A", "a", "A.java", 1);
        StackTraceElement b = appFrame("com.example.B", "b", "B.java", 2);
        StackTraceElement[] frames = new StackTraceElement[] { a, b };

        // when
        StackTraceElement[] filtered = filter.filter(frames, false);

        // then
        assertArrayEquals(new StackTraceElement[] { a, b }, filtered);
    }

    @Test
    public void filter_dropsMockitoInternalFrames_leavesAppFrames() {
        // given: internal + app frames mixed
        StackTraceElement internal = internalMockitoFrame();
        StackTraceElement app = appFrame("com.example.App", "run", "App.java", 42);
        StackTraceElement[] frames = new StackTraceElement[] { internal, app, internal };

        // when
        StackTraceElement[] filtered = filter.filter(frames, false);

        // then: only the application frame should remain
        assertArrayEquals(new StackTraceElement[] { app }, filtered);
    }

    @Test
    public void filterFirst_skipsInternalFrames_andReturnsFirstAppFrame() {
        // given: throwable stack with internal frames before a real app frame
        Throwable t = new Throwable();
        StackTraceElement app = appFrame("com.example.Service", "process", "Service.java", 7);
        t.setStackTrace(new StackTraceElement[] {
                internalMockitoFrame(),
                internalMockitoFrame(),
                app,
                internalMockitoFrame()
        });

        // when
        StackTraceElement first = filter.filterFirst(t, false);

        // then
        assertNotNull(first);
        assertEquals(app, first);
    }

    @Test
    public void findSourceFile_returnsFilenameOfFirstAppFrame() {
        // given
        StackTraceElement app = appFrame("com.example.Controller", "handle", "Controller.java", 15);
        StackTraceElement[] frames = new StackTraceElement[] { app };

        // when
        String source = filter.findSourceFile(frames, "Unknown.java");

        // then
        assertEquals("Controller.java", source);
    }
}