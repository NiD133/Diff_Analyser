package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest20 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("f7(L)+X");
        String string0 = tokenQueue0.chompBalanced('P', 'u');
        assertEquals("f", string0);
        char char0 = tokenQueue0.consume();
        assertEquals('7', char0);
    }
}
