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

public class MappedRandomAccessFile_ESTestTest17 extends MappedRandomAccessFile_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        MappedRandomAccessFile mappedRandomAccessFile0 = new MappedRandomAccessFile("rw", "rw");
        mappedRandomAccessFile0.seek((-1934028355));
        // Undeclared exception!
        try {
            mappedRandomAccessFile0.read();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -1
            //
            verifyException("com.itextpdf.text.pdf.MappedRandomAccessFile", e);
        }
    }
}
