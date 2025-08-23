package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest4 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("oUY}ddkFQ:tJ.q");
        String string0 = tokenQueue0.consumeCssIdentifier();
        assertEquals("oUY", string0);
    }
}
