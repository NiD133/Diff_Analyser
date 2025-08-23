package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest24 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse("[^-a-zA-Z0-9_:.]+");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query '[^-a-zA-Z0-9_:.]+': unexpected token at ''
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
