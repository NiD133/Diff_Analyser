package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest3 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("(2IQL&'x,2.f\"fX");
        // Undeclared exception!
        try {
            tokenQueue0.chompBalanced('(', 'u');
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Did not find balanced marker at '2IQL&'x,2.f\"fX'
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
