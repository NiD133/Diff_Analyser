package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest43 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("k\"YT-6Ih:G~3zAw");
        String string0 = tokenQueue0.consumeElementSelector();
        assertEquals("k", string0);
        String string1 = tokenQueue0.chompBalanced('!', '!');
        assertEquals("\"", string1);
        String string2 = tokenQueue0.consumeCssIdentifier();
        assertEquals("YT", string2);
    }
}
