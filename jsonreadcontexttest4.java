package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;
import static org.junit.jupiter.api.Assertions.*;

public class JsonReadContextTestTest4 extends JUnit5TestBase {

    static class MyContext extends JsonReadContext {

        public MyContext(JsonReadContext parent, int nestingDepth, DupDetector dups, int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    // [core#1421]
    @Test
    void testExtension() {
        MyContext context = new MyContext(null, 0, null, 0, 0, 0);
        assertNotNull(context);
    }
}
