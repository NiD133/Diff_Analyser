package org.mockito.internal.exceptions.stacktrace;

import static org.junit.Assert.assertEquals;
import static org.mockitoutil.Conditions.onlyThoseClasses;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

public class StackTraceFilterTestTest6 extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldNotFilterElementsAboveMockitoJUnitRule() {
        StackTraceElement[] t = new TraceBuilder().classes("org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)", "org.mockito.runners.Runner", "junit.stuff", "org.test.MockitoSampleTest", "org.mockito.internal.MockitoCore.verifyNoMoreInteractions", "org.mockito.internal.debugging.LocationImpl").toTraceArray();
        StackTraceElement[] filtered = filter.filter(t, false);
        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.MockitoSampleTest", "junit.stuff", "org.mockito.runners.Runner", "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)"));
    }
}
