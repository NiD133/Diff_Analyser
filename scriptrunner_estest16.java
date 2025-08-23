package org.apache.ibatis.jdbc;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.sql.Statement;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

public class ScriptRunner_ESTestTest16 extends ScriptRunner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Connection connection0 = mock(Connection.class, new ViolatedAssumptionAnswer());
        doReturn(false, false, false).when(connection0).getAutoCommit();
        ScriptRunner scriptRunner0 = new ScriptRunner(connection0);
        StringReader stringReader0 = new StringReader("--U1!-k'1|Ud7jJjpCMf");
        scriptRunner0.runScript(stringReader0);
    }
}
