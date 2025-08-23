package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest9 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse("37m~T/SMo)=E[BHj8|{");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query '37m~T/SMo)=E[BHj8|{': unexpected token at '/SMo)=E[BHj8|{'
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
