package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest28 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse("x:jT)sndzU!E|jR3");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query 'x:jT)sndzU!E|jR3': unexpected token at ')sndzU!E|jR3'
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
