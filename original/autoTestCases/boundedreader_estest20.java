package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        StringReader stringReader0 = new StringReader("pI2");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, (-2049));
        int int0 = boundedReader0.read();
        assertEquals((-1), int0);
    }
}
