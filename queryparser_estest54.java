package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest54 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test53() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse(":has() must have a selector");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query ':has() must have a selector': unexpected token at ') must have a selector'
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
