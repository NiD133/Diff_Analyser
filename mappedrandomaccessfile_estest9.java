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

public class MappedRandomAccessFile_ESTestTest9 extends MappedRandomAccessFile_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        FileSystemHandling.shouldAllThrowIOExceptions();
        MappedRandomAccessFile mappedRandomAccessFile0 = new MappedRandomAccessFile("rw", "rw");
        try {
            mappedRandomAccessFile0.close();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Simulated IOException
            //
            verifyException("org.evosuite.runtime.vfs.VirtualFileSystem", e);
        }
    }
}
