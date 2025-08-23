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

public class DefaultSplitCharacter_ESTestTest4 extends DefaultSplitCharacter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        char[] charArray0 = new char[1];
        DefaultSplitCharacter defaultSplitCharacter0 = (DefaultSplitCharacter) DefaultSplitCharacter.DEFAULT;
        char char0 = defaultSplitCharacter0.getCurrentCharacter(0, charArray0, (PdfChunk[]) null);
        assertEquals('\u0000', char0);
    }
}
