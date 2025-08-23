package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultIndenterTestTest1 {

    @Test
    void withLinefeed() {
        DefaultIndenter defaultIndenter = new DefaultIndenter();
        DefaultIndenter defaultIndenterTwo = defaultIndenter.withLinefeed("-XG'#x");
        DefaultIndenter defaultIndenterThree = defaultIndenterTwo.withLinefeed("-XG'#x");
        assertEquals("-XG'#x", defaultIndenterThree.getEol());
        assertNotSame(defaultIndenterThree, defaultIndenter);
        assertSame(defaultIndenterThree, defaultIndenterTwo);
    }
}
