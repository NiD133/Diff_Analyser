package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest50 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("v\u0000{Z|NV");
        String string0 = tokenQueue0.consumeCssIdentifier();
        assertEquals("v\uFFFD", string0);
    }
}
