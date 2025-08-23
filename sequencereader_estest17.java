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

public class SequenceReader_ESTestTest17 extends SequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        ArrayDeque<StringReader> arrayDeque0 = new ArrayDeque<StringReader>();
        StringReader stringReader0 = new StringReader("T37");
        arrayDeque0.add(stringReader0);
        arrayDeque0.add(stringReader0);
        SequenceReader sequenceReader0 = new SequenceReader(arrayDeque0);
        sequenceReader0.close();
    }
}
