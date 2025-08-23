package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest26 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse("1+RZ-2|k}9kC<q");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query '1+RZ-2|k}9kC<q': unexpected token at '}9kC<q'
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
