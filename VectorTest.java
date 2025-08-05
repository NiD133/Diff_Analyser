/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Vector} class, focusing on its mathematical operations.
 */
public class VectorTest {

    /**
     * The 'cross' method in iText's Vector class performs a vector-matrix multiplication,
     * which is a standard way to apply an affine transformation to a point. This test
     * verifies that a Vector multiplied by a Matrix produces the correct transformed Vector.
     *
     * <p>The transformation is calculated as follows: v' = v * M</p>
     * <p>Where v = [x, y, z] and M is the 3x3 transformation matrix.</p>
     *
     * <p>Given Vector v = [2, 3, 4]</p>
     * <p>And Matrix M from {@code new Matrix(5, 6, 7, 8, 9, 10)} which corresponds to:</p>
     * <pre>
     * [ 5  6  0 ]
     * [ 7  8  0 ]
     * [ 9 10  1 ]
     * </pre>
     *
     * <p>The resulting vector's components are calculated as:</p>
     * <ul>
     *   <li>x' = (2 * 5) + (3 * 7) + (4 * 9) = 10 + 21 + 36 = 67</li>
     *   <li>y' = (2 * 6) + (3 * 8) + (4 * 10) = 12 + 24 + 40 = 76</li>
     *   <li>z' = (2 * 0) + (3 * 0) + (4 * 1) = 4</li>
     * </ul>
     * <p>Thus, the expected result is the vector [67, 76, 4].</p>
     */
    @Test
    public void crossWithMatrix_shouldCorrectlyTransformVector() {
        // Arrange: Define the initial vector, the transformation matrix, and the expected result.
        // The Arrange-Act-Assert pattern separates the test logic into distinct sections.
        Vector initialVector = new Vector(2, 3, 4);
        Matrix transformationMatrix = new Matrix(5, 6, 7, 8, 9, 10);
        Vector expectedVector = new Vector(67, 76, 4);

        // Act: Apply the transformation by calling the 'cross' method.
        Vector actualVector = initialVector.cross(transformationMatrix);

        // Assert: Verify that the resulting vector matches the expected vector.
        assertEquals(expectedVector, actualVector);
    }
}