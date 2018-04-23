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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.IRecordColumnQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.RecordColumnQueryBuilder;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterQueryConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.IRecordFilterQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.IRecordPanelInitializerQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.RecordPanelInitializerQueryBuilder;

/**
 * Class use to build a SQL query from part of query inside RecordFilter and RecordColumn
 */
public final class QueryBuilder
{
    /**
     * Constructor
     */
    private QueryBuilder( )
    {

    }

    /**
     * Build a SQL query from different parts from a list of RecordColumn and a list of RecordFilter
     * 
     * @param listRecordPanelInitializerQueryPart
     *            The list of all RecordPanelInitializerQueryPart to use for built the query
     * @param listRecordColumnQueryPart
     *            The of RecordColumnQueryPart to retrieve the select and from parts of the query
     * @param listRecordFilterQueryPart
     *            The list of RecordFilterQueryPart to retrieve the where parts of the query
     * @return the global query build from the RecordColmuns and RecordFilters
     */
    public static String buildQuery( List<IRecordPanelInitializerQueryPart> listRecordPanelInitializerQueryPart,
            List<IRecordColumnQueryPart> listRecordColumnQueryPart, List<IRecordFilterQueryPart> listRecordFilterQueryPart )
    {
        StringBuilder stringBuilderGlobalQuery = new StringBuilder( );

        if ( !CollectionUtils.isEmpty( listRecordPanelInitializerQueryPart ) && !CollectionUtils.isEmpty( listRecordColumnQueryPart ) )
        {
            // Build the select query part
            buildSelectQueryPart( stringBuilderGlobalQuery, listRecordPanelInitializerQueryPart, listRecordColumnQueryPart );
            stringBuilderGlobalQuery.append( RecordFilterQueryConstants.SPACE_SEPARATOR );

            // Build the from query part
            buildFromQueryPart( stringBuilderGlobalQuery, listRecordPanelInitializerQueryPart, listRecordColumnQueryPart );
            stringBuilderGlobalQuery.append( RecordFilterQueryConstants.SPACE_SEPARATOR );

            // Build the join query part
            buildJoinQueryPart( stringBuilderGlobalQuery, listRecordPanelInitializerQueryPart, listRecordColumnQueryPart );
            stringBuilderGlobalQuery.append( RecordFilterQueryConstants.SPACE_SEPARATOR );

            // Build the where query part
            buildWhereQueryPart( stringBuilderGlobalQuery, listRecordFilterQueryPart );
        }

        return stringBuilderGlobalQuery.toString( );
    }

    /**
     * Populate the StringBuilder of the global query with the select query part
     * 
     * @param stringBuilderGlobalQuery
     *            The StringBuilder of the global query to populate
     * @param listRecordPanelInitializerQueryPart
     *            The list of all IRecordPanelInitializerQueryPart to use
     * @param listRecordColumnQueryPart
     *            The list of RecordColumnQueryPart to retrieve the select query parts from
     */
    private static void buildSelectQueryPart( StringBuilder stringBuilderGlobalQuery,
            List<IRecordPanelInitializerQueryPart> listRecordPanelInitializerQueryPart, List<IRecordColumnQueryPart> listRecordColumnQueryPart )
    {
        List<String> listSelectQueryParts = new ArrayList<>( );

        // Use the query part of the RecordPanelInitializer
        List<String> listRecordPanelInitializerSelectQueryParts = RecordPanelInitializerQueryBuilder
                .buildPanelInitializerSelectQueryParts( listRecordPanelInitializerQueryPart );
        if ( !CollectionUtils.isEmpty( listRecordPanelInitializerSelectQueryParts ) )
        {
            listSelectQueryParts.addAll( listRecordPanelInitializerSelectQueryParts );
        }

        // Use the query part of the column
        List<String> listRecordColumnSelectQueryParts = RecordColumnQueryBuilder.buildRecordColumnSelectQueryPart( listRecordColumnQueryPart );
        if ( !CollectionUtils.isEmpty( listRecordColumnSelectQueryParts ) )
        {
            listSelectQueryParts.addAll( listRecordColumnSelectQueryParts );
        }

        stringBuilderGlobalQuery.append( buildQueryPart( listSelectQueryParts, RecordFilterQueryConstants.SELECT_KEYWORD ) );
    }

    /**
     * Populate the StringBuilder of the global query with the from query part
     * 
     * @param stringBuilderGlobalQuery
     *            The StringBuilder of the global query to populate
     * @param listRecordPanelInitializerQueryPart
     *            The list of all IRecordPanelInitializerQueryPart to use
     * @param listRecordColumnQueryPart
     *            The list of RecordColumnQueryPart to retrieve the select query parts from
     */
    private static void buildFromQueryPart( StringBuilder stringBuilderGlobalQuery, List<IRecordPanelInitializerQueryPart> listRecordPanelInitializerQueryPart,
            List<IRecordColumnQueryPart> listRecordColumnQueryPart )
    {
        List<String> listFromQueryParts = new ArrayList<>( );

        // Use the query part of the RecordPanelInitializer
        List<String> listRecordPanelInitializerFromQueryPart = RecordPanelInitializerQueryBuilder
                .buildPanelInitializerFromQueryParts( listRecordPanelInitializerQueryPart );
        if ( !CollectionUtils.isEmpty( listRecordPanelInitializerFromQueryPart ) )
        {
            listFromQueryParts.addAll( listRecordPanelInitializerFromQueryPart );
        }

        // Use the query parts of the columns
        List<String> listRecordColumnFromQueryParts = RecordColumnQueryBuilder.buildRecordColumnFromQueryParts( listRecordColumnQueryPart );
        if ( !CollectionUtils.isEmpty( listRecordColumnFromQueryParts ) )
        {
            listFromQueryParts.addAll( listRecordColumnFromQueryParts );
        }

        stringBuilderGlobalQuery.append( buildQueryPart( listFromQueryParts, RecordFilterQueryConstants.FROM_KEYWORD ) );
    }

