package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import java.util.Collection;

/**
 * Unit tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that the TextRenderInfo constructor throws a NullPointerException
     * when the collection of marked content information is null. The constructor
     * internally attempts to create a new ArrayList from this collection, which is
     * an illegal operation on a null reference.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNPEForNullMarkedContentInfo() {
        // Arrange: Create the necessary non-null arguments for the constructor.
        PdfString dummyString = new PdfString("test text");
        GraphicsState dummyGraphicsState = new GraphicsState();
        Matrix dummyMatrix = new Matrix();
        Collection<MarkedContentInfo> nullMarkedContentCollection = null;

        // Act: Call the constructor with a null collection, which is expected to fail.
        // Assert: The @Test(expected) annotation handles the assertion that a
        // NullPointerException is thrown.
        new TextRenderInfo(dummyString, dummyGraphicsState, dummyMatrix, nullMarkedContentCollection);
    }
}