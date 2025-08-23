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

public class Validate_ESTestTest6 extends Validate_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Object[] objectArray0 = new Object[5];
        // Undeclared exception!
        try {
            Validate.fail("wz%c^4e%9no&N8", objectArray0);
            fail("Expecting exception: IllegalFormatWidthException");
        } catch (IllegalFormatWidthException e) {
            //
            // 9
            //
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }
}
