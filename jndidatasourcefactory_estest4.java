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

public class JndiDataSourceFactory_ESTestTest4 extends JndiDataSourceFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test3() throws Throwable {
        JndiDataSourceFactory jndiDataSourceFactory0 = new JndiDataSourceFactory();
        Properties properties0 = new Properties();
        StringReader stringReader0 = new StringReader("data_source");
        properties0.load((Reader) stringReader0);
        // Undeclared exception!
        try {
            jndiDataSourceFactory0.setProperties(properties0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // There was an error configuring JndiDataSourceTransactionPool. Cause: javax.naming.NoInitialContextException: Need to specify class name in environment or system property, or in an application resource file: java.naming.factory.initial
            //
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }
}
