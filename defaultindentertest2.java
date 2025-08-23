package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultIndenterTestTest2 {

    @Test
    void withIndent() {
        DefaultIndenter defaultIndenter = new DefaultIndenter();
        DefaultIndenter defaultIndenterTwo = defaultIndenter.withIndent("9Qh/6,~n");
        DefaultIndenter defaultIndenterThree = defaultIndenterTwo.withIndent("9Qh/6,~n");
        assertEquals(System.lineSeparator(), defaultIndenterThree.getEol());
        assertNotSame(defaultIndenterThree, defaultIndenter);
        assertSame(defaultIndenterThree, defaultIndenterTwo);
    }
}
