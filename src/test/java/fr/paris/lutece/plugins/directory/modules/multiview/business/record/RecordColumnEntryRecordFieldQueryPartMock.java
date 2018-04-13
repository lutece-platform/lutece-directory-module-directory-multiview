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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.RecordColumnEntry;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.impl.RecordColumnEntryQueryPart;

/**
 * Mock for a RecordColumnEntryRecordFieldQueryPart
 */
public class RecordColumnEntryRecordFieldQueryPartMock extends RecordColumnEntryQueryPart
{
    // Constants
    private static final String ENTRY_RECORD_FIELD_SELECT_QUERY_PART = "column_%1$s.column_%1$s_value";
    private static final String ENTRY_RECORD_FIELD_FORM_QUERY_PART = StringUtils.EMPTY;
    private static final String ENTRY_RECORD_FIELD_JOIN_SELECT_QUERY_PART = "LEFT JOIN ( SELECT record_%1$s.id_record AS id_record_%1$s, record_field_%1$s.record_field_value AS column_%1$s_value ";
    private static final String ENTRY_RECORD_FIELD_JOIN_FROM_QUERY_PART = " FROM directory_record_field AS record_field_%s ";
    private static final String ENTRY_RECORD_FIELD_JOIN_RECORD_QUERY_PART = " INNER JOIN directory_record AS record_%1$s ON record_field_%1$s.id_record = record_%1$s.id_record ";
    private static final String ENTRY_RECORD_FIELD_JOIN_ENTRY_QUERY_PART = " INNER JOIN directory_entry AS entry_%1$s ON entry_%1$s.id_entry = record_field_%1$s.id_entry ";
    private static final String ENTRY_RECORD_FIELD_JOIN_WHERE_QUERY_PART = " WHERE entry_%1$s.title IN ( %2$s ) ";
    private static final String ENTRY_RECORD_FIELD_JOIN_QUERY_PART = " AS column_%1$s ON column_%1$s.id_record_%1$s = record.id_record";

    // Variables
    private final int _nPosition;

    /**
     * Constructor
     * 
     * @param nPosition
     *            The position to use for build the name
     */
    public RecordColumnEntryRecordFieldQueryPartMock( int nPosition )
    {
        super( );
        _nPosition = nPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordColumnSelectQuery( )
    {
        String strRecordColumnSelectQuery = String.format( ENTRY_RECORD_FIELD_SELECT_QUERY_PART, _nPosition );

        return strRecordColumnSelectQuery;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordColumnFromQuery( )
    {
        return ENTRY_RECORD_FIELD_FORM_QUERY_PART;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRecordColumnJoinQueries( )
    {
        StringBuilder stringBuilderJoinQuery = new StringBuilder( );

        String strJoinSelectQueryPart = String.format( ENTRY_RECORD_FIELD_JOIN_SELECT_QUERY_PART, _nPosition );
        stringBuilderJoinQuery.append( strJoinSelectQueryPart );

        String strJoinFromQueryPart = String.format( ENTRY_RECORD_FIELD_JOIN_FROM_QUERY_PART, _nPosition );
        stringBuilderJoinQuery.append( strJoinFromQueryPart );

        String strJoinInnerJoinRecordQueryPart = String.format( ENTRY_RECORD_FIELD_JOIN_RECORD_QUERY_PART, _nPosition );
        stringBuilderJoinQuery.append( strJoinInnerJoinRecordQueryPart );

        String strJoinInnerJoinEntryQueryPart = String.format( ENTRY_RECORD_FIELD_JOIN_ENTRY_QUERY_PART, _nPosition );
        stringBuilderJoinQuery.append( strJoinInnerJoinEntryQueryPart );

        StringBuilder stringBuilderListEntryTitle = new StringBuilder( );
        IRecordColumn recordColumn = getRecordColumn( );
        if ( recordColumn instanceof RecordColumnEntry )
        {
            RecordColumnEntry recordColumnEntryRecordField = (RecordColumnEntry) recordColumn;
            List<String> listEntryTitle = recordColumnEntryRecordField.getListEntryTitle( );
            if ( listEntryTitle != null && !listEntryTitle.isEmpty( ) )
            {
                buildListEntryTitle( stringBuilderListEntryTitle, listEntryTitle );
            }
        }

        String strJoinWhereQueryPart = String.format( ENTRY_RECORD_FIELD_JOIN_WHERE_QUERY_PART, _nPosition, stringBuilderListEntryTitle.toString( ) );
        stringBuilderJoinQuery.append( strJoinWhereQueryPart ).append( " ) " );

        String strJoinQueryPart = String.format( ENTRY_RECORD_FIELD_JOIN_QUERY_PART, _nPosition );
        stringBuilderJoinQuery.append( strJoinQueryPart );

        return Arrays.asList( stringBuilderJoinQuery.toString( ) );
    }

    /**
     * Build the list of all entry title from the given list quoting them and separate them by comma and after that append it to the given StringBuilder
     * 
     * @param stringBuilderListEntryTitle
     *            The StringBuilder to append the list of all entry title
     * @param listEntryTitle
     *            The list of entry title to manage
     */
    private void buildListEntryTitle( StringBuilder stringBuilderListEntryTitle, List<String> listEntryTitle )
    {
        Iterator<String> iteratorListEntryTitle = listEntryTitle.iterator( );
        while ( iteratorListEntryTitle.hasNext( ) )
        {
            String strEntryTitle = iteratorListEntryTitle.next( );
            stringBuilderListEntryTitle.append( '\'' ).append( strEntryTitle ).append( '\'' );

            if ( iteratorListEntryTitle.hasNext( ) )
            {
                stringBuilderListEntryTitle.append( ", " );
            }
        }
    }
}