    /**
     * Populate the StringBuilder of the global query with the join query part
     * 
     * @param stringBuilderGlobalQuery
     *            The StringBuilder of the global query to populate
     * @param listRecordPanelInitializerQueryPart
     *            The list of all IRecordPanelInitializerQueryPart to use
     * @param listRecordColumnQueryPart
     *            The list of RecordColumnQueryPart to retrieve the select query parts from
     */
    private static void buildJoinQueryPart( StringBuilder stringBuilderGlobalQuery, List<IRecordPanelInitializerQueryPart> listRecordPanelInitializerQueryPart,
            List<IRecordColumnQueryPart> listRecordColumnQueryPart )
    {
        StringBuilder stringBuilderJoinQueryPart = new StringBuilder( );

        // Use the query parts of the panel filter
        if ( !CollectionUtils.isEmpty( listRecordPanelInitializerQueryPart ) )
        {
            RecordPanelInitializerQueryBuilder.buildRecordPanelInitializerJoinQueryParts( stringBuilderJoinQueryPart, listRecordPanelInitializerQueryPart );
        }

        // Use the query parts of the columns
        if ( listRecordColumnQueryPart != null && !listRecordColumnQueryPart.isEmpty( ) )
        {
            RecordColumnQueryBuilder.buildRecordColumnJoinQueryParts( stringBuilderJoinQueryPart, listRecordColumnQueryPart );
        }

        stringBuilderGlobalQuery.append( stringBuilderJoinQueryPart.toString( ) );
    }

    /**
     * Populate the StringBuilder of the global query with the where query part
     * 
     * @param stringBuilderGlobalQuery
     *            The StringBuilder of the global query to populate
     * @param listRecordFilterQueryPart
     *            The list of IRecordFilterQueryPart to retrieve the where query parts from
     */
    private static void buildWhereQueryPart( StringBuilder stringBuilderGlobalQuery, List<IRecordFilterQueryPart> listRecordFilterQueryPart )
    {
        if ( listRecordFilterQueryPart != null && !listRecordFilterQueryPart.isEmpty( ) )
        {
            stringBuilderGlobalQuery.append( RecordFilterQueryConstants.WHERE_BASE_KEYWORD ).append( RecordFilterQueryConstants.SPACE_SEPARATOR );
            manageFilterWhereQueryParts( stringBuilderGlobalQuery, listRecordFilterQueryPart );
        }
    }

    /**
     * Build a part of the query
     * 
     * @param listQueryPart
     *            The list of query part to use
     * @param strKeyWord
     *            The key word to use for the query for the current list of parts
     * @return the part of the query
     */
    private static String buildQueryPart( List<String> listQueryPart, String strKeyWord )
    {
        StringBuilder stringBuilderFromQuery = new StringBuilder( strKeyWord );
        stringBuilderFromQuery.append( RecordFilterQueryConstants.SPACE_SEPARATOR );

        Iterator<String> iteratorQueryPart = listQueryPart.iterator( );
        while ( iteratorQueryPart.hasNext( ) )
        {
            stringBuilderFromQuery.append( iteratorQueryPart.next( ) );

            if ( iteratorQueryPart.hasNext( ) )
            {
                stringBuilderFromQuery.append( RecordFilterQueryConstants.COMMA_SEPARATOR );
                stringBuilderFromQuery.append( RecordFilterQueryConstants.SPACE_SEPARATOR );
            }
        }

        return stringBuilderFromQuery.toString( );
    }

    /**
     * Populate the StringBuilder of the query with all the where query parts of the list of RecordFilter.
     * 
     * @param stringBuilderWhereQueryPart
     *            The stringBuilder of the request to populate with the where query parts of the given filters
     * @param listRecordFilterQueryPart
     *            The list of RecordFilterQueryPart to retrieve the where query parts
     */
    private static void manageFilterWhereQueryParts( StringBuilder stringBuilderWhereQueryPart, List<IRecordFilterQueryPart> listRecordFilterQueryPart )
    {
        Iterator<IRecordFilterQueryPart> iteratorRecordFilterQueryPart = listRecordFilterQueryPart.iterator( );
        while ( iteratorRecordFilterQueryPart.hasNext( ) )
        {
            IRecordFilterQueryPart recordFilterQueryPart = iteratorRecordFilterQueryPart.next( );
            addAndQueryClause( stringBuilderWhereQueryPart, recordFilterQueryPart.getRecordFilterQuery( ) );

            if ( iteratorRecordFilterQueryPart.hasNext( ) )
            {
                stringBuilderWhereQueryPart.append( RecordFilterQueryConstants.SPACE_SEPARATOR );
            }
        }
    }

    /**
     * Add and AND query part to the given StringBuilder for the specified query part. If the query is null or empty nothing will be added.
     * 
     * @param stringBuilderQuery
     *            The StringBuilder of the query to complete
     * @param strQueryPart
     *            The part of the query to added to the StringBuilder
     */
    private static void addAndQueryClause( StringBuilder stringBuilderQuery, String strQueryPart )
    {
        if ( stringBuilderQuery != null && StringUtils.isNotBlank( strQueryPart ) )
        {
            stringBuilderQuery.append( RecordFilterQueryConstants.AND_KEYWORD );
            stringBuilderQuery.append( RecordFilterQueryConstants.AND_OPEN_CLAUSE );
            stringBuilderQuery.append( strQueryPart );
            stringBuilderQuery.append( RecordFilterQueryConstants.AND_CLOSE_CLAUSE );
        }
    }
}
