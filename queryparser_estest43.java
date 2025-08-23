package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest43 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse(":nth-child(%d)");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse nth-index '%d': unexpected format
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
