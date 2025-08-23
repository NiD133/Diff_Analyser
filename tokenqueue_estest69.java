package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest69 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test68() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("|;tj;PF%cdg`,");
        tokenQueue0.consume();
        tokenQueue0.consume();
        tokenQueue0.advance();
        tokenQueue0.advance();
        tokenQueue0.advance();
        char char0 = tokenQueue0.consume();
        assertEquals('P', char0);
        String[] stringArray0 = new String[4];
        stringArray0[0] = "F";
        String string0 = tokenQueue0.consumeToAny(stringArray0);
        assertEquals("", string0);
    }
}
