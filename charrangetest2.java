package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class CharRangeTestTest2 extends AbstractLangTest {

    @Test
    void testConstructorAccessors_is() {
        final CharRange rangea = CharRange.is('a');
        assertEquals('a', rangea.getStart());
        assertEquals('a', rangea.getEnd());
        assertFalse(rangea.isNegated());
        assertEquals("a", rangea.toString());
    }
}
