package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

/**
 * Tests for {@link ExtraFieldUtils}.
 */
public class ExtraFieldUtils_ESTestTest2 extends ExtraFieldUtils_ESTest_scaffolding {

    /**
     * Verifies that mergeLocalFileDataData throws a NullPointerException
     * when the input array contains a null element.
     */
    @Test(expected = NullPointerException.class)
    public void mergeLocalFileDataDataShouldThrowNullPointerExceptionForArrayWithNullElement() {
        // Arrange: Create an array of extra fields where one element is null.
        // The method under test is expected to fail when it tries to access this null element.
        final ZipExtraField[] fieldsWithNull = new ZipExtraField[2];
        fieldsWithNull[0] = new JarMarker(); // A valid, non-null extra field.
        fieldsWithNull[1] = null;            // The null element that should trigger the exception.

        // Act: Attempt to merge the fields.
        // This call is expected to throw a NullPointerException.
        ExtraFieldUtils.mergeLocalFileDataData(fieldsWithNull);

        // Assert: The expected exception is declared in the @Test annotation,
        // so no further assertion is needed. The test will fail if the
        // expected exception is not thrown.
    }
}