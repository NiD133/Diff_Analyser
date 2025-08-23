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

public class DefaultExceptionContext_ESTestTest4 extends DefaultExceptionContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test3() throws Throwable {
        DefaultExceptionContext defaultExceptionContext0 = new DefaultExceptionContext();
        DefaultExceptionContext defaultExceptionContext1 = defaultExceptionContext0.setContextValue((String) null, (Object) null);
        String string0 = defaultExceptionContext1.getFormattedExceptionMessage((String) null);
        assertEquals("Exception Context:\n\t[1:null=null]\n---------------------------------", string0);
    }
}
