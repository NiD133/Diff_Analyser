package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DefaultExceptionContext_ESTestTest6 extends DefaultExceptionContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test5() throws Throwable {
        DefaultExceptionContext defaultExceptionContext0 = new DefaultExceptionContext();
        String string0 = defaultExceptionContext0.getFormattedExceptionMessage((String) null);
        assertEquals("", string0);
    }
}
