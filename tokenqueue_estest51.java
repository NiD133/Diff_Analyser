package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest51 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test50() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("\u0005");
        tokenQueue0.chompBalanced('J', 'x');
        // Undeclared exception!
        try {
            tokenQueue0.consumeCssIdentifier();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // CSS identifier expected, but end of input found
            //
            verifyException("org.jsoup.parser.TokenQueue", e);
        }
    }
}
