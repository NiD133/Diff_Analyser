package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest29 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("v\u0000{Z^PZ");
        tokenQueue0.close();
        // Undeclared exception!
        try {
            tokenQueue0.matchChomp("v\u0000{Z^PZ");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.parser.CharacterReader", e);
        }
    }
}
