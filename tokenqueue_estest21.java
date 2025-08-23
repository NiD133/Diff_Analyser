package org.jsoup.parser;

import org.junit.Test;
import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;

// The test class structure and runner are preserved from the original generated code.
@RunWith(EvoRunner.class)
public class TokenQueue_ESTestTest21 extends TokenQueue_ESTest_scaffolding {

    /**
     * Verifies that the static unescape method throws a NullPointerException
     * when called with a null input string.
     */
    @Test(expected = NullPointerException.class)
    public void unescapeShouldThrowNullPointerExceptionWhenInputIsNull() {
        TokenQueue.unescape(null);
    }
}