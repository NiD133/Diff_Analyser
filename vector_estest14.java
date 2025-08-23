package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Vector_ESTestTest14 extends Vector_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Vector vector0 = new Vector((-2905.637F), (-2905.637F), (-1.0F));
        Vector vector1 = new Vector(0, (-444.7289F), (-3839.2217F));
        Vector vector2 = vector0.cross(vector1);
        Vector vector3 = vector2.cross(vector1);
        Vector vector4 = vector3.cross(vector0);
        Vector vector5 = vector4.cross(vector3);
        Vector vector6 = vector5.normalize();
        assertEquals(0.0F, vector6.length(), 0.01F);
        assertEquals(4.2085848E26F, vector4.lengthSquared(), 0.01F);
    }
}
