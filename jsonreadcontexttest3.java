package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;
import static org.junit.jupiter.api.Assertions.*;

public class JsonReadContextTestTest3 extends JUnit5TestBase {

    static class MyContext extends JsonReadContext {

        public MyContext(JsonReadContext parent, int nestingDepth, DupDetector dups, int type, int lineNr, int colNr) {
            super(parent, nestingDepth, dups, type, lineNr, colNr);
        }
    }

    @Test
    void reset() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext jsonReadContext = JsonReadContext.createRootContext(dupDetector);
        final ContentReference bogusSrc = ContentReference.unknown();
        assertTrue(jsonReadContext.inRoot());
        assertEquals("root", jsonReadContext.typeDesc());
        assertEquals(1, jsonReadContext.startLocation(bogusSrc).getLineNr());
        assertEquals(0, jsonReadContext.startLocation(bogusSrc).getColumnNr());
        jsonReadContext.reset(200, 500, 200);
        assertFalse(jsonReadContext.inRoot());
        assertEquals("?", jsonReadContext.typeDesc());
        assertEquals(500, jsonReadContext.startLocation(bogusSrc).getLineNr());
        assertEquals(200, jsonReadContext.startLocation(bogusSrc).getColumnNr());
    }
}
