package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest33 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder((CharSequence) "', is neither of type Map.Entry nor an Array");
        CharSequence charSequence0 = CharSequenceUtils.subSequence(stringBuilder0, 0);
        assertNotNull(charSequence0);
        assertEquals("', is neither of type Map.Entry nor an Array", charSequence0);
    }
}
