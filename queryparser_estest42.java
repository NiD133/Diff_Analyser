package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest42 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse(":nth-last-child(0) ~ ");
            //  fail("Expecting exception: IllegalStateException");
            // Unstable assertion
        } catch (IllegalStateException e) {
            //
            // No match found
            //
            verifyException("java.util.regex.Matcher", e);
        }
    }
}
