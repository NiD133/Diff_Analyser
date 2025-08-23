package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;
import static org.junit.jupiter.api.Assertions.*;

public class JsonReadContextTestTest1 extends JUnit5TestBase {

    static class MyContext extends JsonReadContext {

        public MyContext(JsonReadContext parent, int nestingDepth, DupDetector dups, int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    @Test
    void setCurrentNameTwiceWithSameNameRaisesJsonParseException() throws Exception {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext jsonReadContext = new JsonReadContext((JsonReadContext) null, 0, dupDetector, 2441, 2441, 2441);
        jsonReadContext.setCurrentName("dupField");
        try {
            jsonReadContext.setCurrentName("dupField");
            fail("Should not pass");
        } catch (JsonParseException e) {
            verifyException(e, "Duplicate field 'dupField'");
        }
    }
}
