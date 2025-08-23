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

public class ScriptRunner_ESTestTest4 extends ScriptRunner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        FileSystemHandling.appendStringToFile((EvoSuiteFile) null, "--UVo%w/S[2W`*&");
        ResultSetMetaData resultSetMetaData0 = mock(ResultSetMetaData.class, new ViolatedAssumptionAnswer());
        doReturn(1192).when(resultSetMetaData0).getColumnCount();
        doReturn((String) null, (String) null, (String) null, (String) null, (String) null).when(resultSetMetaData0).getColumnLabel(anyInt());
        ResultSet resultSet0 = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(resultSetMetaData0).when(resultSet0).getMetaData();
        doReturn(false).when(resultSet0).next();
        Statement statement0 = mock(Statement.class, new ViolatedAssumptionAnswer());
        doReturn(true).when(statement0).execute(anyString());
        doReturn(false, false, true).when(statement0).getMoreResults();
        doReturn(resultSet0, (ResultSet) null).when(statement0).getResultSet();
        doReturn((-1588), 43).when(statement0).getUpdateCount();
        doReturn((SQLWarning) null, (SQLWarning) null, (SQLWarning) null, (SQLWarning) null).when(statement0).getWarnings();
        Connection connection0 = mock(Connection.class, new ViolatedAssumptionAnswer());
        doReturn(statement0).when(connection0).createStatement();
        doReturn(false, false).when(connection0).getAutoCommit();
        ScriptRunner scriptRunner0 = new ScriptRunner(connection0);
        scriptRunner0.setStopOnError(false);
        scriptRunner0.setRemoveCRs(true);
        scriptRunner0.setThrowWarning(true);
        scriptRunner0.setSendFullScript(true);
        scriptRunner0.setRemoveCRs(false);
        scriptRunner0.setAutoCommit(false);
        scriptRunner0.setSendFullScript(true);
        Reader reader0 = Reader.nullReader();
        // Undeclared exception!
        try {
            scriptRunner0.runScript(reader0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Error executing: .  Cause: java.lang.NullPointerException
            //
            verifyException("org.apache.ibatis.jdbc.ScriptRunner", e);
        }
    }
}
