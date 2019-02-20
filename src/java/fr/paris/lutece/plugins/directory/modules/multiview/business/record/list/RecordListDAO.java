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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.QueryBuilder;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.RecordParameters;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCell;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.IRecordColumnQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.RecordColumnQueryPartFacade;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.IRecordFilterQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.RecordFilterQueryPartFacade;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.configuration.RecordPanelConfiguration;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.IRecordPanelInitializerQueryPart;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.RecordPanelInitializerQueryPartFacade;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Implementation of the IRecordListDAO interface
 */
public class RecordListDAO implements IRecordListDAO
{
    // Constants
    private static final String ID_RECORD_COLUMN_NAME = "id_record";
    private static final String ID_DIRECTORY_COLUMN_NAME = "id_directory";

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateRecordColumns( IRecordPanel recordPanel, List<IRecordColumn> listRecordColumn, List<IRecordFilter> listRecordFilter )
    {
        // To retrieve the values to display on the table we must have a RecordPanel and a list of RecordColumn
        if ( recordPanel == null || CollectionUtils.isEmpty( listRecordColumn ) )
        {
            return;
        }

        // Create the list of all values of the parameter to used
        List<String> listQueryParametersPositionValue = new ArrayList<>( );

        // Build the list of query part from the recordPanel, the list of columns and the list of filters
        List<IRecordPanelInitializerQueryPart> listRecordPanelInitializerQueryPart = buildRecordPanelInitializerQueryPartList( recordPanel,
                listQueryParametersPositionValue );
        List<IRecordColumnQueryPart> listRecordColumnQueryPart = buildRecordColumnQueryPartList( listRecordColumn );
        List<IRecordFilterQueryPart> listRecordFilterQueryPart = buildRecordFilterQueryPartList( listRecordFilter, listQueryParametersPositionValue );

        // Build the query to execute
        String strQuery = QueryBuilder.buildQuery( listRecordPanelInitializerQueryPart, listRecordColumnQueryPart, listRecordFilterQueryPart );

        List<DirectoryRecordItem> listDirectoryRecordItem = new ArrayList<>( );

        if ( StringUtils.isNotBlank( strQuery ) )
        {
            DAOUtil daoUtil = new DAOUtil( strQuery );
            fillDAOUtilParameterValues( daoUtil, listQueryParametersPositionValue );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                // Create a DirectoryRecordItem for the current result line
                DirectoryRecordItem directoryRecordItem = createDirectoryItem( daoUtil );
                listDirectoryRecordItem.add( directoryRecordItem );

                for ( IRecordColumnQueryPart recordColumnQueryPart : listRecordColumnQueryPart )
                {
                    RecordColumnCell recordColumnCell = recordColumnQueryPart.getRecordColumnCell( daoUtil );
                    directoryRecordItem.addRecordColumnCell( recordColumnCell );
                }
            }

            daoUtil.close( );
        }
        
