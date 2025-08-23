package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest1 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Vector vector0 = new Vector((-528.75F), (-528.75F), 1.0F);
        float float0 = vector0.dot(vector0);
        assertEquals(559154.1F, float0, 0.01F);
    }
}
