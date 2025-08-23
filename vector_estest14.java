package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Vector} class.
 * This class contains an improved version of a generated test case.
 */
public class Vector_ESTestTest14 extends Vector_ESTest_scaffolding {

    /**
     * Tests that a specific, complex sequence of vector cross products results in a
     * vector that, upon normalization, has a length of zero.
     *
     * <p>This test case verifies an edge case, likely related to floating-point
     * arithmetic with large-magnitude inputs. The chain of operations is as follows:
     * <ol>
     *     <li>v2 = vectorA x vectorB</li>
     *     <li>v3 = v2 x vectorB</li>
     *     <li>v4 = v3 x vectorA</li>
     *     <li>v5 = v4 x v3</li>
     *     <li>v6 = v5.normalize()</li>
     * </ol>
     * The test asserts that the length of the final vector (v6) is zero. This implies
     * that the preceding vector (v5) was the zero vector. An assertion on an
     * intermediate vector (v4) is also included to validate the calculation chain.
     * </p>
     */
    @Test(timeout = 4000)
    public void normalizingComplexSequenceOfCrossProductsCanYieldZeroVector() {
        // ARRANGE
        // These specific input vectors with large, non-trivial values are chosen
        // to trigger the edge case behavior under test.
        Vector vectorA = new Vector(-2905.637F, -2905.637F, -1.0F);
        Vector vectorB = new Vector(0F, -444.7289F, -3839.2217F);

        // Define expected values for clarity.
        final float expectedIntermediateLengthSq = 4.2085848E26F;
        final float expectedFinalNormalizedLength = 0.0F;
        final float delta = 0.01F;

        // ACT
        // A complex sequence of cross products is performed.
        Vector crossA_B = vectorA.cross(vectorB);
        Vector crossAB_crossB = crossA_B.cross(vectorB);
        Vector intermediateVector = crossAB_crossB.cross(vectorA);
        Vector finalVector = intermediateVector.cross(crossAB_crossB);
        Vector normalizedFinalVector = finalVector.normalize();

        // ASSERT
        // 1. Assert a property of an intermediate vector in the chain.
        // This helps confirm the calculation is proceeding as expected up to this point.
        assertEquals(
                "Squared length of the intermediate vector did not match the expected value.",
                expectedIntermediateLengthSq,
                intermediateVector.lengthSquared(),
                delta);

        // 2. Assert the main property of the final result.
        // A zero length after normalization implies the original vector was the zero vector.
        assertEquals(
                "The length of the final normalized vector should be zero.",
                expectedFinalNormalizedLength,
                normalizedFinalVector.length(),
                delta);
    }
}