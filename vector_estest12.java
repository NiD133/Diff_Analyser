package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest12 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Vector vector0 = new Vector(0.0F, 0.0F, 742.77F);
        float float0 = vector0.dot(vector0);
        assertEquals(551707.3F, float0, 0.01F);
    }
}
