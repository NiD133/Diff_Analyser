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

public class Lister_ESTestTest8 extends Lister_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        String[] stringArray0 = new String[8];
        stringArray0[0] = "bf,eR=!9:b8u`J";
        stringArray0[1] = "7z";
        Lister lister0 = new Lister(true, stringArray0);
        // Undeclared exception!
        try {
            lister0.go();
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            //
            // org/apache/commons/compress/archivers/sevenz/SevenZFile
            //
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }
}
