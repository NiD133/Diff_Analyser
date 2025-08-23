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

public class LangCollectors_ESTestTest2 extends LangCollectors_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        char[] charArray0 = new char[1];
        CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
        Collector<Object, ?, String> collector0 = LangCollectors.joining((CharSequence) charBuffer0, (CharSequence) charBuffer0, (CharSequence) charBuffer0, (Function<Object, String>) null);
        assertNotNull(collector0);
    }
}
