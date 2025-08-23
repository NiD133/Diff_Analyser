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

public class ISO8601Utils_ESTestTest17 extends ISO8601Utils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        ParsePosition parsePosition0 = new ParsePosition(0);
        Date date0 = ISO8601Utils.parse("2014-02-14T20:21:22.575+00:00", parsePosition0);
        assertEquals("Fri Feb 14 20:21:21 GMT 2014", date0.toString());
    }
}
