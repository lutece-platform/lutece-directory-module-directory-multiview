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

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.IRecordColumnQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.RecordColumnQueryBuilder;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterQueryConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.IRecordFilterQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl.standalone.IRecordFilterStandaloneQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.impl.standalone.RecordFilterStandaloneQueryBuilder;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.querypart.impl.standalone.IRecordPanelQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.querypart.impl.standalone.RecordPanelQueryBuilder;

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
     * @param listRecordColumnQueryPart
     *            The of RecordColumnQueryPart to retrieve the select and from parts of the query
     * @param listRecordFilterQueryPart
     *            The list of RecordFilterQueryPart to retrieve the where parts of the query
     * @return the global query build from the RecordColmuns and RecordFilters
     */
    public static String buildQuery( List<IRecordColumnQueryPart> listRecordColumnQueryPart, List<IRecordFilterQueryPart> listRecordFilterQueryPart )
    {
        StringBuilder stringBuilderGlobalQuery = new StringBuilder( );

        if ( listRecordColumnQueryPart != null && !listRecordColumnQueryPart.isEmpty( ) )
        {
            // Build the list of standalone record filter
            List<IRecordFilterStandaloneQueryPart> listRecordFilterQueryPartStandalone = retrieveStandaloneFilterList( listRecordFilterQueryPart );
            IRecordPanelQueryPart recordFilterPanelQueryPart = extractRecordPanelQueryPart( listRecordFilterQueryPartStandalone );

            // Build the select query part
            buildSelectQueryPart( stringBuilderGlobalQuery, listRecordColumnQueryPart, recordFilterPanelQueryPart );
            stringBuilderGlobalQuery.append( RecordFilterQueryConstants.SPACE_SEPARATOR );

            // Build the from query part
            buildFromQueryPart( stringBuilderGlobalQuery, listRecordColumnQueryPart, recordFilterPanelQueryPart );
            stringBuilderGlobalQuery.append( RecordFilterQueryConstants.SPACE_SEPARATOR );

            // Build the join query part
            buildJoinQueryPart( stringBuilderGlobalQuery, listRecordColumnQueryPart, recordFilterPanelQueryPart, listRecordFilterQueryPartStandalone );
            stringBuilderGlobalQuery.append( RecordFilterQueryConstants.SPACE_SEPARATOR );

            // Build the where query part
            buildWhereQueryPart( stringBuilderGlobalQuery, listRecordFilterQueryPart );
        }

        return stringBuilderGlobalQuery.toString( );
    }

    /**
     * Retrieve the list of standalone filter from a list of record filter
     * 
     * @param listRecordFilterQueryPart
     *            The list of record filter query part to retrieve the list of standalone filter from
     * @return the list of IRecordFilterStandaloneQueryPart from a list of record filter
     */
    private static List<IRecordFilterStandaloneQueryPart> retrieveStandaloneFilterList( List<IRecordFilterQueryPart> listRecordFilterQueryPart )
    {
        List<IRecordFilterStandaloneQueryPart> listRecordFilterQueryPartStandalone = new ArrayList<>( );

        if ( listRecordFilterQueryPart != null && !listRecordFilterQueryPart.isEmpty( ) )
        {
            for ( IRecordFilterQueryPart recordFilterQueryPart : listRecordFilterQueryPart )
            {
                if ( recordFilterQueryPart instanceof IRecordFilterStandaloneQueryPart )
                {
                    listRecordFilterQueryPartStandalone.add( (IRecordFilterStandaloneQueryPart) recordFilterQueryPart );
                }
            }
        }

        return listRecordFilterQueryPartStandalone;
    }

    /**
     * Retrieve form the list of record filter standalone query part the record filter associate to the current panel and remove it from the given list
     * 
     * @param listRecordFilterStandaloneQueryPart
     *            The list of record filter standalone to retrieve the record panel query part from
     * @return the record filter query part associated to a record panel or null if not found
     */
    private static IRecordPanelQueryPart extractRecordPanelQueryPart( List<IRecordFilterStandaloneQueryPart> listRecordFilterStandaloneQueryPart )
    {
        IRecordPanelQueryPart recordPanelQueryPart = null;

        if ( listRecordFilterStandaloneQueryPart != null && !listRecordFilterStandaloneQueryPart.isEmpty( ) )
        {
            Iterator<IRecordFilterStandaloneQueryPart> iteratorRecordFilterStandaloneQueryPart = listRecordFilterStandaloneQueryPart.iterator( );
            while ( iteratorRecordFilterStandaloneQueryPart.hasNext( ) )
            {
                IRecordFilterStandaloneQueryPart recordFilterStandaloneQueryPart = iteratorRecordFilterStandaloneQueryPart.next( );
                if ( recordFilterStandaloneQueryPart instanceof IRecordPanelQueryPart )
                {
                    recordPanelQueryPart = (IRecordPanelQueryPart) recordFilterStandaloneQueryPart;
                    iteratorRecordFilterStandaloneQueryPart.remove( );
                    break;
                }
            }
        }

        return recordPanelQueryPart;
    }

    /**
     * Populate the StringBuilder of the global query with the select query part
     * 
     * @param stringBuilderGlobalQuery
     *            The StringBuilder of the global query to populate
     * @param recordPanelQueryPart
     *            The query part associated to the current panel
     * @param listRecordColumnQueryPart
     *            The list of RecordColumnQueryPart to retrieve the select query parts from
     */
    private static void buildSelectQueryPart( StringBuilder stringBuilderGlobalQuery, List<IRecordColumnQueryPart> listRecordColumnQueryPart,
            IRecordPanelQueryPart recordPanelQueryPart )
    {
        List<String> listSelectQueryParts = new ArrayList<>( );

        // Use the query part of the panel filter
        String strRecordPanelSelectQueryPart = RecordPanelQueryBuilder.buildPanelSelectQueryPart( recordPanelQueryPart );
        if ( StringUtils.isNotBlank( strRecordPanelSelectQueryPart ) )
        {
            listSelectQueryParts.add( strRecordPanelSelectQueryPart );
        }

        // Use the query part of the column
        List<String> listRecordColumnSelectQueryParts = RecordColumnQueryBuilder.buildRecordColumnSelectQueryPart( listRecordColumnQueryPart );
        if ( listRecordColumnSelectQueryParts != null && !listRecordColumnSelectQueryParts.isEmpty( ) )
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
     * @param recordPanelQueryPart
     *            The query part associated to the current panel
     * @param listRecordColumnQueryPart
     *            The list of RecordColumnQueryPart to retrieve the select query parts from
     */
    private static void buildFromQueryPart( StringBuilder stringBuilderGlobalQuery, List<IRecordColumnQueryPart> listRecordColumnQueryPart,
            IRecordPanelQueryPart recordPanelQueryPart )
    {
        List<String> listFromQueryParts = new ArrayList<>( );

        // Use the query part of the panel filter
        String strRecordPanelFromQueryPart = RecordPanelQueryBuilder.buildPanelFromQueryPart( recordPanelQueryPart );
        if ( StringUtils.isNotBlank( strRecordPanelFromQueryPart ) )
        {
            listFromQueryParts.add( strRecordPanelFromQueryPart );
        }

        // Use the query parts of the columns
        List<String> listRecordColumnFromQueryParts = RecordColumnQueryBuilder.buildRecordColumnFromQueryParts( listRecordColumnQueryPart );
        if ( listRecordColumnFromQueryParts != null && !listRecordColumnFromQueryParts.isEmpty( ) )
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
     * @param recordPanelQueryPart
     *            The query part associated to the current panel
     * @param listRecordColumnQueryPart
     *            The list of RecordColumnQueryPart to retrieve the select query parts from
     * @param listRecordFilterQueryPartStandalone
     *            The list of IRecordFilterStandaloneQueryPart to retrieve the join query parts from
     */
    private static void buildJoinQueryPart( StringBuilder stringBuilderGlobalQuery, List<IRecordColumnQueryPart> listRecordColumnQueryPart,
            IRecordPanelQueryPart recordPanelQueryPart, List<IRecordFilterStandaloneQueryPart> listRecordFilterQueryPartStandalone )
    {
        StringBuilder stringBuilderJoinQueryPart = new StringBuilder( );

        // Use the query parts of the panel filter
        if ( recordPanelQueryPart != null )
        {
            RecordPanelQueryBuilder.buildPanelJoinQueryParts( stringBuilderJoinQueryPart, recordPanelQueryPart );
        }

        // Use the query parts of the standalone filters
        if ( listRecordFilterQueryPartStandalone != null && !listRecordFilterQueryPartStandalone.isEmpty( ) )
        {
            RecordFilterStandaloneQueryBuilder.buildRecordFilterStandaloneQueryParts( stringBuilderJoinQueryPart, listRecordFilterQueryPartStandalone );
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
