package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class OptionBuilderTestTest3 {

    @Test
    void testBuilderIsResettedAlways() {
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.withDescription("JUnit").create('"'));
        assertNull(OptionBuilder.create('x').getDescription(), "we inherited a description");
        assertThrows(IllegalStateException.class, (Executable) OptionBuilder::create);
        assertNull(OptionBuilder.create('x').getDescription(), "we inherited a description");
    }
}
