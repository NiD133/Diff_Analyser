package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest4 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse("#P>hfIV>e@0fe");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query '#P>hfIV>e@0fe': unexpected token at '@0fe'
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
