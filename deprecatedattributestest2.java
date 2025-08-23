package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DeprecatedAttributesTestTest2 {

    @Test
    void testBuilderNonDefaultsToString() {
        // @formatter:off
        assertEquals("Deprecated for removal since 2.0: Use Bar instead!", DeprecatedAttributes.builder().setDescription("Use Bar instead!").setForRemoval(true).setSince("2.0").get().toString());
        assertEquals("Deprecated for removal: Use Bar instead!", DeprecatedAttributes.builder().setDescription("Use Bar instead!").setForRemoval(true).get().toString());
        assertEquals("Deprecated since 2.0: Use Bar instead!", DeprecatedAttributes.builder().setDescription("Use Bar instead!").setSince("2.0").get().toString());
        assertEquals("Deprecated: Use Bar instead!", DeprecatedAttributes.builder().setDescription("Use Bar instead!").get().toString());
        // @formatter:on
    }
}
