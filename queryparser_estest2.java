package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest2 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse("RV,LfO:4},X");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query 'RV,LfO:4},X': unexpected token at 'fO:4},X'
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
