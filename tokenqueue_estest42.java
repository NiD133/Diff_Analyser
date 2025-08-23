package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest42 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        TokenQueue tokenQueue0 = null;
        try {
            tokenQueue0 = new TokenQueue((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.io.StringReader", e);
        }
    }
}
