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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryFilter;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.RecordColumnEntry;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.configuration.RecordFilterConfiguration;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.configuration.RecordFilterEntryConfiguration;
import fr.paris.lutece.plugins.directory.modules.multiview.service.DirectoryMultiviewPlugin;
import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordEntryNameConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.util.ReferenceListFactory;
import fr.paris.lutece.util.ReferenceList;

/**
 * Implementation of the IRecordFilterDisplay interface for the filter on the Entry column
 */
public class RecordFilterDisplayEntry extends AbstractRecordFilterDisplay
{
    // Constants
    private static final String DEFAULT_ENTRY_VALUE = StringUtils.EMPTY;
    private static final String PARAMETER_ENTRY_VALUE_PATTERN = "multiview_entry_value_%s";
    private static final String ENTRY_VALUE_ATTRIBUTE = "value";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterName( )
    {
        return buildElementName( PARAMETER_ENTRY_VALUE_PATTERN );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getFilterDisplayMapValues( HttpServletRequest request )
    {
        String strEntryValue = DEFAULT_ENTRY_VALUE;
        Map<String, Object> mapFilterNameValues = new LinkedHashMap<>( );

        String strParameterName = buildElementName( PARAMETER_ENTRY_VALUE_PATTERN );
        String strEntryParameterValue = request.getParameter( strParameterName );
        if ( StringUtils.isNotBlank( strEntryParameterValue ) )
        {
            int nRecordColumnPosition = NumberUtils.INTEGER_MINUS_ONE;
            IRecordColumn recordColumn = retrieveRecordColumn( );
            if ( recordColumn != null )
            {
                nRecordColumnPosition = recordColumn.getRecordColumnPosition( );
            }

            String strEntryValueColumnName = RecordEntryNameConstants.FILTER_ENTRY_BASE_NAME_PATTERN + nRecordColumnPosition;
            mapFilterNameValues.put( strEntryValueColumnName, strEntryParameterValue );
            strEntryValue = strEntryParameterValue;
        }

        setValue( strEntryValue );

        return mapFilterNameValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildTemplate( HttpServletRequest request )
    {
        String strParameterName = buildElementName( PARAMETER_ENTRY_VALUE_PATTERN );
        manageFilterTemplate( request, createReferenceList( request ), strParameterName );
    }

    /**
     * Create the ReferenceList based on the value of the Entry for an Entry column
     * 
     * @return the ReferenceList with all values of the Entry for an Entry column
     */
    private ReferenceList createReferenceList( HttpServletRequest request )
    {
        List<IEntry> listIEntryToRetrieveValueFrom = new ArrayList<>( );

        IRecordColumn recordColumn = retrieveRecordColumn( );
        if ( recordColumn instanceof RecordColumnEntry )
        {
            RecordColumnEntry recordColumnEntry = (RecordColumnEntry) recordColumn;
            List<String> listEntryTitle = recordColumnEntry.getListEntryTitle( );
            listIEntryToRetrieveValueFrom = getEntryListFromTitle( listEntryTitle );
        }

        // Build the list of RecordFilter to use for the filter from the list of entry to search on
        List<RecordField> listRecordField = getRecordFieldList( listIEntryToRetrieveValueFrom );
        cleanListRecordField( listRecordField );
        filterListRecordField( listRecordField, request );
        ReferenceListFactory referenceListFactory = new ReferenceListFactory( listRecordField, ENTRY_VALUE_ATTRIBUTE, ENTRY_VALUE_ATTRIBUTE, Boolean.FALSE );

        String strDefaultReferenceListName = getRecordFilterDisplayLabel( );
        referenceListFactory.setDefaultName( strDefaultReferenceListName );
        referenceListFactory.setDefaultSortNeeded( Boolean.TRUE );

        return referenceListFactory.createReferenceList( );
    }

    /**
     * Return the list of entry built from the name of the entry which are stored in configuration of the column
     * 
     * @param listEntryTitle
     *            The list of title of entry to retrieve the value from
     * @return the list of entry built from the given list of entry title
     */
    private static List<IEntry> getEntryListFromTitle( List<String> listEntryTitle )
    {
        List<IEntry> listEntry = new ArrayList<>( );

        if ( listEntryTitle != null && !listEntryTitle.isEmpty( ) )
        {
            // Retrieve the list of directory
            DirectoryFilter directoryFilter = new DirectoryFilter( );
            List<Directory> listDirectory = DirectoryHome.getDirectoryList( directoryFilter, DirectoryMultiviewPlugin.getPlugin( ) );

            if ( listDirectory != null && !listDirectory.isEmpty( ) )
            {
                for ( Directory directory : listDirectory )
                {
                    listEntry.addAll( fillEntryListFromTitle( directory.getIdDirectory( ), listEntryTitle ) );
                }
            }
        }

        return listEntry;
    }

    /**
     * Return the list of all the IEntry of the specified directory which have the same title than the value in the given list.
     * 
     * @param nIdDirectory
     *            The identifier of the directory to retrieve the IEntry from
     * @param listEntryTitle
     *            The list of title of entry to retrieve the value from
     * @return the list of entry retrieve from the directory from the given list of entry title
     */
    private static List<IEntry> fillEntryListFromTitle( int nIdDirectory, List<String> listEntryTitle )
    {
        List<IEntry> listIEntry = new ArrayList<>( );

        EntryFilter entryFilter = new EntryFilter( );
        entryFilter.setIdDirectory( nIdDirectory );
        entryFilter.setIsGroup( EntryFilter.FILTER_FALSE );
        entryFilter.setIsComment( EntryFilter.FILTER_FALSE );

        List<IEntry> entryList = EntryHome.getEntryList( entryFilter, DirectoryMultiviewPlugin.getPlugin( ) );

        for ( String strEntryTitleToFind : listEntryTitle )
        {
            for ( IEntry entry : entryList )
            {
                if ( strEntryTitleToFind.equals( entry.getTitle( ) ) )
                {
                    listIEntry.add( entry );
                }
            }
        }

        return listIEntry;
    }

    /**
     * Return the list of RecordField which are associated to the specified entry of the given list
     * 
     * @param listIEntryToRetrieveValueFrom
     *            The list of entry to retrieve the record field value from
     * @return the list of RecordField which belong to an entry of the list of Entry which we want to gather values
     */
    private static List<RecordField> getRecordFieldList( List<IEntry> listIEntryToRetrieveValueFrom )
    {
        List<RecordField> listRecordFieldResult = new ArrayList<>( );

        if ( listIEntryToRetrieveValueFrom != null && !listIEntryToRetrieveValueFrom.isEmpty( ) )
        {
            for ( IEntry entry : listIEntryToRetrieveValueFrom )
            {
                RecordFieldFilter recordFieldFilter = new RecordFieldFilter( );
                recordFieldFilter.setIdEntry( entry.getIdEntry( ) );

                listRecordFieldResult.addAll( RecordFieldHome.getRecordFieldList( recordFieldFilter, DirectoryMultiviewPlugin.getPlugin( ) ) );
            }
        }

        return listRecordFieldResult;
    }

    /**
     * Clean the given list of RecordField by removing all of those which have empty value
     * 
     * @param listRecordField
     *            The list to remove the RecordField with empty value
     */
    private static void cleanListRecordField( List<RecordField> listRecordField )
    {
        if ( !CollectionUtils.isEmpty( listRecordField ) )
        {
            Iterator<RecordField> iteratorRecordField = listRecordField.iterator( );
            while ( iteratorRecordField.hasNext( ) )
            {
                RecordField recordField = iteratorRecordField.next( );
                if ( recordField != null && StringUtils.isBlank( recordField.getValue( ) ) )
                {
                    iteratorRecordField.remove( );
                }
            }
        }
    }

    /**
     * Return the name of the element build from the given pattern and the position of the filter
     * 
     * @param strPatternElementName
     *            The pattern of the name of the element
     * @return the property key name for the current EntryRecordField filter for the entry to map
     */
    private String buildElementName( String strPatternElementName )
    {
        int nPosition = NumberUtils.INTEGER_MINUS_ONE;

        IRecordFilter recordFilter = getRecordFilter( );
        if ( recordFilter != null && recordFilter.getRecordFilterConfiguration( ) != null )
        {
            nPosition = recordFilter.getRecordFilterConfiguration( ).getPosition( );
        }

        return String.format( strPatternElementName, nPosition );
    }

    /**
     * Return the IRecordColumn from the RecordFilterConfiguration of the RecordFilter of the RecordFilterDisplay
     * 
     * @return the IRecordColumn from the RecordFilterConfiguration of the RecordFilter of the RecordFilterDisplay or null if not exist
     */
    private IRecordColumn retrieveRecordColumn( )
    {
        IRecordColumn recordColumnResult = null;

        IRecordFilter recordFilter = getRecordFilter( );
        if ( recordFilter != null )
        {
            RecordFilterConfiguration recordFilterConfiguration = recordFilter.getRecordFilterConfiguration( );
            if ( recordFilterConfiguration instanceof RecordFilterEntryConfiguration )
            {
                RecordFilterEntryConfiguration recordFilterEntryRecordFieldConfiguration = (RecordFilterEntryConfiguration) recordFilterConfiguration;
                recordColumnResult = recordFilterEntryRecordFieldConfiguration.getRecordColumn( );
            }
        }

        return recordColumnResult;
    }
    
    /**
     * Process a filtration of the record field; must be overrided
     * @param listRecordField
     *                  The list of recordField
     * @param request
     */
    protected void filterListRecordField( List<RecordField> listRecordField, HttpServletRequest request )
    {
        //Do nothing
    }
}
