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

public class Validate_ESTestTest14 extends Validate_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Object[] objectArray0 = new Object[7];
        objectArray0[0] = (Object) "K)%x7cPKb^^;05";
        // Undeclared exception!
        try {
            Validate.ensureNotNull((Object) null, "K)%x7cPKb^^;05", objectArray0);
            fail("Expecting exception: IllegalFormatConversionException");
        } catch (IllegalFormatConversionException e) {
            //
            // x != java.lang.String
            //
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }
}
