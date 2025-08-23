package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.junit.runner.RunWith;

public class ISO8601Utils_ESTestTest15 extends ISO8601Utils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        ParsePosition parsePosition0 = new ParsePosition((-2016));
        try {
            ISO8601Utils.parse((String) null, parsePosition0);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            //
            // Failed to parse date [null]: (java.lang.NumberFormatException)
            //
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }
}
