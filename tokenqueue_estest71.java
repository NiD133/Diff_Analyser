package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest71 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test70() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("k\"YT-6Ih:G~3zAw");
        // Undeclared exception!
        try {
            tokenQueue0.consume("Must Lbe false");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Queue did not match expected sequence
            //
            verifyException("org.jsoup.parser.TokenQueue", e);
        }
    }
}
