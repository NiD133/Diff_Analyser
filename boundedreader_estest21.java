package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BoundedReader_ESTestTest21 extends BoundedReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        StringReader stringReader0 = new StringReader("wa");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 10);
        boundedReader0.mark(1);
        long long0 = boundedReader0.skip(10);
        assertEquals(1L, long0);
    }
}
