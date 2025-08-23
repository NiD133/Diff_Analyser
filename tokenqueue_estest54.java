package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest54 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test53() throws Throwable {
        String string0 = TokenQueue.escapeCssIdentifier("4O*!*d1^m3R|W");
        assertEquals("\\34 O\\*\\!\\*d1\\^m3R\\|W", string0);
    }
}
