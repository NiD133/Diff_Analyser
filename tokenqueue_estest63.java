package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest63 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test62() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("The '%s' parameter must not be empty.");
        // Undeclared exception!
        try {
            tokenQueue0.chompBalanced('T', 'T');
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Did not find balanced marker at 'he '%s' parameter must not be empty.'
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
