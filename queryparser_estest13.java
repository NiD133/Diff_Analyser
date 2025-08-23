package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest13 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse(":matches?holeText(1s)");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Dangling meta character '?' near index 0
            // ?
            // ^
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
