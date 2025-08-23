package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest64 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test63() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("?&KL?gZd\"8't");
        // Undeclared exception!
        try {
            tokenQueue0.chompBalanced('?', '?');
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Did not find balanced marker at '&KL?gZd\"8't'
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
