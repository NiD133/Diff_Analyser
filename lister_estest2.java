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

public class Lister_ESTestTest2 extends Lister_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        String[] stringArray0 = new String[1];
        stringArray0[0] = "ustar\u0000";
        Lister lister0 = new Lister(true, stringArray0);
        // Undeclared exception!
        try {
            lister0.go();
            fail("Expecting exception: InvalidPathException");
        } catch (InvalidPathException e) {
        }
    }
}
