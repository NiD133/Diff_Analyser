package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest77 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test76() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("%>-8wJ/e4'{T9H");
        String string0 = tokenQueue0.consumeTo("%>-8wJ/e4'{T9H");
        assertEquals("", string0);
    }
}
