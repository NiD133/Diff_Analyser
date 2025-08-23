package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import java.util.zip.ZipException;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that calling the parse method with a null byte array
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void parseWithNullDataShouldThrowNullPointerException() throws ZipException {
        // The method signature for parse(byte[]) declares "throws ZipException".
        // While a NullPointerException is expected for a null input, the test
        // method's signature must be compatible with the method under test.
        ExtraFieldUtils.parse(null);
    }
}