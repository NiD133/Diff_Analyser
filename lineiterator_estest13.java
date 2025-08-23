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

public class LineIterator_ESTestTest13 extends LineIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        StringReader stringReader0 = new StringReader("\n");
        BufferedReader bufferedReader0 = new BufferedReader(stringReader0, 1048);
        LineIterator lineIterator0 = new LineIterator(bufferedReader0);
        lineIterator0.hasNext();
        String string0 = lineIterator0.nextLine();
        assertEquals("", string0);
    }
}
