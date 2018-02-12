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
package fr.paris.lutece.plugins.directory.modules.multiview.service.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.directory.web.action.DirectoryAdminSearchFields;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * Implementation of the module-directory-multiview search service
 */
public class DirectoryMultiviewSearchService implements IDirectoryMultiviewSearchService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, RecordAssignment> filterBySearchedText( Map<String, RecordAssignment> mapRecordAssignment, Collection<Directory> listDirectories,
            AdminUser adminUser, Plugin plugin, String strSearchText, Locale locale )
    {
        // No search text
        if ( StringUtils.isBlank( strSearchText ) )
        {
            return mapRecordAssignment;
        }
        else
        {
            Map<String, RecordAssignment> mapReturn = new LinkedHashMap<>( mapRecordAssignment );
            List<String> listIdRecord = new ArrayList<>( );

            // For each directory, compute the plain text search;
            for ( Directory directory : listDirectories )
            {
                // Create the list of entry to search on
                List<IEntry> listEntry = retrieveDirectoryListEntry( directory, plugin );

                // Create the query map
                HashMap<String, List<RecordField>> mapSearchRecordField = createMapSearchRecordField( listEntry, strSearchText );

                // If the map contains entry to search on we will make the search
                if ( !mapSearchRecordField.isEmpty( ) )
                {
                    populateIdRecordResultSearchList( adminUser, listIdRecord, directory, mapSearchRecordField, locale );
                }
            }

            // Keep only the record which are contains in the result list
            mapReturn.keySet( ).retainAll( listIdRecord );

            return mapReturn;
        }
    }

    /**
     * Retrieve the list of all entry to search on for a given directory
     * 
     * @param directory
     *            The directory to retrieve the entry from
     * @param plugin
     *            The plugin used for the search
     * @return the list of entry to search on for the given directory
     */
    private List<IEntry> retrieveDirectoryListEntry( Directory directory, Plugin plugin )
    {
        // Set the operator OR for search
        directory.setSearchOperatorOr( true );

        // Compute the search fields
        EntryFilter filter = new EntryFilter( );
        filter.setIdDirectory( directory.getIdDirectory( ) );
        List<IEntry> listParentEntry = DirectoryUtils.getFormEntriesByFilter( filter, plugin );

        // Return the list of all children entry
        return getListOfAllChildren( listParentEntry );
    }

    /**
     * Return the list of all entry which are not belong to a group and of all children of entry of type group.
     * 
     * @param listEntry
     *            The list of entry of type group or which are not belong to a group
     * @return the list of all children of all given entry of type group and all entry which are not belong to a group
     */
    private List<IEntry> getListOfAllChildren( List<IEntry> listEntry )
    {
        List<IEntry> listEntryResult = new ArrayList<>( );

        if ( listEntry != null && !listEntry.isEmpty( ) )
        {
            for ( IEntry entry : listEntry )
            {
                List<IEntry> listChildrenEntry = entry.getChildren( );
                if ( listChildrenEntry != null && !listChildrenEntry.isEmpty( ) )
                {
                    listEntryResult.addAll( listChildrenEntry );
                }
                else
                {
                    listEntryResult.add( entry );
                }
            }
        }

        return listEntryResult;
    }

    /**
     * Create the map of all RecordField with the specified text to search for each entry of the given list which are indexed as summary.
     * 
     * @param listEntry
     *            The list of entry to search on
     * @param strSearchText
     *            The text to find as record field value
     * @return the map which contains all RecordField with the given search text as value for each indexed entry for the specified list
     */
    private HashMap<String, List<RecordField>> createMapSearchRecordField( List<IEntry> listEntry, String strSearchText )
    {
        HashMap<String, List<RecordField>> mapSearchRecordField = new HashMap<>( );

        if ( listEntry != null && !listEntry.isEmpty( ) )
        {
            for ( IEntry entry : listEntry )
            {
                if ( entry.isIndexedAsSummary( ) )
                {
                    List<RecordField> listRecordFields = new ArrayList<>( );
                    RecordField recordField = new RecordField( );
                    recordField.setEntry( entry );
                    recordField.setValue( strSearchText );
                    listRecordFields.add( recordField );
                    mapSearchRecordField.put( Integer.toString( entry.getIdEntry( ) ), listRecordFields );
                }
            }
        }

        return mapSearchRecordField;
    }

    /**
     * Populate the list of id record with the id record of the search result for the given directory for the given map of RecordField
     * 
     * @param adminUser
     *            The adminUser who made the search
     * @param listIdRecord
     *            The list of id record to populate
     * @param directory
     *            The directory to make the search on
     * @param mapSearchRecordField
     *            The map which contains the field to find
     * @param locale
     *            The locale
     */
    private void populateIdRecordResultSearchList( AdminUser adminUser, List<String> listIdRecord, Directory directory,
            HashMap<String, List<RecordField>> mapSearchRecordField, Locale locale )
    {
        // Create the search fields object
        DirectoryAdminSearchFields searchFields = new DirectoryAdminSearchFields( );
        searchFields.setIdDirectory( directory.getIdDirectory( ) );
        searchFields.setMapQuery( mapSearchRecordField );

        // Compute the search
        listIdRecord.addAll( DirectoryUtils.getListResults( null, directory, Boolean.TRUE, Boolean.TRUE, searchFields, adminUser, locale ).stream( )
                .map( nId -> nId.toString( ) ).collect( Collectors.toList( ) ) );
    }
}
