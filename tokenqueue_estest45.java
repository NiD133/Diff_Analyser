package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest45 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("WC**~WF-]9Y$]");
        String string0 = tokenQueue0.consumeElementSelector();
        assertEquals("WC**", string0);
    }
}
