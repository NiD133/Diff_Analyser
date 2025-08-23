package org.apache.commons.lang3.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.apache.commons.lang3.ObjectToStringRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultExceptionContextTestTest1 extends AbstractExceptionContextTest<DefaultExceptionContext> {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        exceptionContext = new DefaultExceptionContext();
        super.setUp();
    }

    @Test
    void testFormattedExceptionMessageExceptionHandling() {
        exceptionContext = new DefaultExceptionContext();
        final String label1 = "throws 1";
        final String label2 = "throws 2";
        exceptionContext.addContextValue(label1, new ObjectToStringRuntimeException(label1));
        exceptionContext.addContextValue(label2, new ObjectToStringRuntimeException(label2));
        final String message = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);
        assertTrue(message.startsWith(TEST_MESSAGE));
        assertTrue(message.contains(label1));
        assertTrue(message.contains(label2));
    }
}
