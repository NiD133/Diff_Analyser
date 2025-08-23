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

public class JndiDataSourceFactory_ESTestTest2 extends JndiDataSourceFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        JndiDataSourceFactory jndiDataSourceFactory0 = new JndiDataSourceFactory();
        Properties properties0 = new Properties();
        UnpooledDataSource unpooledDataSource0 = new UnpooledDataSource("initial_context", "data_source", properties0);
        Object object0 = new Object();
        properties0.put(unpooledDataSource0, object0);
        // Undeclared exception!
        try {
            jndiDataSourceFactory0.setProperties(properties0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // class org.apache.ibatis.datasource.unpooled.UnpooledDataSource cannot be cast to class java.lang.String (org.apache.ibatis.datasource.unpooled.UnpooledDataSource is in unnamed module of loader org.evosuite.instrumentation.InstrumentingClassLoader @69dd31fb; java.lang.String is in module java.base of loader 'bootstrap')
            //
            verifyException("org.apache.ibatis.datasource.jndi.JndiDataSourceFactory", e);
        }
    }
}
