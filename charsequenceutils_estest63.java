package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest63 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test62() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder();
        CharBuffer charBuffer0 = CharBuffer.allocate(65536);
        stringBuilder0.insert(0, (CharSequence) charBuffer0);
        // Undeclared exception!
        CharSequenceUtils.indexOf(stringBuilder0, (-1790), 3507);
    }
}
