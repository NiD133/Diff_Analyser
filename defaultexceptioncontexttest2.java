package org.apache.commons.lang3.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.apache.commons.lang3.ObjectToStringRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultExceptionContextTestTest2 extends AbstractExceptionContextTest<DefaultExceptionContext> {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        exceptionContext = new DefaultExceptionContext();
        super.setUp();
    }

    @Test
    void testFormattedExceptionMessageNull() {
        exceptionContext = new DefaultExceptionContext();
        assertEquals("", exceptionContext.getFormattedExceptionMessage(null));
    }
}