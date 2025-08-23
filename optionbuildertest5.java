package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class OptionBuilderTestTest5 {

    @Test
    void testCreateIncompleteOption() {
        assertThrows(IllegalStateException.class, (Executable) OptionBuilder::create);
        // implicitly reset the builder
        OptionBuilder.create("opt");
    }
}
