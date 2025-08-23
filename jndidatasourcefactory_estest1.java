package org.apache.ibatis.datasource.jndi;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JndiDataSourceFactory_ESTestTest1 extends JndiDataSourceFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        JndiDataSourceFactory jndiDataSourceFactory0 = new JndiDataSourceFactory();
        // Undeclared exception!
        try {
            jndiDataSourceFactory0.setProperties((Properties) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }
}
