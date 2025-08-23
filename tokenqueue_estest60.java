package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest60 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test59() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("{#7l{C/gwF;D^Xtn&");
        // Undeclared exception!
        try {
            tokenQueue0.chompBalanced('{', ';');
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Did not find balanced marker at '#7l{C/gwF;D^Xtn&'
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
