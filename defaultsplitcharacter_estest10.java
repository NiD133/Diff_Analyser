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

public class DefaultSplitCharacter_ESTestTest10 extends DefaultSplitCharacter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        DefaultSplitCharacter defaultSplitCharacter0 = new DefaultSplitCharacter('\u02FA');
        char[] charArray0 = new char[51];
        boolean boolean0 = defaultSplitCharacter0.isSplitCharacter('\u0000', 0, 0, charArray0, (PdfChunk[]) null);
        assertFalse(boolean0);
    }
}