package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class DefaultSplitCharacterTestTest3 {

    private final String[] INPUT_TEXT = new String[] { "tha111-is one that should-be-splitted-right-herel-2018-12-18", "anddate format2 01-01-1920" };

    private boolean isPsplitCharacter(int current, String text) {
        return new DefaultSplitCharacter().isSplitCharacter(75, current, text.length() + 1, text.toCharArray(), null);
    }

    @Test
    public void hypenBeforeDateTest() {
        //check HyphenBeforeAdate ex. '-2019-01-01'
        Assert.assertTrue(isPsplitCharacter(49, INPUT_TEXT[0]));
    }
}
