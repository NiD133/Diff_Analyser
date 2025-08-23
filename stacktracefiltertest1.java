package org.mockito.internal.exceptions.stacktrace;

import static org.junit.Assert.assertEquals;
import static org.mockitoutil.Conditions.onlyThoseClasses;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

public class StackTraceFilterTestTest1 extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldFilterOutCglibGarbage() {
        StackTraceElement[] t = new TraceBuilder().classes("MockitoExampleTest", "List$$EnhancerByMockitoWithCGLIB$$2c406024").toTraceArray();
        StackTraceElement[] filtered = filter.filter(t, false);
        Assertions.assertThat(filtered).has(onlyThoseClasses("MockitoExampleTest"));
    }
}
