package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IllegalFieldValueException_ESTestTest21 extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        DurationFieldType durationFieldType0 = DurationFieldType.eras();
        IllegalFieldValueException illegalFieldValueException0 = new IllegalFieldValueException(durationFieldType0, "");
        illegalFieldValueException0.getDurationFieldType();
        assertEquals("Value \"\" for eras is not supported", illegalFieldValueException0.getMessage());
    }
}
