package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest24 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 0.0F);
        Object object0 = new Object();
        boolean boolean0 = vector0.equals(object0);
        assertFalse(boolean0);
    }
}
