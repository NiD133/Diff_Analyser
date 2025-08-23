package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Vector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SequenceReader_ESTestTest4 extends SequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        StringReader stringReader0 = new StringReader("");
        Reader[] readerArray0 = new Reader[2];
        readerArray0[0] = (Reader) stringReader0;
        readerArray0[1] = (Reader) stringReader0;
        SequenceReader sequenceReader0 = new SequenceReader(readerArray0);
        char[] charArray0 = new char[3];
        try {
            sequenceReader0.read(charArray0, 1, 1);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Stream closed
            //
            verifyException("java.io.StringReader", e);
        }
    }
}
