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

public class Validate_ESTestTest8 extends Validate_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Object[] objectArray0 = new Object[1];
        objectArray0[0] = (Object) "jzb<%EEu_";
        // Undeclared exception!
        try {
            Validate.fail("jzb<%EEu_", objectArray0);
            fail("Expecting exception: IllegalFormatConversionException");
        } catch (IllegalFormatConversionException e) {
            //
            // e != java.lang.String
            //
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }
}
