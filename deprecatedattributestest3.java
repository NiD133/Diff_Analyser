package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DeprecatedAttributesTestTest3 {

    @Test
    void testDefaultBuilder() {
        final DeprecatedAttributes defaultValue = DeprecatedAttributes.builder().get();
        assertEquals(DeprecatedAttributes.DEFAULT.getDescription(), defaultValue.getDescription());
        assertEquals(DeprecatedAttributes.DEFAULT.getSince(), defaultValue.getSince());
        assertEquals(DeprecatedAttributes.DEFAULT.isForRemoval(), defaultValue.isForRemoval());
    }
}
