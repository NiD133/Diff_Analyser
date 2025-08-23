package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSequenceUtils_ESTestTest3 extends CharSequenceUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        boolean boolean0 = CharSequenceUtils.regionMatches("', is neither of type Map.Entry nor an Array", false, 117, "", (-1469), (-1231));
        assertFalse(boolean0);
    }
}