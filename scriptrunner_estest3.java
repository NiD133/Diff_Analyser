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

public class ScriptRunner_ESTestTest3 extends ScriptRunner_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        ScriptRunner scriptRunner0 = new ScriptRunner((Connection) null);
        Reader reader0 = Reader.nullReader();
        // Undeclared exception!
        try {
            scriptRunner0.runScript(reader0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Could not set AutoCommit to false. Cause: java.lang.NullPointerException
            //
            verifyException("org.apache.ibatis.jdbc.ScriptRunner", e);
        }
    }
}
