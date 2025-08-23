package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest37 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("CdIx'!Y");
        tokenQueue0.close();
        // Undeclared exception!
        try {
            tokenQueue0.consumeElementSelector();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
