/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCell;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.RecordColumnEntry;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.impl.RecordColumnEntryQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.mock.DAOUtilMock;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Test of the implementation of the IRecordColumnQueryPart for the EntryRecordField column
 */
public class RecordColumnEntryRecordFieldQueryPartTest extends LuteceTestCase
{
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
     * Test for the {@link IRecordColumnQueryPart#getRecordColumnCell(fr.paris.lutece.util.sql.DAOUtil)}
     */
    public void testGetRecordColumnCellEntryRecordField( )
    {
        String strRecordEntryRecordFieldValueToRetrieve = "entry record field value";
        DAOUtil daoUtil = new DAOUtilMock( StringUtils.EMPTY, "column_1_value", strRecordEntryRecordFieldValueToRetrieve );

        IRecordColumn recordColumn = new RecordColumnEntry( 1, "Entry Record Field", new ArrayList<String>( ) );
        RecordColumnEntryQueryPart recordColumnEntryRecordFieldQueryPart = new RecordColumnEntryQueryPart( );
        recordColumnEntryRecordFieldQueryPart.setRecordColumn( recordColumn );

        RecordColumnCell recordColumnCell = recordColumnEntryRecordFieldQueryPart.getRecordColumnCell( daoUtil );
        assertThat( recordColumnCell, is( not( nullValue( ) ) ) );

        Map<String, Object> mapRecordColumnCellValues = recordColumnCell.getRecordColumnCellValues( );
        assertThat( mapRecordColumnCellValues, is( not( nullValue( ) ) ) );
        assertThat( mapRecordColumnCellValues.size( ), is( 1 ) );

        Object objDirectoryResult = recordColumnCell.getRecordColumnCellValueByName( "column_1_value" );
        assertThat( objDirectoryResult, is( not( nullValue( ) ) ) );
        assertThat( String.valueOf( objDirectoryResult ), is( strRecordEntryRecordFieldValueToRetrieve ) );
    }

    /**
     * Test for the {@link IRecordColumnQueryPart#getRecordColumnCell(fr.paris.lutece.util.sql.DAOUtil)} using a column that doesn't exist
     */
    public void testGetRecordColumnCellEntryRecordFieldWithWrongColumnName( )
    {
        String strRecordEntryRecordFieldValueToRetrieve = "entry record field value";
        DAOUtil daoUtil = new DAOUtilMock( StringUtils.EMPTY, "colonne", strRecordEntryRecordFieldValueToRetrieve );

        IRecordColumn recordColumn = new RecordColumnEntry( 1, "Entry Record Field", new ArrayList<String>( ) );
        RecordColumnEntryQueryPart recordColumnEntryRecordFieldQueryPart = new RecordColumnEntryQueryPart( );
        recordColumnEntryRecordFieldQueryPart.setRecordColumn( recordColumn );

        try
        {
            recordColumnEntryRecordFieldQueryPart.getRecordColumnCell( daoUtil );
            fail( "Test fail : AppException hasn't been thrown !" );
        }
        catch( AppException exception )
        {

        }
    }
    
    /**
     * Test for the {@link RecordColumnEntryQueryPart#getRecordColumnJoinQueries()} method with a column which have no Entry title
     */
    public void testGetRecordColumnJoinQueriesWithoutEntryTitle( )
    {
        String strJoinQueryExpected = "LEFT JOIN ( SELECT record_1.id_record AS id_record_1, record_field_1.record_field_value AS "
                + "column_1_value FROM directory_record_field AS record_field_1 INNER JOIN directory_record AS record_1 ON "
                + "record_field_1.id_record = record_1.id_record INNER JOIN directory_entry AS entry_1 ON entry_1.id_entry = "
                + "record_field_1.id_entry WHERE entry_1.title IN ( ) ) AS column_1 ON column_1.id_record_1 = record.id_record";
        
        IRecordColumn recordColumn = new RecordColumnEntry( 1, "Titre", new ArrayList<>( ) );
        RecordColumnEntryQueryPart recordColumnEntryQueryPart = new RecordColumnEntryQueryPart( );
        recordColumnEntryQueryPart.setRecordColumn( recordColumn );
        
        List<String> listRecordColumnJoinQueries = recordColumnEntryQueryPart.getRecordColumnJoinQueries( );
        assertThat( listRecordColumnJoinQueries, is( not( nullValue( ) ) ) );
        assertThat( listRecordColumnJoinQueries.size( ), is( 1 ) );
        
        String strRecordColumnJoinQuery = removeQuerySpaces( listRecordColumnJoinQueries.get( 0 ) );
        assertThat( strRecordColumnJoinQuery, is( strJoinQueryExpected ) );
    }
    
    /**
     * Test for the {@link RecordColumnEntryQueryPart#getRecordColumnJoinQueries()} method with a column which have no Entry title
     * and no position
     */
    public void testGetRecordColumnJoinQueriesWithoutEntryTitleAndColumnPosition( )
    {
        String strJoinQueryExpected = "LEFT JOIN ( SELECT record_-1.id_record AS id_record_-1, record_field_-1.record_field_value AS "
                + "column_-1_value FROM directory_record_field AS record_field_-1 INNER JOIN directory_record AS record_-1 ON "
                + "record_field_-1.id_record = record_-1.id_record INNER JOIN directory_entry AS entry_-1 ON entry_-1.id_entry = "
                + "record_field_-1.id_entry WHERE entry_-1.title IN ( ) ) AS column_-1 ON column_-1.id_record_-1 = record.id_record";
        
        RecordColumnEntryQueryPart recordColumnEntryQueryPart = new RecordColumnEntryQueryPart( );
        
        List<String> listRecordColumnJoinQueries = recordColumnEntryQueryPart.getRecordColumnJoinQueries( );
        assertThat( listRecordColumnJoinQueries, is( not( nullValue( ) ) ) );
        assertThat( listRecordColumnJoinQueries.size( ), is( 1 ) );
        
        String strRecordColumnJoinQuery = removeQuerySpaces( listRecordColumnJoinQueries.get( 0 ) );
        assertThat( strRecordColumnJoinQuery, is( strJoinQueryExpected ) );
    }
    
    /**
     * Test for the {@link RecordColumnEntryQueryPart#getRecordColumnJoinQueries()} method
     */
    public void testGetRecordColumnJoinQueries( )
    {
        String strJoinQueryExpected = "LEFT JOIN ( SELECT record_5.id_record AS id_record_5, record_field_5.record_field_value AS "
                + "column_5_value FROM directory_record_field AS record_field_5 INNER JOIN directory_record AS record_5 ON "
                + "record_field_5.id_record = record_5.id_record INNER JOIN directory_entry AS entry_5 ON entry_5.id_entry = "
                + "record_field_5.id_entry WHERE entry_5.title IN ( 'FirstName', 'LastName' ) ) AS column_5 ON column_5.id_record_5 "
                + "= record.id_record";
        
        List<String> listEntryTitle = Arrays.asList( "FirstName", "LastName" );
        IRecordColumn recordColumn = new RecordColumnEntry( 5, "Titre", listEntryTitle );
        
        RecordColumnEntryQueryPart recordColumnEntryQueryPart = new RecordColumnEntryQueryPart( );
        recordColumnEntryQueryPart.setRecordColumn( recordColumn );
        
        List<String> listRecordColumnJoinQueries = recordColumnEntryQueryPart.getRecordColumnJoinQueries( );
        assertThat( listRecordColumnJoinQueries, is( not( nullValue( ) ) ) );
        assertThat( listRecordColumnJoinQueries.size( ), is( 1 ) );
        
        String strRecordColumnJoinQuery = removeQuerySpaces( listRecordColumnJoinQueries.get( 0 ) );
        assertThat( strRecordColumnJoinQuery, is( strJoinQueryExpected ) );
    }
    
    /**
     * Remove all the unnecessary spaces of a query
     * 
     * @param strQuery
     *            The query to remove the spaces
     * @return the given query without all unnecessary spaces
     */
    private String removeQuerySpaces( String strQuery )
    {
        String strQueryResult = StringUtils.EMPTY;

        if ( StringUtils.isNotBlank( strQuery ) )
        {
            strQueryResult = strQuery.trim( ).replaceAll( " +", " " );
            strQueryResult = strQueryResult.replaceAll( " +,", "," );
        }

        return strQueryResult;
    }
}
