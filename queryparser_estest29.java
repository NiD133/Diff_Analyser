package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest29 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse("9F>R8Qm5cTG:&@z");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query '9F>R8Qm5cTG:&@z': unexpected token at '&@z'
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
