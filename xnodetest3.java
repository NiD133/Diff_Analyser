package org.apache.ibatis.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Properties;
import org.junit.jupiter.api.Test;

public class XNodeTestTest3 {

    @Test
    void xnodeToStringVariables() throws Exception {
        String src = "<root attr='${x}'>y = ${y}<sub attr='${y}'>x = ${x}</sub></root>";
        String expected = "<root attr=\"foo\">\n  y = bar\n  <sub attr=\"bar\">\n    x = foo\n  </sub>\n</root>\n";
        Properties vars = new Properties();
        vars.put("x", "foo");
        vars.put("y", "bar");
        XPathParser parser = new XPathParser(src, false, vars);
        XNode selectNode = parser.evalNode("/root");
        assertEquals(expected, selectNode.toString());
    }
}
