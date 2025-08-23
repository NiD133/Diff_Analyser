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

public class Lister_ESTestTest11 extends Lister_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        String[] stringArray0 = new String[2];
        stringArray0[0] = "";
        stringArray0[1] = "tar";
        Lister lister0 = new Lister(false, stringArray0);
        // Undeclared exception!
        try {
            lister0.go();
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            //
            // Could not initialize class org.apache.commons.compress.archivers.zip.ZipEncodingHelper
            //
            verifyException("org.apache.commons.compress.archivers.tar.TarFile", e);
        }
    }
}
