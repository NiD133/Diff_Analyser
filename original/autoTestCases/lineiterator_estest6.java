package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        StringReader stringReader0 = new StringReader("DwHpo._Y&0~e=On_{");
        LineIterator lineIterator0 = new LineIterator(stringReader0);
        LineIterator lineIterator1 = new LineIterator(stringReader0);
        LineIterator.closeQuietly(lineIterator1);
        // Undeclared exception!
        try {
            lineIterator0.hasNext();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // java.io.IOException: Stream closed
            //
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }
}
