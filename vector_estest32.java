package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest32 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        Vector vector0 = new Vector((-2905.637F), (-2905.637F), (-1.0F));
        String string0 = vector0.toString();
        assertEquals("-2905.637,-2905.637,-1.0", string0);
    }
}
