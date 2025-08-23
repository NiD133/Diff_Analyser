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

public class DefaultExceptionContext_ESTestTest8 extends DefaultExceptionContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test7() throws Throwable {
        DefaultExceptionContext defaultExceptionContext0 = new DefaultExceptionContext();
        List<Object> list0 = defaultExceptionContext0.getContextValues("tnJ_{Nixtyk");
        assertFalse(list0.contains("tnJ_{Nixtyk"));
    }
}
