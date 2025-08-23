package org.apache.ibatis.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Properties;
import org.junit.jupiter.api.Test;

public class XNodeTestTest1 {

    @Test
    void formatXNodeToString() {
        XPathParser parser = new XPathParser("<users><user><id>100</id><name>Tom</name><age>30</age><cars><car index=\"1\">BMW</car><car index=\"2\">Audi</car><car index=\"3\">Benz</car></cars></user></users>");
        String usersNodeToString = parser.evalNode("/users").toString();
        String userNodeToString = parser.evalNode("/users/user").toString();
        String carsNodeToString = parser.evalNode("/users/user/cars").toString();
        String usersNodeToStringExpect = """
            <users>
              <user>
                <id>
                  100
                </id>
                <name>
                  Tom
                </name>
                <age>
                  30
                </age>
                <cars>
                  <car index="1">
                    BMW
                  </car>
                  <car index="2">
                    Audi
                  </car>
                  <car index="3">
                    Benz
                  </car>
                </cars>
              </user>
            </users>
            """;
        String userNodeToStringExpect = """
            <user>
              <id>
                100
              </id>
              <name>
                Tom
              </name>
              <age>
                30
              </age>
              <cars>
                <car index="1">
                  BMW
                </car>
                <car index="2">
                  Audi
                </car>
                <car index="3">
                  Benz
                </car>
              </cars>
            </user>
            """;
        String carsNodeToStringExpect = """
            <cars>
              <car index="1">
                BMW
              </car>
              <car index="2">
                Audi
              </car>
              <car index="3">
                Benz
              </car>
            </cars>
            """;
        assertEquals(usersNodeToStringExpect, usersNodeToString);
        assertEquals(userNodeToStringExpect, userNodeToString);
        assertEquals(carsNodeToStringExpect, carsNodeToString);
    }
}
