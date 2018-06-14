/*
 * Copyright (c) 2002-2018, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.column;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItemComparator;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Test class for the RecordColumnCellComparator
 */
public class RecordColumnCellComparatorTest extends LuteceTestCase
{
    // Constants
    private static final String DEFAULT_SORT_ATTRIBUTE_NAME = "name";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown( ) throws Exception
    {
        super.tearDown( );
    }

    /**
     * Test for the method {@link RecordColumnCellComparator#compare(RecordColumnCell, RecordColumnCell)} without any values on each RecordColumnCell
     */
    public void testCompareWithoutValues( )
    {
        RecordColumnCell recordColumnCellOne = new RecordColumnCell( );

        RecordColumnCell recordColumnCellTwo = new RecordColumnCell( );

        RecordColumnCellComparator recordColumnCellComparator = new RecordColumnCellComparator( DEFAULT_SORT_ATTRIBUTE_NAME );

        int nComparisonResult = recordColumnCellComparator.compare( recordColumnCellOne, recordColumnCellTwo );
        assertThat( nComparisonResult, is( NumberUtils.INTEGER_ZERO ) );
    }

    /**
     * Test for the method {@link RecordColumnCellComparator#compare(RecordColumnCell, RecordColumnCell)} with the second RecordColumnCell which have a greater
     * value than the first
     */
    public void testCompareWithSecondCellValueGreater( )
    {
        Map<String, Object> mapRecordColumnValuesOne = new LinkedHashMap<>( );
        mapRecordColumnValuesOne.put( DEFAULT_SORT_ATTRIBUTE_NAME, "Martin" );
        RecordColumnCell recordColumnCellOne = new RecordColumnCell( );
        recordColumnCellOne.setRecordColumnCellValues( mapRecordColumnValuesOne );

        Map<String, Object> mapRecordColumnValuesTwo = new LinkedHashMap<>( );
        mapRecordColumnValuesTwo.put( DEFAULT_SORT_ATTRIBUTE_NAME, "Albert" );
        RecordColumnCell recordColumnCellTwo = new RecordColumnCell( );
        recordColumnCellTwo.setRecordColumnCellValues( mapRecordColumnValuesTwo );

        RecordColumnCellComparator recordColumnCellComparator = new RecordColumnCellComparator( DEFAULT_SORT_ATTRIBUTE_NAME );

        int nComparisonResult = recordColumnCellComparator.compare( recordColumnCellOne, recordColumnCellTwo );
        assertTrue( nComparisonResult > NumberUtils.INTEGER_ZERO );
    }

    /**
     * Test for the method {@link RecordColumnCellComparator#compare(RecordColumnCell, RecordColumnCell)} with the first RecordColumnCell which have a greater
     * value than the second
     */
    public void testCompareWithFirstCellValueGreater( )
    {
        Map<String, Object> mapRecordColumnValuesOne = new LinkedHashMap<>( );
        mapRecordColumnValuesOne.put( DEFAULT_SORT_ATTRIBUTE_NAME, "Albert" );
        RecordColumnCell recordColumnCellOne = new RecordColumnCell( );
        recordColumnCellOne.setRecordColumnCellValues( mapRecordColumnValuesOne );

        Map<String, Object> mapRecordColumnValuesTwo = new LinkedHashMap<>( );
        mapRecordColumnValuesTwo.put( DEFAULT_SORT_ATTRIBUTE_NAME, "Martin" );
        RecordColumnCell recordColumnCellTwo = new RecordColumnCell( );
        recordColumnCellTwo.setRecordColumnCellValues( mapRecordColumnValuesTwo );

        RecordColumnCellComparator recordColumnCellComparator = new RecordColumnCellComparator( DEFAULT_SORT_ATTRIBUTE_NAME );

        int nComparisonResult = recordColumnCellComparator.compare( recordColumnCellOne, recordColumnCellTwo );
        assertTrue( nComparisonResult < NumberUtils.INTEGER_ZERO );
    }

    /**
     * Test for the method {@link RecordColumnCellComparator#compare(RecordColumnCell, RecordColumnCell)} with integer values for the sorting key
     */
    public void testCompareWithIntegerValues( )
    {
        Map<String, Object> mapRecordColumnValuesOne = new LinkedHashMap<>( );
        mapRecordColumnValuesOne.put( DEFAULT_SORT_ATTRIBUTE_NAME, 1 );
        RecordColumnCell recordColumnCellOne = new RecordColumnCell( );
        recordColumnCellOne.setRecordColumnCellValues( mapRecordColumnValuesOne );

        Map<String, Object> mapRecordColumnValuesTwo = new LinkedHashMap<>( );
        mapRecordColumnValuesTwo.put( DEFAULT_SORT_ATTRIBUTE_NAME, 10 );
        RecordColumnCell recordColumnCellTwo = new RecordColumnCell( );
        recordColumnCellTwo.setRecordColumnCellValues( mapRecordColumnValuesTwo );

        RecordColumnCellComparator recordColumnCellComparator = new RecordColumnCellComparator( DEFAULT_SORT_ATTRIBUTE_NAME );

        int nComparisonResult = recordColumnCellComparator.compare( recordColumnCellOne, recordColumnCellTwo );
        assertThat( nComparisonResult, is( NumberUtils.INTEGER_MINUS_ONE ) );
    }

