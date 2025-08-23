package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest20 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.clockhourOfDay();
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException(dateTimeFieldType0, (Number) null, ".#Q: =KY]1ld]Nf>u");
    }
}