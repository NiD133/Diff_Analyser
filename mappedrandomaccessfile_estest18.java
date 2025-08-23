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

public class MappedRandomAccessFile_ESTestTest18 extends MappedRandomAccessFile_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        EvoSuiteFile evoSuiteFile0 = new EvoSuiteFile("qw");
        FileSystemHandling.appendStringToFile(evoSuiteFile0, "qw");
        MappedRandomAccessFile mappedRandomAccessFile0 = null;
        try {
            mappedRandomAccessFile0 = new MappedRandomAccessFile("qw", "qw");
            fail("Expecting exception: IOException");
        } catch (Throwable e) {
            //
            // MappedByteBuffer mocks are not supported yet
            //
            verifyException("org.evosuite.runtime.mock.java.io.EvoFileChannel", e);
        }
    }
}
