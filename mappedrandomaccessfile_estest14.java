package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

public class MappedRandomAccessFile_ESTestTest14 extends MappedRandomAccessFile_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        MappedRandomAccessFile mappedRandomAccessFile0 = new MappedRandomAccessFile("rw", "rw");
        byte[] byteArray0 = new byte[9];
        int int0 = mappedRandomAccessFile0.read(byteArray0, (int) (byte) 0, 105);
        assertEquals((-1), int0);
        assertEquals(0L, mappedRandomAccessFile0.getFilePointer());
    }
}
