package org.jsoup.helper;

import org.junit.Test;
import java.util.Map;

/**
 * Tests for the {@link W3CDom} helper class.
 */
public class W3CDomTest {

    /**
     * Verifies that the propertiesFromMap method throws a NullPointerException
     * when given a null map as input, adhering to its contract.
     */
    @Test(expected = NullPointerException.class)
    public void propertiesFromMapShouldThrowExceptionOnNullInput() {
        // The method is expected to fail fast when the input map is null.
        // The cast is necessary to resolve method ambiguity for the compiler.
        W3CDom.propertiesFromMap((Map<String, String>) null);
    }
}