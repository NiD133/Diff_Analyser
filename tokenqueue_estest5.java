package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest5 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        String string0 = TokenQueue.unescape("");
        assertEquals("", string0);
    }
}
