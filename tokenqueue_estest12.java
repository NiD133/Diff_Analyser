package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest12 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("k\"YT-6Ih:G~3zAw");
        String string0 = tokenQueue0.consumeElementSelector();
        assertEquals("k", string0);
        boolean boolean0 = tokenQueue0.matches('\"');
        assertTrue(boolean0);
    }
}
