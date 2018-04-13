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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryFilter;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.service.DirectoryMultiviewPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.directory.web.action.DirectoryAdminSearchFields;
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
    public List<DirectoryRecordItem> filterBySearchedText( List<DirectoryRecordItem> listDirectoryRecordItem, AdminUser adminUser, String strSearchText,
            Locale locale )
    {
        List<DirectoryRecordItem> listDirectoryRecordItemResult = new ArrayList<>( );

        if ( listDirectoryRecordItem != null && !listDirectoryRecordItem.isEmpty( ) )
        {
            if ( StringUtils.isNotBlank( strSearchText ) )
            {
                // Retrieve the list of directory to use for filter the records
                listDirectoryRecordItemResult = buildDirectoryRecordItemSearchResult( listDirectoryRecordItem, strSearchText, adminUser, locale );
            }
            else
            {
                listDirectoryRecordItemResult = new ArrayList<>( listDirectoryRecordItem );
            }
        }

        return listDirectoryRecordItemResult;
    }

    /**
     * Return the list of DirectoryRecordItem retain from the given list of DirectoryRecordItem and the result of the search
     * 
     * @param listDirectoryRecordItem
     *            The list of DirectoryRecordItem which all the record information before the search is made
     * @param strSearchText
     *            The text which must be searched
     * @param adminUser
     *            The AdminUser who made the search
     * @param locale
     *            The locale to use for the search
     * @return the list of DirectoryRecordItem retain from the given list of DirectoryRecordItem and the result of the search
     */
    private List<DirectoryRecordItem> buildDirectoryRecordItemSearchResult( List<DirectoryRecordItem> listDirectoryRecordItem, String strSearchText,
            AdminUser adminUser, Locale locale )
    {
        List<DirectoryRecordItem> listDirectoryRecordItemResult = new ArrayList<>( );

        // Retrieve the list of directory to use for filter the records
        DirectoryFilter directoryFilter = new DirectoryFilter( );
        List<Directory> listDirectories = DirectoryHome.getDirectoryList( directoryFilter, DirectoryMultiviewPlugin.getPlugin( ) );

        // For each directory, compute the plain text search
        if ( listDirectories != null && !listDirectories.isEmpty( ) )
        {
            // Retrieve the list of the id record of the result of the search
            List<Integer> listIdRecord = retrieveListRecordResultSearch( listDirectories, strSearchText, adminUser, locale );

            // Keep only the record which are contains in the result list
            for ( DirectoryRecordItem directoryRecordItem : listDirectoryRecordItem )
            {
                Integer nIdRecord = directoryRecordItem.getIdRecord( );
                if ( listIdRecord.contains( nIdRecord ) )
                {
                    listDirectoryRecordItemResult.add( directoryRecordItem );
                }
            }
        }

        return listDirectoryRecordItemResult;
    }

    /**
     * Retrieve the list of the id record of the result of the search
     * 
     * @param listDirectories
     *            The list of directory on which the search is based
     * @param strSearchText
     *            The text which must be searched
     * @param adminUser
     *            The AdminUser who made the search
     * @param locale
     *            The locale to use for the search
     * @return the list of the id record of the result of the search
     */
    private List<Integer> retrieveListRecordResultSearch( List<Directory> listDirectories, String strSearchText, AdminUser adminUser, Locale locale )
    {
        List<Integer> listIdRecord = new ArrayList<>( );

        for ( Directory directory : listDirectories )
        {
            // Create the list of entry to search on
            List<IEntry> listEntry = retrieveDirectoryListEntry( directory, DirectoryMultiviewPlugin.getPlugin( ) );

            // Create the query map
            HashMap<String, List<RecordField>> mapSearchRecordField = createMapSearchRecordField( listEntry, strSearchText );

            // If the map contains entry to search on we will make the search
            if ( !mapSearchRecordField.isEmpty( ) )
            {
                List<Integer> listResultSearchIdRecord = populateIdRecordResultSearchList( adminUser, directory, mapSearchRecordField, locale );
                listIdRecord.addAll( listResultSearchIdRecord );
            }
        }

        return listIdRecord;
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
                    List<IEntry> listAllChildren = getListOfAllChildren( listChildrenEntry );
                    listEntryResult.addAll( listAllChildren );
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
                List<RecordField> listRecordFields = new ArrayList<>( );
                RecordField recordField = new RecordField( );
                recordField.setEntry( entry );
                recordField.setValue( strSearchText );
                listRecordFields.add( recordField );
                mapSearchRecordField.put( Integer.toString( entry.getIdEntry( ) ), listRecordFields );
            }
        }

        return mapSearchRecordField;
    }

    /**
     * Return the list of id record with the id record of the search result for the given directory for the given map of RecordField
     * 
     * @param adminUser
     *            The adminUser who made the search
     * @param directory
     *            The directory to make the search on
     * @param mapSearchRecordField
     *            The map which contains the field to find
     * @param locale
     *            The locale
     * @return the the list of id record with the id record of the search result for the given directory for the given map of RecordField
     */
    private List<Integer> populateIdRecordResultSearchList( AdminUser adminUser, Directory directory, HashMap<String, List<RecordField>> mapSearchRecordField,
            Locale locale )
    {
        // Create the search fields object
        DirectoryAdminSearchFields searchFields = new DirectoryAdminSearchFields( );
        searchFields.setIdDirectory( directory.getIdDirectory( ) );
        searchFields.setMapQuery( mapSearchRecordField );

        // Compute the search
        boolean bWorkflowServiceEnable = Boolean.TRUE;
        boolean bUseFilterDirectory = Boolean.TRUE;
        List<Integer> listIdRecord = DirectoryUtils.getListResults( null, directory, bWorkflowServiceEnable, bUseFilterDirectory, searchFields, adminUser,
                locale );

        return listIdRecord;
    }
}
