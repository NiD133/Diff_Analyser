package org.mockito.internal.exceptions.stacktrace;

import static org.junit.Assert.assertEquals;
import static org.mockitoutil.Conditions.onlyThoseClasses;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

public class StackTraceFilterTestTest3 extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldFilterOutMockitoPackage() {
        StackTraceElement[] t = new TraceBuilder().classes("org.test.MockitoSampleTest", "org.mockito.Mockito").toTraceArray();
        StackTraceElement[] filtered = filter.filter(t, false);
        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.MockitoSampleTest"));
    }
}
