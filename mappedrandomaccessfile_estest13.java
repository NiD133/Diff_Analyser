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

public class MappedRandomAccessFile_ESTestTest13 extends MappedRandomAccessFile_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        MappedRandomAccessFile mappedRandomAccessFile0 = new MappedRandomAccessFile("rw", "rw");
        mappedRandomAccessFile0.seek((-1934028347));
        byte[] byteArray0 = new byte[0];
        // Undeclared exception!
        try {
            mappedRandomAccessFile0.read(byteArray0, (-1934028347), 841);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -1
            //
            verifyException("com.itextpdf.text.pdf.MappedRandomAccessFile", e);
        }
    }
}
