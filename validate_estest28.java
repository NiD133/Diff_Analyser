package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.FormatFlagsConversionMismatchException;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatFlagsException;
import java.util.IllegalFormatWidthException;
import java.util.MissingFormatArgumentException;
import java.util.MissingFormatWidthException;
import java.util.UnknownFormatConversionException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Validate_ESTestTest28 extends Validate_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Object[] objectArray0 = new Object[7];
        objectArray0[0] = (Object) "";
        // Undeclared exception!
        try {
            Validate.noNullElements(objectArray0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Array must not contain any null objects
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
