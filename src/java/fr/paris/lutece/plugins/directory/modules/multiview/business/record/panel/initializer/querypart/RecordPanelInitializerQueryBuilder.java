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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterQueryConstants;

/**
 * Query builder utility class for RecordFilterPanel
 */
public final class RecordPanelInitializerQueryBuilder
{
    /**
     * Constructor
     */
    private RecordPanelInitializerQueryBuilder( )
    {

    }

    /**
     * Build the select query part of the RecordPanelInitializer
     * 
     * @param listRecordPanelInitializerQueryPart
     *            The list of RecordPanelInitializerQueryPart to use
     * @return the list of all select query part of the given list of IRecordPanelInitializerQueryPart
     */
    public static List<String> buildPanelInitializerSelectQueryParts( List<IRecordPanelInitializerQueryPart> listRecordPanelInitializerQueryPart )
    {
        List<String> listPanelInitializerSelectQueryParts = new ArrayList<>( );

        if ( !CollectionUtils.isEmpty( listRecordPanelInitializerQueryPart ) )
        {
            for ( IRecordPanelInitializerQueryPart recordPanelInitializerQueryPart : listRecordPanelInitializerQueryPart )
            {
                String strRecordPanelInitializerSelectQueryPart = recordPanelInitializerQueryPart.getRecordPanelInitializerSelectQuery( );
                if ( StringUtils.isNotBlank( strRecordPanelInitializerSelectQueryPart ) )
                {
                    listPanelInitializerSelectQueryParts.add( strRecordPanelInitializerSelectQueryPart );
                }
            }
        }

        return listPanelInitializerSelectQueryParts;
    }

    /**
     * Build the from query part of the RecordPanelInitializer
     * 
     * @param listRecordPanelInitializerQueryPart
     *            The list of RecordPanelInitializerQueryPart to use
     * @return the list of all from query part of the given list of IRecordPanelInitializerQueryPart
     */
    public static List<String> buildPanelInitializerFromQueryParts( List<IRecordPanelInitializerQueryPart> listRecordPanelInitializerQueryPart )
    {
        List<String> listPanelInitializerFromQueryParts = new ArrayList<>( );

        if ( !CollectionUtils.isEmpty( listRecordPanelInitializerQueryPart ) )
        {
            for ( IRecordPanelInitializerQueryPart recordPanelInitializerQueryPart : listRecordPanelInitializerQueryPart )
            {
                String strRecordPanelInitializerFromQueryPart = recordPanelInitializerQueryPart.getRecordPanelInitializerFromQuery( );
                if ( StringUtils.isNotBlank( strRecordPanelInitializerFromQueryPart ) )
                {
                    listPanelInitializerFromQueryParts.add( strRecordPanelInitializerFromQueryPart );
                }
            }
        }

        return listPanelInitializerFromQueryParts;
    }

    /**
     * Populate the given string builder with all the join queries from the given RecordPanelInitializer list
     * 
     * @param stringBuilderJoinQueryPart
     *            The string builder to populate with all join queries parts of the RecordPanelInitializer
     * @param listRecordPanelInitializerQueryPart
     *            The list of all RecordPanelInitializerQueryPart to retrieve the list of join query parts from
     */
    public static void buildRecordPanelInitializerJoinQueryParts( StringBuilder stringBuilderJoinQueryPart,
            List<IRecordPanelInitializerQueryPart> listRecordPanelInitializerQueryPart )
    {
        for ( IRecordPanelInitializerQueryPart recordPanelInitializerQueryPart : listRecordPanelInitializerQueryPart )
        {
            List<String> listRecordPanelInitializerJoinQueries = recordPanelInitializerQueryPart.getRecordPanelInitializerJoinQueries( );
            if ( listRecordPanelInitializerJoinQueries != null && !listRecordPanelInitializerJoinQueries.isEmpty( ) )
            {
                for ( String strRecordPanelInitializerJoinQuery : listRecordPanelInitializerJoinQueries )
                {
                    if ( StringUtils.isNotBlank( strRecordPanelInitializerJoinQuery ) )
                    {
                        stringBuilderJoinQueryPart.append( strRecordPanelInitializerJoinQuery );
                        stringBuilderJoinQueryPart.append( RecordFilterQueryConstants.SPACE_SEPARATOR );
                    }
                }
            }
        }
    }
}
