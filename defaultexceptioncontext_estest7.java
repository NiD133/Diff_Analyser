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

public class DefaultExceptionContext_ESTestTest7 extends DefaultExceptionContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test6() throws Throwable {
        DefaultExceptionContext defaultExceptionContext0 = new DefaultExceptionContext();
        List<Pair<String, Object>> list0 = defaultExceptionContext0.getContextEntries();
        defaultExceptionContext0.setContextValue(",yx", list0);
        // Undeclared exception!
        try {
            defaultExceptionContext0.getFormattedExceptionMessage(",yx");
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
