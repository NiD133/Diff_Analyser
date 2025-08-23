package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Lister_ESTestTest10 extends Lister_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Lister lister0 = new Lister();
        // Undeclared exception!
        try {
            lister0.go();
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            //
            // org/apache/commons/compress/utils/IOUtils
            //
            verifyException("org.apache.commons.compress.archivers.ArchiveStreamFactory", e);
        }
    }
}
