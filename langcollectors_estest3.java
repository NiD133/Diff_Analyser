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

public class LangCollectors_ESTestTest3 extends LangCollectors_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test2() throws Throwable {
        Collector<Object, ?, String> collector0 = LangCollectors.joining();
        assertNotNull(collector0);
    }
}
