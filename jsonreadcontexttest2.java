package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;
import static org.junit.jupiter.api.Assertions.*;

public class JsonReadContextTestTest2 extends JUnit5TestBase {

    static class MyContext extends JsonReadContext {

        public MyContext(JsonReadContext parent, int nestingDepth, DupDetector dups, int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    @Test
    void setCurrentName() throws Exception {
        JsonReadContext jsonReadContext = JsonReadContext.createRootContext(0, 0, (DupDetector) null);
        jsonReadContext.setCurrentName("abc");
        assertEquals("abc", jsonReadContext.getCurrentName());
        jsonReadContext.setCurrentName(null);
        assertNull(jsonReadContext.getCurrentName());
    }
}
