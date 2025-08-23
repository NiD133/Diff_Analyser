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

public class MappedRandomAccessFile_ESTestTest19 extends MappedRandomAccessFile_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        MappedRandomAccessFile mappedRandomAccessFile0 = null;
        try {
            mappedRandomAccessFile0 = new MappedRandomAccessFile("rw", "PmF8^@U[W<vM7");
            fail("Expecting exception: FileNotFoundException");
        } catch (Throwable e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.evosuite.runtime.mock.java.io.MockFileInputStream", e);
        }
    }
}
