package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest25 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Vector vector0 = new Vector((-80.165F), (-80.165F), (-80.165F));
        boolean boolean0 = vector0.equals((Object) null);
        assertFalse(boolean0);
        assertEquals(19279.281F, vector0.lengthSquared(), 0.01F);
    }
}