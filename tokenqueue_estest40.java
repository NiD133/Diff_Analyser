package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest40 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("Om`o!");
        tokenQueue0.close();
        // Undeclared exception!
        try {
            tokenQueue0.consume();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
