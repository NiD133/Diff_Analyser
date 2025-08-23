package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest66 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test65() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("$dXr'&T^\"0qAav");
        // Undeclared exception!
        try {
            tokenQueue0.chompBalanced('$', '$');
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Did not find balanced marker at 'dXr'&T^\"0qAav'
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