        recordPanel.setDirectoryRecordItemList( listDirectoryRecordItem );
    }

    /**
     * Fill the values to used to prepare the query of the given DAOUtil
     * 
     * @param daoUtil
     *            The DAOUtil on which the values must be set to prepare the query
     * @param listQueryParametersPositionValue
     *            The list of all parameter values to used to fill the given DAOUtil statement
     */
    private void fillDAOUtilParameterValues( DAOUtil daoUtil, List<String> listQueryParametersPositionValue )
    {
        if ( !CollectionUtils.isEmpty( listQueryParametersPositionValue ) )
        {
            int nIndex = 1;
            for ( String strParameterValue : listQueryParametersPositionValue )
            {
                try
                {
                    int nParameterValue = Integer.parseInt( strParameterValue );
                    daoUtil.setInt( nIndex++, nParameterValue );
                }
                catch( NumberFormatException exception )
                {
                    daoUtil.setString( nIndex++, strParameterValue );
                }
            }
        }
    }

    /**
     * Create a DirectoryItem from a DAOUtil
     * 
     * @param daoUtil
     *            The daoUtil to retrieve the values of the request from
     * @return the created DirectoryRecordItem
     */
    private DirectoryRecordItem createDirectoryItem( DAOUtil daoUtil )
    {
        DirectoryRecordItem directoryRecordItem = new DirectoryRecordItem( );
        directoryRecordItem.setIdRecord( daoUtil.getInt( ID_RECORD_COLUMN_NAME ) );
        directoryRecordItem.setIdDirectory( daoUtil.getInt( ID_DIRECTORY_COLUMN_NAME ) );

        return directoryRecordItem;
    }

    /**
     * Build the list of all RecordPanelInitializerQueryPart associate to all the RecordPanelInitializer to retrieve from the given RecordPanel
     * 
     * @param recordPanel
     *            The RecordPanel used to retrieve the list of all RecordPanelInitializer to retrieve the list of RecordPanelInitializerQueryPart
     * @param listQueryParametersPositionValue
     *            The list of all parameter values to used to fill the DAOUtil statement
     * @return the list of all RecordPanelInitializerQueryPart associate to all the RecordPanelInitializer to retrieve from the given RecordPanel
     */
    private static List<IRecordPanelInitializerQueryPart> buildRecordPanelInitializerQueryPartList( IRecordPanel recordPanel,
            List<String> listQueryParametersPositionValue )
    {
        List<IRecordPanelInitializerQueryPart> listRecordPanelInitializerQueryPart = new ArrayList<>( );

        RecordPanelConfiguration recordPanelConfiguration = recordPanel.getRecordPanelConfiguration( );

        if ( recordPanelConfiguration != null && !CollectionUtils.isEmpty( recordPanelConfiguration.getListRecordPanelInitializer( ) ) )
        {
            List<IRecordPanelInitializer> listRecordPanelInitializer = recordPanelConfiguration.getListRecordPanelInitializer( );

            for ( IRecordPanelInitializer recordPanelInitializer : listRecordPanelInitializer )
            {
                IRecordPanelInitializerQueryPart recordPanelInitializerQueryPart = retrieveRecordPanelInitializerQueryPart( recordPanelInitializer,
                        listQueryParametersPositionValue );
                if ( recordPanelInitializerQueryPart != null )
                {
                    listRecordPanelInitializerQueryPart.add( recordPanelInitializerQueryPart );
                }
            }
        }

        return listRecordPanelInitializerQueryPart;
    }

    /**
     * Retrieve the IRecordPanelInitializerQueryPart associate to the given RecordPanelInitializer
     * 
     * @param recordPanelInitializer
     *            The RecordPanelInitializer used to retrieve the associated IRecordPanelInitializerQueryPart
     * @param listQueryParametersPositionValue
     *            The list of all parameter values to used to fill the DAOUtil statement
     * @return the IRecordPanelInitializerQueryPart associate to the given RecordPanelInitializer or null if not found
     */
    private static IRecordPanelInitializerQueryPart retrieveRecordPanelInitializerQueryPart( IRecordPanelInitializer recordPanelInitializer,
            List<String> listQueryParametersPositionValue )
    {
        IRecordPanelInitializerQueryPart recordPanelInitializerQueryPartResult = null;

        if ( recordPanelInitializer != null )
        {
            recordPanelInitializerQueryPartResult = new RecordPanelInitializerQueryPartFacade( ).getRecordPanelInitializerQueryPart( recordPanelInitializer );

            if ( recordPanelInitializerQueryPartResult != null )
            {
                RecordParameters recordParameters = recordPanelInitializer.getRecordParameters( );
                recordPanelInitializerQueryPartResult.buildRecordPanelInitializerQuery( recordParameters );

                List<String> listUsedParametersValues = recordParameters.getListUsedParametersValue( );
                listQueryParametersPositionValue.addAll( listUsedParametersValues );
            }
        }

        return recordPanelInitializerQueryPartResult;
    }

    /**
     * Build the list of IRecordColumnQueryPart to use for build the global query from the given list of IRecordColumn
     * 
     * @param listRecordColumn
     *            The list of IRecordColumn to use for retrieving the list of IRecordColumnQueryPart
     * @return the list of IRecordColumnQueryPart to use for build the global query
     */
    private static List<IRecordColumnQueryPart> buildRecordColumnQueryPartList( List<IRecordColumn> listRecordColumn )
    {
        List<IRecordColumnQueryPart> listRecordColumnQueryPart = new ArrayList<>( );

        if ( listRecordColumn != null && !listRecordColumn.isEmpty( ) )
        {
            for ( IRecordColumn recordColumn : listRecordColumn )
            {
                IRecordColumnQueryPart recordColumnQueryPart = retrieveRecordColumnQueryPart( recordColumn );
                if ( recordColumnQueryPart != null )
                {
                    recordColumnQueryPart.setRecordColumn( recordColumn );
                    listRecordColumnQueryPart.add( recordColumnQueryPart );
                }
            }
        }

        return listRecordColumnQueryPart;
    }

    /**
     * Return the record column query part for the given record column
     * 
     * @param recordColumn
     *            The RecordColumn to build the queries from
     * @return the record column query part associated to the given record column
     */
    private static IRecordColumnQueryPart retrieveRecordColumnQueryPart( IRecordColumn recordColumn )
    {
        IRecordColumnQueryPart recordColumnQueryPartResult = null;

        if ( recordColumn != null )
        {
            recordColumnQueryPartResult = new RecordColumnQueryPartFacade( ).getRecordFilterQueryPart( recordColumn );
        }

        return recordColumnQueryPartResult;
    }

    /**
     * Build the list of record filter query part for the given list of record filter
     * 
     * @param listRecordFilter
     *            The list of record filter to build the list of record filter query part from
     * @param listQueryParametersPositionValue
     *            The list of all parameter values to used to fill the DAOUtil statement
     * @return the list of record filter query part from the given list of record filter
     */
    private static List<IRecordFilterQueryPart> buildRecordFilterQueryPartList( List<IRecordFilter> listRecordFilter,
            List<String> listQueryParametersPositionValue )
    {
        List<IRecordFilterQueryPart> listRecordFilterQueryPart = new ArrayList<>( );

        if ( listRecordFilter != null && !listRecordFilter.isEmpty( ) )
        {
            for ( IRecordFilter recordFilter : listRecordFilter )
            {
                IRecordFilterQueryPart recordFilterQueryPart = retrieveRecordFilterQueryPart( recordFilter, listQueryParametersPositionValue );
                if ( recordFilterQueryPart != null )
                {
                    listRecordFilterQueryPart.add( recordFilterQueryPart );
                }
            }
        }

        return listRecordFilterQueryPart;
    }

    /**
     * Retrieve the query part for the given filter
     * 
     * @param recordFilter
     *            The RecordFilter to build the query
     * @param listQueryParametersPositionValue
     *            The list of all parameter values to used to fill the DAOUtil statement
     * @return the record filter query part associated to the given record filter
     */
    private static IRecordFilterQueryPart retrieveRecordFilterQueryPart( IRecordFilter recordFilter, List<String> listQueryParametersPositionValue )
    {
        IRecordFilterQueryPart recordFilterQueryPartResult = null;

        if ( recordFilter != null )
        {
            IRecordFilterQueryPart recordFilterQueryPart = new RecordFilterQueryPartFacade( ).getRecordFilterQueryPart( recordFilter );
            if ( recordFilterQueryPart != null )
            {
                RecordParameters recordParameters = recordFilter.getRecordParameters( );
                recordFilterQueryPart.buildRecordFilterQuery( recordParameters );
                recordFilterQueryPartResult = recordFilterQueryPart;

                List<String> listUsedParametersValue = recordParameters.getListUsedParametersValue( );
                listQueryParametersPositionValue.addAll( listUsedParametersValue );
            }
        }

        return recordFilterQueryPartResult;
    }
}
