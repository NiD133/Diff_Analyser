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

public class DefaultExceptionContext_ESTestTest10 extends DefaultExceptionContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test9() throws Throwable {
        DefaultExceptionContext defaultExceptionContext0 = new DefaultExceptionContext();
        Object object0 = defaultExceptionContext0.getFirstContextValue("6Yh~Lxgr");
        assertNull(object0);
    }
}
