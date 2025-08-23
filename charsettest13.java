package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

public class CharSetTestTest13 extends AbstractLangTest {

    @Test
    void testJavadocExamples() {
        assertFalse(CharSet.getInstance("^a-c").contains('a'));
        assertTrue(CharSet.getInstance("^a-c").contains('d'));
        assertTrue(CharSet.getInstance("^^a-c").contains('a'));
        assertFalse(CharSet.getInstance("^^a-c").contains('^'));
        assertTrue(CharSet.getInstance("^a-cd-f").contains('d'));
        assertTrue(CharSet.getInstance("a-c^").contains('^'));
        assertTrue(CharSet.getInstance("^", "a-c").contains('^'));
    }
}
