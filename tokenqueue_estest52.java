package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest52 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("             ");
        boolean boolean0 = tokenQueue0.consumeWhitespace();
        assertTrue(boolean0);
    }
}