    /**
     * Test for the method {@link DirectoryRecordItemComparator#compare(DirectoryRecordItem, DirectoryRecordItem)} with the first RecordCellColumn which have a
     * null value
     */
    public void testCompareWithFirstCellNullValue( )
    {
        Map<String, Object> mapRecordColumnValuesOne = new LinkedHashMap<>( );
        mapRecordColumnValuesOne.put( DEFAULT_SORT_ATTRIBUTE_NAME, null );
        RecordColumnCell recordColumnCellOne = new RecordColumnCell( );
        recordColumnCellOne.setRecordColumnCellValues( mapRecordColumnValuesOne );

        Map<String, Object> mapRecordColumnValuesTwo = new LinkedHashMap<>( );
        mapRecordColumnValuesTwo.put( DEFAULT_SORT_ATTRIBUTE_NAME, "Martin" );
        RecordColumnCell recordColumnCellTwo = new RecordColumnCell( );
        recordColumnCellTwo.setRecordColumnCellValues( mapRecordColumnValuesTwo );

        RecordColumnCellComparator recordColumnCellComparator = new RecordColumnCellComparator( DEFAULT_SORT_ATTRIBUTE_NAME );

        int nComparisonResult = recordColumnCellComparator.compare( recordColumnCellOne, recordColumnCellTwo );
        assertThat( nComparisonResult, is( NumberUtils.INTEGER_MINUS_ONE ) );
    }

    /**
     * Test for the method {@link DirectoryRecordItemComparator#compare(DirectoryRecordItem, DirectoryRecordItem)} with the second RecordCellColumn which have a
     * null value
     */
    public void testCompareWithSecondCellNullValue( )
    {
        Map<String, Object> mapRecordColumnValuesOne = new LinkedHashMap<>( );
        mapRecordColumnValuesOne.put( DEFAULT_SORT_ATTRIBUTE_NAME, "Martin" );
        RecordColumnCell recordColumnCellOne = new RecordColumnCell( );
        recordColumnCellOne.setRecordColumnCellValues( mapRecordColumnValuesOne );

        Map<String, Object> mapRecordColumnValuesTwo = new LinkedHashMap<>( );
        mapRecordColumnValuesTwo.put( DEFAULT_SORT_ATTRIBUTE_NAME, null );
        RecordColumnCell recordColumnCellTwo = new RecordColumnCell( );
        recordColumnCellTwo.setRecordColumnCellValues( mapRecordColumnValuesTwo );

        RecordColumnCellComparator recordColumnCellComparator = new RecordColumnCellComparator( DEFAULT_SORT_ATTRIBUTE_NAME );

        int nComparisonResult = recordColumnCellComparator.compare( recordColumnCellOne, recordColumnCellTwo );
        assertThat( nComparisonResult, is( NumberUtils.INTEGER_ONE ) );
    }

    /**
     * Test for the method {@link DirectoryRecordItemComparator#compare(DirectoryRecordItem, DirectoryRecordItem)} with one RecordCellColumn which haven't the
     * good key
     */
    public void testCompareWithOneMissingKey( )
    {
        Map<String, Object> mapRecordColumnValuesOne = new LinkedHashMap<>( );
        mapRecordColumnValuesOne.put( "test", "Albert" );
        RecordColumnCell recordColumnCellOne = new RecordColumnCell( );
        recordColumnCellOne.setRecordColumnCellValues( mapRecordColumnValuesOne );

        Map<String, Object> mapRecordColumnValuesTwo = new LinkedHashMap<>( );
        mapRecordColumnValuesTwo.put( DEFAULT_SORT_ATTRIBUTE_NAME, "Martin" );
        RecordColumnCell recordColumnCellTwo = new RecordColumnCell( );
        recordColumnCellTwo.setRecordColumnCellValues( mapRecordColumnValuesTwo );

        RecordColumnCellComparator recordColumnCellComparator = new RecordColumnCellComparator( DEFAULT_SORT_ATTRIBUTE_NAME );

        int nComparisonResult = recordColumnCellComparator.compare( recordColumnCellOne, recordColumnCellTwo );
        assertThat( nComparisonResult, is( NumberUtils.INTEGER_MINUS_ONE ) );
    }

    /**
     * Test for the method {@link DirectoryRecordItemComparator#compare(DirectoryRecordItem, DirectoryRecordItem)} with the two RecordCellColumn which haven't
     * the good key
     */
    public void testCompareWithoutSortKey( )
    {
        Map<String, Object> mapRecordColumnValuesOne = new LinkedHashMap<>( );
        mapRecordColumnValuesOne.put( "test", "Albert" );
        RecordColumnCell recordColumnCellOne = new RecordColumnCell( );
        recordColumnCellOne.setRecordColumnCellValues( mapRecordColumnValuesOne );

        Map<String, Object> mapRecordColumnValuesTwo = new LinkedHashMap<>( );
        mapRecordColumnValuesTwo.put( "test", "Martin" );
        RecordColumnCell recordColumnCellTwo = new RecordColumnCell( );
        recordColumnCellTwo.setRecordColumnCellValues( mapRecordColumnValuesTwo );

        RecordColumnCellComparator recordColumnCellComparator = new RecordColumnCellComparator( DEFAULT_SORT_ATTRIBUTE_NAME );

        int nComparisonResult = recordColumnCellComparator.compare( recordColumnCellOne, recordColumnCellTwo );
        assertThat( nComparisonResult, is( NumberUtils.INTEGER_ZERO ) );
    }
}
