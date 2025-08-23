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

public class ISO8601Utils_ESTestTest22 extends ISO8601Utils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        MockDate mockDate0 = new MockDate(1194, 1194, 2550);
        SimpleTimeZone simpleTimeZone0 = new SimpleTimeZone(2550, "!#[5.iqQz1B2Sn'");
        simpleTimeZone0.setRawOffset((-141));
        String string0 = ISO8601Utils.format((Date) mockDate0, true, (TimeZone) simpleTimeZone0);
        assertEquals("3200-06-22T23:59:59.859-00:00", string0);
    }
}
