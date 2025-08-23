package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest32 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        CharSequence charSequence0 = CharSequenceUtils.subSequence((CharSequence) null, 1114110);
        assertNull(charSequence0);
    }
}
