package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LineIterator_ESTestTest3 extends LineIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        StringReader stringReader0 = new StringReader("w*N4EtL4abL*9i`");
        LineIterator lineIterator0 = new LineIterator(stringReader0);
        boolean boolean0 = lineIterator0.isValidLine("w*N4EtL4abL*9i`");
        assertTrue(boolean0);
    }
}
