package org.apache.commons.compress.harmony.unpack200.bytecode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

// The test class name and inheritance from the scaffolding are preserved from the original.
public class CodeAttribute_ESTestTest22 extends CodeAttribute_ESTest_scaffolding {

    /**
     * Tests that CodeAttribute.toString() throws a NullPointerException when it contains
     * a sub-attribute that was initialized with a null name. This occurs because
     * CodeAttribute.toString() delegates to the sub-attribute's toString() method,
     * which then fails when trying to access its null name.
     */
    @Test
    public void toStringShouldThrowNPEWhenAddedAttributeHasNullName() throws Pack200Exception {
        // Arrange: Set up a CodeAttribute and add a sub-attribute with a null name.

        // 1. Define basic parameters for the CodeAttribute.
        // These values are arbitrary as they don't affect this test's outcome.
        final int maxStack = 10;
        final int maxLocals = 10;
        final byte[] packedCode = new byte[0]; // No bytecode is needed for this test.
        final List<ExceptionTableEntry> exceptionTable = new LinkedList<>();

        // 2. The CodeAttribute constructor requires an OperandManager. Its internal state
        // is not relevant here, so we can initialize it with minimal data.
        final int[] emptyInts = new int[0];
        final OperandManager operandManager = new OperandManager(
            emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts,
            emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts,
            emptyInts, emptyInts, emptyInts, null, emptyInts, null, null);

        // 3. Create the CodeAttribute instance. The Segment can be null for this test.
        final CodeAttribute codeAttribute =
            new CodeAttribute(maxStack, maxLocals, packedCode, null, operandManager, exceptionTable);

        // 4. Create the sub-attribute with a null name (CPUTF8). This is the
        // condition that is expected to trigger the NullPointerException.
        final AnnotationsAttribute.Annotation[] annotations = new AnnotationsAttribute.Annotation[0];
        final RuntimeVisibleorInvisibleAnnotationsAttribute annotationAttributeWithNullName =
            new RuntimeVisibleorInvisibleAnnotationsAttribute(null, annotations); // The first argument is the attribute's name.

        codeAttribute.addAttribute(annotationAttributeWithNullName);

        // Act & Assert: Call toString() and verify that it throws the expected exception
        // from the correct source.
        try {
            codeAttribute.toString();
            fail("A NullPointerException was expected but not thrown.");
        } catch (final NullPointerException e) {
            // The exception is expected. We can further verify it originated from the correct class.
            final StackTraceElement topOfStack = e.getStackTrace()[0];
            assertEquals("Exception should originate from the annotation attribute's class.",
                RuntimeVisibleorInvisibleAnnotationsAttribute.class.getName(),
                topOfStack.getClassName());
        }
    }
}