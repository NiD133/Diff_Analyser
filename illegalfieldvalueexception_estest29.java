package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest29 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.year();
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException(dateTimeFieldType0, (Number) null, (Number) null, (Number) null, "org.joda.time.DateTimeZone$LazyInit");
    }
}
