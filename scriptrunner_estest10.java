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

public class ScriptRunner_ESTestTest10 extends ScriptRunner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Statement statement0 = mock(Statement.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(statement0).execute(anyString());
        doReturn(false, false).when(statement0).getMoreResults();
        doReturn(705, (-227290201), (-1)).when(statement0).getUpdateCount();
        Connection connection0 = mock(Connection.class, new ViolatedAssumptionAnswer());
        doReturn(statement0).when(connection0).createStatement();
        doReturn(true, true, true).when(connection0).getAutoCommit();
        ScriptRunner scriptRunner0 = new ScriptRunner(connection0);
        StringReader stringReader0 = new StringReader("_DO3)u?Yu1;[5^Fn");
        scriptRunner0.runScript(stringReader0);
    }
}
