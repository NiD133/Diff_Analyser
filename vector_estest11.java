package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest11 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Vector vector0 = new Vector(1864.694F, (-105.0F), (-1351.098F));
        float float0 = vector0.get(1);
        assertEquals(5313574.5F, vector0.lengthSquared(), 0.01F);
        assertEquals((-105.0F), float0, 0.01F);
    }
}
