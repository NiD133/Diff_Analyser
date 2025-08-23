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

public class SequenceReader_ESTestTest6 extends SequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Reader[] readerArray0 = new Reader[1];
        PipedReader pipedReader0 = new PipedReader();
        readerArray0[0] = (Reader) pipedReader0;
        SequenceReader sequenceReader0 = new SequenceReader(readerArray0);
        try {
            sequenceReader0.read();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Pipe not connected
            //
            verifyException("java.io.PipedReader", e);
        }
    }
}
