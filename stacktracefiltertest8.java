package org.mockito.internal.exceptions.stacktrace;

import static org.junit.Assert.assertEquals;
import static org.mockitoutil.Conditions.onlyThoseClasses;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

public class StackTraceFilterTestTest8 extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldStartFilteringAndKeepTop() {
        // given
        StackTraceElement[] t = new TraceBuilder().classes("org.test.Good", "org.mockito.internal.Bad", "org.test.MockitoSampleTest").toTraceArray();
        // when
        StackTraceElement[] filtered = filter.filter(t, true);
        // then
        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.MockitoSampleTest", "org.test.Good"));
    }
}
