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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterQueryConstants;

/**
 * Query builder utility class for RecordColumn class
 */
public final class RecordColumnQueryBuilder
{
    /**
     * Constructor
     */
    private RecordColumnQueryBuilder( )
    {

    }

    /**
     * Return the list of all select query part from the given list of record columns
     * 
     * @param listRecordColumnQueryPart
     *            The list of record column query part to retrieve the query parts from
     * @return the list of all select query parts from the given list of record columns
     */
    public static List<String> buildRecordColumnSelectQueryPart( List<IRecordColumnQueryPart> listRecordColumnQueryPart )
    {
        List<String> listSelectQueryParts = new ArrayList<>( );

        if ( listRecordColumnQueryPart != null && !listRecordColumnQueryPart.isEmpty( ) )
        {
            for ( IRecordColumnQueryPart recordColumnQueryPart : listRecordColumnQueryPart )
            {
                String strRecordColumnSelectQueryPart = recordColumnQueryPart.getRecordColumnSelectQuery( );
                if ( StringUtils.isNotBlank( strRecordColumnSelectQueryPart ) )
                {
                    listSelectQueryParts.add( strRecordColumnSelectQueryPart );
                }
            }
        }

        return listSelectQueryParts;
    }

    /**
     * Return the list of all from query part from the given list of record columns
     * 
     * @param listRecordColumnQueryPart
     *            The list of record column query part to retrieve the query parts from
     * @return the list of all from query parts from the given list of record columns
     */
    public static List<String> buildRecordColumnFromQueryParts( List<IRecordColumnQueryPart> listRecordColumnQueryPart )
    {
        List<String> listRecordColumnFromQueryParts = new ArrayList<>( );

        if ( listRecordColumnQueryPart != null && !listRecordColumnQueryPart.isEmpty( ) )
        {
            for ( IRecordColumnQueryPart recordColumnQueryPart : listRecordColumnQueryPart )
            {
                String strRecordColumnFromQuery = recordColumnQueryPart.getRecordColumnFromQuery( );
                if ( StringUtils.isNotBlank( strRecordColumnFromQuery ) )
                {
                    listRecordColumnFromQueryParts.add( strRecordColumnFromQuery );
                }
            }
        }

        return listRecordColumnFromQueryParts;
    }

    /**
     * Populate the given string builder with all the join queries from all the record column from the given list
     * 
     * @param stringBuilderJoinQueryPart
     *            The string builder to populate with all the join queries
     * @param listRecordColumnQueryPart
     *            The list of all record column to retrieve the list of all join queries from
     */
    public static void buildRecordColumnJoinQueryParts( StringBuilder stringBuilderJoinQueryPart, List<IRecordColumnQueryPart> listRecordColumnQueryPart )
    {
        for ( IRecordColumnQueryPart recordColumnQueryPart : listRecordColumnQueryPart )
        {
            List<String> listRecordColumnJoinQueries = recordColumnQueryPart.getRecordColumnJoinQueries( );
            if ( listRecordColumnJoinQueries != null && !listRecordColumnJoinQueries.isEmpty( ) )
            {
                for ( String strRecordColumnJoinQuery : listRecordColumnJoinQueries )
                {
                    if ( StringUtils.isNotBlank( strRecordColumnJoinQuery ) )
                    {
                        stringBuilderJoinQueryPart.append( strRecordColumnJoinQuery );
                        stringBuilderJoinQueryPart.append( RecordFilterQueryConstants.SPACE_SEPARATOR );
                    }
                }
            }
        }
    }
}
