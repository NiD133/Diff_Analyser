package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DeprecatedAttributesTestTest1 {

    @Test
    void testBuilderNonDefaults() {
        // @formatter:off
        final DeprecatedAttributes value = DeprecatedAttributes.builder().setDescription("Use Bar instead!").setForRemoval(true).setSince("2.0").get();
        // @formatter:on
        assertEquals("Use Bar instead!", value.getDescription());
        assertEquals("2.0", value.getSince());
        assertEquals(true, value.isForRemoval());
    }
}
