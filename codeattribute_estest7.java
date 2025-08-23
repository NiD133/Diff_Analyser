package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class is a refactored version of an auto-generated test.
 * The original scaffolding for EvoSuite has been kept for compatibility.
 */
public class CodeAttribute_ESTestTest7_Improved extends CodeAttribute_ESTest_scaffolding {

    /**
     * Tests that CodeAttribute.toString() throws an ArrayIndexOutOfBoundsException
     * if it contains a malformed Annotation attribute.
     *
     * The malformation is created by declaring that an annotation has more element-value
     * pairs than are actually provided. This causes an out-of-bounds access when the
     * annotation's toString() method is transitively called.
     */
    @Test(timeout = 4000)
    public void toStringShouldThrowArrayIndexOutOfBoundsWhenContainingMalformedAnnotation() throws Throwable {
        // --- Arrange ---

        // 1. Set up the containing CodeAttribute. Its parameters are not critical for this test.
        final int maxStack = 97;
        final int maxLocals = 97;
        byte[] packedCode = new byte[2]; // Dummy bytecode data.
        List<ExceptionTableEntry> exceptionTable = Collections.emptyList();

        // The OperandManager requires many arrays; for this test, their content is irrelevant.
        int[] dummyOperandData = new int[6];
        OperandManager operandManager = new OperandManager(
            dummyOperandData, dummyOperandData, dummyOperandData, dummyOperandData,
            dummyOperandData, dummyOperandData, dummyOperandData, dummyOperandData,
            dummyOperandData, dummyOperandData, dummyOperandData, dummyOperandData,
            dummyOperandData, dummyOperandData, dummyOperandData, dummyOperandData,
            dummyOperandData, null, dummyOperandData, null, null);

        CodeAttribute codeAttribute = new CodeAttribute(maxStack, maxLocals, packedCode, null, operandManager, exceptionTable);

        // 2. Create a deliberately malformed Annotation.
        // It will claim to have 2 element-value pairs but will only be given 1.
        final int DECLARED_ELEMENT_VALUE_PAIRS = 2;
        final int ACTUAL_ELEMENT_VALUE_PAIRS = 1;

        // Create the single actual element-value pair.
        final int arbitraryTag = -84;
        AnnotationsAttribute.ElementValue[] elementValues = new AnnotationsAttribute.ElementValue[ACTUAL_ELEMENT_VALUE_PAIRS];
        elementValues[0] = new AnnotationsAttribute.ElementValue(arbitraryTag, null);

        // The array for element names must exist but its contents can be null for this test.
        CPUTF8[] elementNames = new CPUTF8[DECLARED_ELEMENT_VALUE_PAIRS];

        // Instantiate the annotation with the mismatched pair count.
        AnnotationsAttribute.Annotation malformedAnnotation = new AnnotationsAttribute.Annotation(
            DECLARED_ELEMENT_VALUE_PAIRS,
            null, // Annotation type (not relevant)
            elementNames,
            elementValues // This array is smaller than declared, causing the error.
        );

        // 3. Add the malformed annotation to the CodeAttribute via a container attribute.
        AnnotationsAttribute.Annotation[] annotations = new AnnotationsAttribute.Annotation[4];
        annotations[0] = malformedAnnotation;
        RuntimeVisibleorInvisibleAnnotationsAttribute runtimeAnnotationsAttribute =
            new RuntimeVisibleorInvisibleAnnotationsAttribute(null, annotations);

        codeAttribute.addAttribute(runtimeAnnotationsAttribute);

        // --- Act & Assert ---
        try {
            codeAttribute.toString();
            fail("Expected an ArrayIndexOutOfBoundsException due to the malformed annotation.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // The exception is expected because Annotation.toString() will try to access
            // elementValues[1], which does not exist.
            assertEquals("1", e.getMessage());

            // Verify the exception originates from the expected class, as in the original test.
            verifyException("org.apache.commons.compress.harmony.unpack200.bytecode.AnnotationsAttribute$Annotation", e);
        }
    }
}