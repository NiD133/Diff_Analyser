package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest61 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test60() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue(" 5mL\"2HD\"v");
        // Undeclared exception!
        try {
            tokenQueue0.chompBalanced(' ', ' ');
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Did not find balanced marker at '5mL\"2HD\"v'
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
