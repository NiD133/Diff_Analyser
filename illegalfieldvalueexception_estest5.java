package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest5 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.year();
        Float float0 = new Float(1.0F);
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException(dateTimeFieldType0, float0, float0, float0);
        illegalFieldValueException0.getFieldName();
        assertEquals("Value 1.0 for year must be in the range [1.0,1.0]", illegalFieldValueException0.getMessage());
    }
}