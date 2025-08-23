package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest41 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("U\fXOZ8Ez~:Kb");
        tokenQueue0.close();
        // Undeclared exception!
        try {
            tokenQueue0.chompBalanced('v', 'v');
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
