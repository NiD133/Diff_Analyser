package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class IEEE754rUtils_ESTest extends IEEE754rUtils_ESTest_scaffolding {

    // Tests for min methods
    public static class MinTests {
        
        // Float array min tests
        @Test
        public void floatArrayMin_multipleValues_returnsSmallest() {
            float[] values = {1.0F, 1.0F, 825.0F, 1.0F, 1.0F, 825.0F};
            assertEquals(1.0F, IEEE754rUtils.min(values), 0.01F);
        }

        @Test
        public void floatArrayMin_singleValue_returnsValue() {
            float[] values = {-1.0F};
            assertEquals(-1.0F, IEEE754rUtils.min(values), 0.01F);
        }

        @Test
        public void floatArrayMin_emptyArray_throwsException() {
            float[] empty = new float[0];
            assertThrows(IllegalArgumentException.class, () -> IEEE754rUtils.min(empty));
        }

        @Test
        public void floatArrayMin_nullArray_throwsException() {
            assertThrows(NullPointerException.class, () -> IEEE754rUtils.min((float[]) null));
        }

        // Two-argument float min tests
        @Test
        public void minWithTwoFloats_returnsSmallest() {
            assertEquals(0.0F, IEEE754rUtils.min(0.0F, 629.0559F), 0.01F);
        }

        @Test
        public void minWithTwoNegativeFloats_returnsSmallest() {
            assertEquals(-513.9F, IEEE754rUtils.min(-513.9F, -513.9F), 0.01F);
        }

        // Three-argument float min tests
        @Test
        public void minWithThreeFloats_returnsSmallest() {
            assertEquals(-161.0F, IEEE754rUtils.min(0.0F, 629.0559F, -161.0F), 0.01F);
        }

        @Test
        public void minWithThreeIdenticalFloats_returnsValue() {
            float value = 4616.2134F;
            assertEquals(value, IEEE754rUtils.min(value, value, value), 0.01F);
        }

        // Double array min tests
        @Test
        public void doubleArrayMin_singleValue_returnsValue() {
            double[] values = {1.0};
            assertEquals(1.0, IEEE754rUtils.min(values), 0.01);
        }

        @Test
        public void doubleArrayMin_multipleValues_returnsSmallest() {
            double[] values = new double[8];
            values[1] = -567.84087;
            assertEquals(-567.84087, IEEE754rUtils.min(values), 0.01);
        }

        @Test
        public void doubleArrayMin_emptyArray_throwsException() {
            double[] empty = new double[0];
            assertThrows(IllegalArgumentException.class, () -> IEEE754rUtils.min(empty));
        }

        @Test
        public void doubleArrayMin_nullArray_throwsException() {
            assertThrows(NullPointerException.class, () -> IEEE754rUtils.min((double[]) null));
        }

        // Two-argument double min tests
        @Test
        public void minWithTwoDoubles_positiveAndNegative_returnsNegative() {
            assertEquals(-855.02919, IEEE754rUtils.min(-855.02919, 0.0), 0.01);
        }

        @Test
        public void minWithTwoDoubles_positiveValues_returnsSmallest() {
            assertEquals(0.0, IEEE754rUtils.min(0.0, 1460.933541), 0.01);
        }

        // Three-argument double min tests
        @Test
        public void minWithThreeDoubles_returnsSmallest() {
            assertEquals(-567.84087, IEEE754rUtils.min(1660.66, -567.84087, 4173.8887585), 0.01);
        }

        @Test
        public void minWithThreeIdenticalDoubles_returnsValue() {
            double value = 1.0;
            assertEquals(value, IEEE754rUtils.min(value, value, value), 0.01);
        }

        @Test
        public void minWithThreeDoubles_middleValueIsSmallest_returnsMiddle() {
            assertEquals(0.0, IEEE754rUtils.min(976.5, 0.0, 0.0), 0.01);
        }
    }

    // Tests for max methods
    public static class MaxTests {
        
        // Float array max tests
        @Test
        public void floatArrayMax_multipleValues_returnsLargest() {
            float[] values = new float[9];
            values[2] = 981.74023F;
            assertEquals(981.74023F, IEEE754rUtils.max(values), 0.01F);
        }

        @Test
        public void floatArrayMax_negativeValues_returnsLargest() {
            float[] values = {-835.94F, -1742.84F, -1.0F};
            assertEquals(-1.0F, IEEE754rUtils.max(values), 0.01F);
        }

        @Test
        public void floatArrayMax_emptyArray_throwsException() {
            float[] empty = new float[0];
            assertThrows(IllegalArgumentException.class, () -> IEEE754rUtils.max(empty));
        }

        @Test
        public void floatArrayMax_nullArray_throwsException() {
            assertThrows(NullPointerException.class, () -> IEEE754rUtils.max((float[]) null));
        }

        // Two-argument float max tests
        @Test
        public void maxWithTwoFloats_returnsLargest() {
            assertEquals(1190.8F, IEEE754rUtils.max(1.0F, 1190.8F), 0.01F);
        }

        @Test
        public void maxWithTwoNegativeFloats_returnsValue() {
            float value = -2157.9656F;
            assertEquals(value, IEEE754rUtils.max(value, value), 0.01F);
        }

        @Test
        public void maxWithTwoZeros_returnsZero() {
            assertEquals(0.0F, IEEE754rUtils.max(0.0F, 0.0F), 0.01F);
        }

        // Three-argument float max tests
        @Test
        public void maxWithThreeFloats_returnsLargest() {
            assertEquals(2780.809F, IEEE754rUtils.max(2162.2F, 0.0F, 2780.809F), 0.01F);
        }

        @Test
        public void maxWithThreeIdenticalFloats_returnsValue() {
            float value = -659.8F;
            assertEquals(value, IEEE754rUtils.max(value, value, value), 0.01F);
        }

        @Test
        public void maxWithMixedFloats_returnsLargestPositive() {
            assertEquals(0.0F, IEEE754rUtils.max(-1.0F, 0.0F, -2806.0F), 0.01F);
        }

        // Double array max tests
        @Test
        public void doubleArrayMax_multipleValues_returnsLargest() {
            double[] values = new double[4];
            values[1] = 1769.7924036104557;
            assertEquals(1769.7924036104557, IEEE754rUtils.max(values), 0.01);
        }

        @Test
        public void doubleArrayMax_negativeValues_returnsLargest() {
            double[] values = {-1742.84, -163.0, -835.94, -1.0, -835.94, -1742.84, -1.0};
            assertEquals(-1.0, IEEE754rUtils.max(values), 0.01);
        }

        @Test
        public void doubleArrayMax_emptyArray_throwsException() {
            double[] empty = new double[0];
            assertThrows(IllegalArgumentException.class, () -> IEEE754rUtils.max(empty));
        }

        @Test
        public void doubleArrayMax_nullArray_throwsException() {
            assertThrows(NullPointerException.class, () -> IEEE754rUtils.max((double[]) null));
        }

        // Two-argument double max tests
        @Test
        public void maxWithTwoDoubles_returnsLargest() {
            assertEquals(564.128287262, IEEE754rUtils.max(-4398.39854599338, 564.128287262), 0.01);
        }

        @Test
        public void maxWithTwoNegativeDoubles_returnsLargest() {
            assertEquals(-1.0, IEEE754rUtils.max(-3055.536570341673, -1.0), 0.01);
        }

        @Test
        public void maxWithTwoZeros_returnsZero() {
            assertEquals(0.0, IEEE754rUtils.max(0.0, 0.0), 0.01);
        }

        // Three-argument double max tests
        @Test
        public void maxWithThreeDoubles_returnsLargest() {
            assertEquals(0.0, IEEE754rUtils.max(0.0, -1416.7961547236, 0.0), 0.01);
        }

        @Test
        public void maxWithThreeIdenticalDoubles_returnsValue() {
            double value = 1.0;
            assertEquals(value, IEEE754rUtils.max(value, value, value), 0.01);
        }

        @Test
        public void maxWithThreeNegativeDoubles_returnsLargest() {
            assertEquals(-1.0, IEEE754rUtils.max(-1.0, -1.0, -4660.4), 0.01);
        }
    }

    // Edge case tests
    public static class EdgeCaseTests {
        @Test
        public void minWithFloatArray_allZeros_returnsZero() {
            float[] zeros = new float[2];
            assertEquals(0.0F, IEEE754rUtils.min(zeros), 0.01F);
        }

        @Test
        public void minWithDoubleArray_allZeros_returnsZero() {
            double[] zeros = new double[4];
            assertEquals(0.0, IEEE754rUtils.min(zeros), 0.01);
        }

        @Test
        public void maxWithFloatArray_allZeros_returnsZero() {
            float[] zeros = new float[3];
            assertEquals(0.0F, IEEE754rUtils.max(zeros), 0.01F);
        }

        @Test
        public void maxWithDoubleArray_allZeros_returnsZero() {
            double[] zeros = new double[4];
            assertEquals(0.0, IEEE754rUtils.max(zeros), 0.01);
        }

        @Test
        public void constructorExists() {
            // Just to cover the constructor
            new IEEE754rUtils();
        }
    }
}