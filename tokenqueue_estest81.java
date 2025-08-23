package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest81 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test80() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue(">'9sOEbGPT9N3{HK5");
        String string0 = tokenQueue0.toString();
        assertEquals(">'9sOEbGPT9N3{HK5", string0);
    }
}
