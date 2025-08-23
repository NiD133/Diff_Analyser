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

public class Lister_ESTestTest5 extends Lister_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        String[] stringArray0 = new String[3];
        Lister lister0 = null;
        try {
            lister0 = new Lister(false, stringArray0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // args[0]
            //
            verifyException("java.util.Objects", e);
        }
    }
}
