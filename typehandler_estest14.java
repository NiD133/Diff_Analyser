package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.util.Date;
import java.util.Map;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class TypeHandler_ESTestTest14 extends TypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        TypeHandler typeHandler0 = TypeHandler.getDefault();
        Class<Date> class0 = Date.class;
        Converter<Date, ?> converter0 = typeHandler0.getConverter(class0);
        assertNotNull(converter0);
    }
}
