package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest23 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse("wj[b]_o4~M");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query 'wj[b]_o4~M': unexpected token at '_o4~M'
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
