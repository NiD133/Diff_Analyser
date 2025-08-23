package org.mockito.internal.exceptions.stacktrace;

import static org.junit.Assert.assertEquals;
import static org.mockitoutil.Conditions.onlyThoseClasses;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

public class StackTraceFilterTestTest10 extends TestBase {

    private final StackTraceFilter filter = new StackTraceFilter();

    @Test
    public void shouldReturnEmptyArrayWhenInputIsEmpty() throws Exception {
        // when
        StackTraceElement[] filtered = filter.filter(new StackTraceElement[0], false);
        // then
        assertEquals(0, filtered.length);
    }
}
