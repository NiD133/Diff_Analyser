package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class IEEE754rUtilsTest extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMinFloatWithThreeValues() {
        float result = IEEE754rUtils.min(0.0F, 629.0559F, -161.0F);
        assertEquals(-161.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMinFloatArray() {
        float[] values = {1.0F, 1.0F, 825.0F, 1.0F, 1.0F, 825.0F};
        float result = IEEE754rUtils.min(values);
        assertEquals(1.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMinSingleElementFloatArray() {
        float[] values = {-1.0F};
        float result = IEEE754rUtils.min(values);
        assertEquals(-1.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMinSingleElementDoubleArray() {
        double[] values = {1.0};
        double result = IEEE754rUtils.min(values);
        assertEquals(1.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMinDoubleArrayWithNegativeValue() {
        double[] values = {0.0, -567.84087, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        double result = IEEE754rUtils.min(values);
        assertEquals(-567.84087, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMinFloatWithIdenticalValues() {
        float result = IEEE754rUtils.min(4616.2134F, 4616.2134F, 4616.2134F);
        assertEquals(4616.2134F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMinFloatWithTwoIdenticalValues() {
        float result = IEEE754rUtils.min(0.0F, 0.0F);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMinFloatWithNegativeIdenticalValues() {
        float result = IEEE754rUtils.min(-513.9F, -513.9F);
        assertEquals(-513.9F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMinDoubleWithIdenticalValues() {
        double result = IEEE754rUtils.min(1.0, 1.0, 1.0);
        assertEquals(1.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMinDoubleWithMixedValues() {
        double result = IEEE754rUtils.min(1660.66, -567.84087, 4173.8887585);
        assertEquals(-567.84087, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMinDoubleWithTwoIdenticalValues() {
        double result = IEEE754rUtils.min(2855.35762973, 2855.35762973);
        assertEquals(2855.35762973, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMinDoubleWithNegativeAndZero() {
        double result = IEEE754rUtils.min(-855.02919, 0.0);
        assertEquals(-855.02919, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxFloatArray() {
        float[] values = {0.0F, 0.0F, 981.74023F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
        float result = IEEE754rUtils.max(values);
        assertEquals(981.74023F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMaxFloatArrayWithNegativeValues() {
        float[] values = {-835.94F, -1742.84F, -1.0F};
        float result = IEEE754rUtils.max(values);
        assertEquals(-1.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMaxDoubleArray() {
        double[] values = {0.0, 1769.7924036104557, 0.0, 0.0};
        double result = IEEE754rUtils.max(values);
        assertEquals(1769.7924036104557, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxDoubleArrayWithNegativeValues() {
        double[] values = {-1742.84, -163.0, -835.94, -1.0, -835.94, -1742.84, -1.0};
        double result = IEEE754rUtils.max(values);
        assertEquals(-1.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxFloatWithMixedValues() {
        float result = IEEE754rUtils.max(-1.0F, 0.0F, -2806.0F);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMaxFloatWithIdenticalNegativeValues() {
        float result = IEEE754rUtils.max(-659.8F, -659.8F, -659.8F);
        assertEquals(-659.8F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMaxFloatWithTwoValues() {
        float result = IEEE754rUtils.max(1.0F, 1190.8F);
        assertEquals(1190.8F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMaxFloatWithTwoIdenticalNegativeValues() {
        float result = IEEE754rUtils.max(-2157.9656F, -2157.9656F);
        assertEquals(-2157.9656F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMaxDoubleWithMixedValues() {
        double result = IEEE754rUtils.max(0.0, -1416.7961547236, 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxDoubleWithIdenticalValues() {
        double result = IEEE754rUtils.max(1.0, 1.0, 1.0);
        assertEquals(1.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxDoubleWithTwoValues() {
        double result = IEEE754rUtils.max(-4398.39854599338, 564.128287262);
        assertEquals(564.128287262, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxDoubleWithNegativeAndFloatValue() {
        double result = IEEE754rUtils.max(-3055.536570341673, -1.0F);
        assertEquals(-1.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMinFloatWithNullArray() {
        try {
            IEEE754rUtils.min((float[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinDoubleWithNullArray() {
        try {
            IEEE754rUtils.min((double[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMaxFloatWithNullArray() {
        try {
            IEEE754rUtils.max((float[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMaxDoubleWithNullArray() {
        try {
            IEEE754rUtils.max((double[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinFloatWithTwoIdenticalValues() {
        float result = IEEE754rUtils.min(4616.2134F, 4616.2134F);
        assertEquals(4616.2134F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMinDoubleWithTwoValues() {
        double result = IEEE754rUtils.min(0.0, 1460.933541);
        assertEquals(0.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxFloatWithTwoIdenticalValues() {
        float result = IEEE754rUtils.max(0.0F, 0.0F);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMaxDoubleWithTwoIdenticalValues() {
        double result = IEEE754rUtils.max(0.0, 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMinFloatWithEmptyArray() {
        float[] values = new float[0];
        try {
            IEEE754rUtils.min(values);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinDoubleWithEmptyArray() {
        double[] values = new double[0];
        try {
            IEEE754rUtils.min(values);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testMaxFloatWithEmptyArray() {
        float[] values = new float[0];
        try {
            IEEE754rUtils.max(values);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testMaxDoubleWithEmptyArray() {
        double[] values = new double[0];
        try {
            IEEE754rUtils.max(values);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinFloatWithThreeIdenticalValues() {
        float result = IEEE754rUtils.min(0.0F, 0.0F, 0.0F);
        assertEquals(0.0F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testMinDoubleWithZeroAndPositiveValue() {
        double result = IEEE754rUtils.min(976.5, 0.0, 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxDoubleWithNegativeValues() {
        double result = IEEE754rUtils.max(-1.0, -1.0, -4660.4);
        assertEquals(-1.0, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxFloatWithMixedValues() {
        float result = IEEE754rUtils.max(2162.2F, 0.0F, 2780.809F);
        assertEquals(2780.809F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testIEEE754rUtilsConstructor() {
        IEEE754rUtils utils = new IEEE754rUtils();
        assertNotNull(utils);
    }
}