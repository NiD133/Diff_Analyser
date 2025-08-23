package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest10 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        MockThrowable mockThrowable0 = new MockThrowable();
        StackTraceElement stackTraceElement0 = new StackTraceElement("/34KVo", "org.mockito.internal.configuration.plugins.DefaultPluginSwitch", "/34KVo", "org.mockito.internal.configuration.plugins.DefaultPluginSwitch", "/34KVo", "org.mockito.internal.configuration.plugins.DefaultPluginSwitch", 1);
        mockThrowable0.setOriginForDelegate(stackTraceElement0);
        StackTraceElement stackTraceElement1 = stackTraceFilter0.filterFirst(mockThrowable0, true);
        assertEquals("<evosuite>", stackTraceElement1.getFileName());
    }
}
