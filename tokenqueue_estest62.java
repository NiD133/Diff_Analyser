package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest62 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test61() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("\"@=e|.=e88-P~");
        // Undeclared exception!
        try {
            tokenQueue0.chompBalanced('\"', '\"');
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Did not find balanced marker at '@=e|.=e88-P~'
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
