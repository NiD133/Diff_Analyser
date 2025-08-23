package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest48 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        String string0 = TokenQueue.escapeCssIdentifier("%WApE_");
        assertEquals("\\%WApE_", string0);
    }
}
