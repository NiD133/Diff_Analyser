package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Tests the behavior of the {@link XmpWriter} class under specific conditions.
 */
public class XmpWriter_ESTestTest15 extends XmpWriter_ESTest_scaffolding {

    /**
     * Verifies that appendArrayItem throws a NullPointerException if the internal
     * XMPMeta object is null. This tests the robustness of the method against an
     * unexpected internal state.
     */
    @Test(expected = NullPointerException.class)
    public void appendArrayItem_whenInternalXmpMetaIsNull_throwsNullPointerException() throws IOException, XMPException {
        // Arrange: Create an XmpWriter and force it into an invalid state.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);

        // Artificially set the internal xmpMeta object to null.
        // This is not a standard use case, as the constructor always initializes xmpMeta.
        // This setup specifically tests the method's handling of a corrupt internal state.
        xmpWriter.xmpMeta = null;

        // Define dummy arguments for the method call.
        String dummySchemaNS = "http://ns.example.com/dummy/1.0/";
        String dummyArrayName = "DummyArray";
        String dummyItemValue = "item1";

        // Act: Attempt to append an array item.
        // This call is expected to fail because it will try to access the null xmpMeta object.
        xmpWriter.appendArrayItem(dummySchemaNS, dummyArrayName, dummyItemValue);

        // Assert: The test will pass if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}