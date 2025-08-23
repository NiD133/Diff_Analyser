package org.mockito.internal.exceptions.stacktrace;

import static org.junit.Assert.assertEquals;
import static org.mockitoutil.Conditions.onlyThoseClasses;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

public class StackTraceFilterTestTest9 extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldKeepGoodTraceFromTheTopBecauseRealImplementationsOfSpiesSometimesThrowExceptions() {
        StackTraceElement[] t = new TraceBuilder().classes("org.good.Trace", "org.yet.another.good.Trace", "org.mockito.internal.to.be.Filtered", "org.test.MockitoSampleTest").toTraceArray();
        StackTraceElement[] filtered = filter.filter(t, true);
        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.MockitoSampleTest", "org.yet.another.good.Trace", "org.good.Trace"));
    }
}
