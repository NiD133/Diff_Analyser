package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest3 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse(":is(:eq(1114)) ~ ");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query ':is(:eq(1114)) ~': unexpected token at ''
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
