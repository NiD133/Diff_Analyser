package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest47 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("\u0005");
        String string0 = tokenQueue0.chompBalanced('J', 'x');
        assertEquals("\u0005", string0);
        String string1 = tokenQueue0.consumeElementSelector();
        assertFalse(string1.equals((Object) string0));
    }
}
