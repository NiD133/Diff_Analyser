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

public class DefaultExceptionContext_ESTestTest9 extends DefaultExceptionContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test8() throws Throwable {
        DefaultExceptionContext defaultExceptionContext0 = new DefaultExceptionContext();
        Set<String> set0 = defaultExceptionContext0.getContextLabels();
        defaultExceptionContext0.addContextValue("=", set0);
        List<Object> list0 = defaultExceptionContext0.getContextValues("=");
        assertEquals(1, list0.size());
    }
}
