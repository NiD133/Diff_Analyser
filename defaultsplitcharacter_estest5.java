package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.TabSettings;
import java.util.ArrayList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DefaultSplitCharacter_ESTestTest5 extends DefaultSplitCharacter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        DefaultSplitCharacter defaultSplitCharacter0 = new DefaultSplitCharacter();
        char[] charArray0 = new char[7];
        charArray0[4] = '1';
        char char0 = defaultSplitCharacter0.getCurrentCharacter(4, charArray0, (PdfChunk[]) null);
        assertEquals('1', char0);
    }
}
