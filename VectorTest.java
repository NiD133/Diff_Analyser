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
 * the Free Foundation, Inc., 51 Franklin Street, Fifth Floor,
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
 * Test suite for the Vector class, focusing on vector-matrix operations
 * used in PDF coordinate transformations.
 * 
 * @author kevin
 */
public class VectorTest {

    /**
     * Tests the cross product operation between a vector and a matrix.
     * 
     * This test verifies that when a vector [2, 3, 4] is multiplied by a matrix
     * with values [5, 6, 7, 8, 9, 10], the resulting vector has the correct
     * transformed coordinates.
     * 
     * The matrix represents a 2x3 transformation matrix in the form:
     * | 5  6  7 |
     * | 8  9 10 |
     * 
     * Expected calculation:
     * - X component: (2 * 5) + (3 * 6) + (4 * 7) = 10 + 18 + 28 = 56... 
     * Wait, let me recalculate based on the expected result [67, 76, 4]:
     * This suggests the Z component (4) remains unchanged, indicating this might
     * be a 2D transformation where Z is preserved.
     */
    @Test
    public void testVectorMatrixCrossProduct() {
        // Given: A vector representing a point in 3D space
        Vector inputVector = new Vector(2, 3, 4);
        
        // And: A transformation matrix with 6 values (2x3 matrix)
        Matrix transformationMatrix = new Matrix(5, 6, 7, 8, 9, 10);
        
        // When: We apply the matrix transformation to the vector
        Vector actualResult = inputVector.cross(transformationMatrix);
        
        // Then: The result should match the expected transformed coordinates
        Vector expectedResult = new Vector(67, 76, 4);
        assertEquals("Vector-matrix cross product should produce correct transformed coordinates", 
                    expectedResult, actualResult);
    }
    
    /**
     * Additional test methods could be added here to cover other Vector operations:
     * - testVectorVectorCrossProduct() for vector-vector cross products
     * - testVectorSubtraction() for vector subtraction
     * - testVectorNormalization() for unit vector calculations
     * - testVectorDotProduct() for dot product operations
     * - testVectorLength() for magnitude calculations
     * - testVectorEquality() for equals() method verification
     */
}