package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DeprecatedAttributesTestTest4 {

    @Test
    void testDefaultToString() {
        assertEquals("Deprecated", DeprecatedAttributes.DEFAULT.toString());
    }
}
