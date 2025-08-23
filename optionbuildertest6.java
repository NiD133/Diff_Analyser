package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class OptionBuilderTestTest6 {

    @Test
    void testIllegalOptions() {
        // bad single character option
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.withDescription("option description").create('"'));
        // bad character in option string
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create("opt`"));
        // valid option
        OptionBuilder.create("opt");
    }
}
