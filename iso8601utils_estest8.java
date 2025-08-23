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

public class ISO8601Utils_ESTestTest8 extends ISO8601Utils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        MockDate mockDate0 = new MockDate(0L);
        TimeZone timeZone0 = TimeZone.getTimeZone("MU/^fUMQ");
        String string0 = ISO8601Utils.format((Date) mockDate0, false, timeZone0);
        assertEquals("1970-01-01T00:00:00Z", string0);
    }
}