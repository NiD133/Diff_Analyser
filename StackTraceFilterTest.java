/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockitoutil.Conditions.onlyThoseClasses;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

/**
 * Tests for {@link StackTraceFilter}.
 * <p>
 * The filter is responsible for cleaning up stack traces by removing irrelevant frames
 * from Mockito, CGLIB, and other libraries to make them more readable for users.
 */
public class StackTraceFilterTest extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    // --- Constants for representative class names ---
    private static final String USER_CODE = "org.my.app.MyTest";
    private static final String USER_CODE_2 = "org.my.app.TestSupport";
    private static final String JUNIT_RUNNER = "org.junit.runner.Runner";
    private static final String MOCKITO_RUNNER = "org.mockito.runners.Runner";
    private static final String MOCKITO_INTERNAL_RUNNER = "org.mockito.internal.runners.Runner";
    private static final String MOCKITO_RULE = "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)";

    private static final String MOCKITO_INTERNAL = "org.mockito.internal.MockitoCore";
    private static final String CGLIB_PROXY = "org.my.app.Book$$EnhancerByMockitoWithCGLIB$$12345";
    private static final String BYTE_BUDDY_PROXY = "org.my.app.User$MockitoMock$1882975947.doSomething(Unknown Source)";

    // region Basic Filtering (keepTop = false)

    @Test
    public void filter_shouldRemoveCglibGeneratedClassNames() {
        // given
        StackTraceElement[] stackTrace = new TraceBuilder().classes(USER_CODE, CGLIB_PROXY).toTraceArray();

        // when
        StackTraceElement[] filtered = filter.filter(stackTrace, false);

        // then
        assertThat(filtered).has(onlyThoseClasses(USER_CODE));
    }

    @Test
    public void filter_shouldRemoveByteBuddyGeneratedClassNames() {
        // given
        StackTraceElement[] stackTrace = new TraceBuilder().classes(USER_CODE, BYTE_BUDDY_PROXY).toTraceArray();

        // when
        StackTraceElement[] filtered = filter.filter(stackTrace, false);

        // then
        assertThat(filtered).has(onlyThoseClasses(USER_CODE));
    }

    @Test
    public void filter_shouldRemoveMockitoInternalClassNames() {
        // given
        StackTraceElement[] stackTrace = new TraceBuilder().classes(USER_CODE, MOCKITO_INTERNAL).toTraceArray();

        // when
        StackTraceElement[] filtered = filter.filter(stackTrace, false);

        // then
        assertThat(filtered).has(onlyThoseClasses(USER_CODE));
    }

    @Test
    public void filter_shouldPreserveUserCodeBetweenFilteredClassNames() {
        // given
        StackTraceElement[] stackTrace =
                new TraceBuilder()
                        .classes(USER_CODE, USER_CODE_2, MOCKITO_INTERNAL, USER_CODE_2, MOCKITO_INTERNAL)
                        .toTraceArray();

        // when
        StackTraceElement[] filtered = filter.filter(stackTrace, false);

        // then
        assertThat(filtered).has(onlyThoseClasses(USER_CODE, USER_CODE_2, USER_CODE_2));
    }

    @Test
    public void filter_shouldPreserveRunnerClassNames() {
        // given
        StackTraceElement[] stackTrace =
                new TraceBuilder().classes(MOCKITO_RUNNER, JUNIT_RUNNER, USER_CODE, MOCKITO_INTERNAL).toTraceArray();

        // when
        StackTraceElement[] filtered = filter.filter(stackTrace, false);

        // then
        assertThat(filtered).has(onlyThoseClasses(MOCKITO_RUNNER, JUNIT_RUNNER, USER_CODE));
    }

    @Test
    public void filter_shouldPreserveInternalRunnerClassNames() {
        // given
        StackTraceElement[] stackTrace =
                new TraceBuilder().classes(MOCKITO_INTERNAL_RUNNER, USER_CODE).toTraceArray();

        // when
        StackTraceElement[] filtered = filter.filter(stackTrace, false);

        // then
        assertThat(filtered).has(onlyThoseClasses(MOCKITO_INTERNAL_RUNNER, USER_CODE));
    }

    @Test
    public void filter_shouldPreserveFramesAboveMockitoRule() {
        // given
        StackTraceElement[] stackTrace =
                new TraceBuilder()
                        .classes(
                                MOCKITO_RULE,
                                MOCKITO_RUNNER,
                                JUNIT_RUNNER,
                                USER_CODE,
                                MOCKITO_INTERNAL,
                                "org.mockito.internal.debugging.LocationImpl")
                        .toTraceArray();

        // when
        StackTraceElement[] filtered = filter.filter(stackTrace, false);

        // then
        assertThat(filtered).has(onlyThoseClasses(MOCKITO_RULE, MOCKITO_RUNNER, JUNIT_RUNNER, USER_CODE));
    }

    // endregion

    // region Advanced Filtering (keepTop = true)

    @Test
    public void filter_withKeepTop_preservesUserCodeAtTopOfStack() {
        // given
        // The 'keepTop' flag is for scenarios like exceptions in spies. It preserves all frames
        // from the top until the first frame that needs to be filtered.
        final String userCodeAtTop = "org.my.app.Good";
        final String internalCodeInMiddle = "org.mockito.internal.Bad";
        StackTraceElement[] stackTrace =
                new TraceBuilder().classes(userCodeAtTop, internalCodeInMiddle, USER_CODE).toTraceArray();

        // when
        StackTraceElement[] filtered = filter.filter(stackTrace, true);

        // then
        // The internal code is removed, but the user code above it is preserved.
        assertThat(filtered).has(onlyThoseClasses(userCodeAtTop, USER_CODE));
    }

    @Test
    public void filter_withKeepTop_preservesUserCodeFromSpyInvocations() {
        // given
        // This simulates a spy calling real code, which then throws an exception.
        // The top of the stack trace is user code that should be preserved.
        final String userCodeSpyImpl = "org.my.app.SpyImpl";
        final String anotherUserCode = "org.my.app.AnotherClass";
        final String internalCode = "org.mockito.internal.to.be.Filtered";
        StackTraceElement[] stackTrace =
                new TraceBuilder()
                        .classes(userCodeSpyImpl, anotherUserCode, internalCode, USER_CODE)
                        .toTraceArray();

        // when
        StackTraceElement[] filtered = filter.filter(stackTrace, true);

        // then
        // The contiguous block of user code at the top is preserved.
        assertThat(filtered).has(onlyThoseClasses(userCodeSpyImpl, anotherUserCode, USER_CODE));
    }

    // endregion

    // region Edge Cases

    @Test
    public void filter_shouldReturnEmptyArrayForEmptyInput() {
        // given
        StackTraceElement[] emptyStackTrace = new StackTraceElement[0];

        // when
        StackTraceElement[] filtered = filter.filter(emptyStackTrace, false);

        // then
        assertThat(filtered).isEmpty();
    }

    // endregion
}