package org.apache.commons.lang3.stream;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import java.util.function.Function;
import java.util.stream.Collector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LangCollectors_ESTestTest1 extends LangCollectors_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        char[] charArray0 = new char[5];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        Collector<Object, ?, String> collector0 = LangCollectors.joining((CharSequence) null, (CharSequence) null, (CharSequence) charBuffer0);
        assertNotNull(collector0);
    }
}
