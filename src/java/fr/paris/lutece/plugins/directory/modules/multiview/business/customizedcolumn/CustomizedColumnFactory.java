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
package fr.paris.lutece.plugins.directory.modules.multiview.business.customizedcolumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordFieldItem;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Factory for the CustomizedColumn objects
 */
public class CustomizedColumnFactory
{
    // Constants
    private static final String PROPERTY_KEY_VALUES_SEPARATOR = ",";
    private static final int DEFAULT_CUSTOMIZED_COLUMN_NUMBER = NumberUtils.INTEGER_MINUS_ONE;
    private static final int FIRST_COLUMN_NUMBER = NumberUtils.INTEGER_ONE;
    private static final String DEFAULT_RECORD_FIELD_VALUE = StringUtils.EMPTY;

    // Property
    private static final String PROPERTY_COLUMN_NUMBER = "directory-multiview.entry_name_list.customized_column_number";

    // Pattern
    private static final String PROPERTY_COLUMN_VALUE_KEY_PATTERN = "directory-multiview.entry_name_list.customized_column_%s";
    private static final String PROPERTY_FILTER_COLUMN_KEY_PATTERN = "directory-multiview.entry_name_list.customized_column_%s_filter";

    // Variables
    private final Plugin _plugin;
    private final int _nColumnNumber;
    private final List<CustomizedColumn> _listCustomizedColumn;

    /**
     * Constructor
     * 
     * @param listDirectory
     *            The list of Directory to construct the list of CustomizedColumn with
     * @param plugin
     *            The Plugin to use for retrieving values
     * @param locale
     *            The locale to used for retrieving the title of the column
     */
    public CustomizedColumnFactory( List<Directory> listDirectory, Plugin plugin, Locale locale )
    {
        _plugin = plugin;
        _nColumnNumber = NumberUtils.toInt( AppPropertiesService.getProperty( PROPERTY_COLUMN_NUMBER ), DEFAULT_CUSTOMIZED_COLUMN_NUMBER );

        // Create the list of CustomizedColumn
        _listCustomizedColumn = new ArrayList<>( );
        if ( _nColumnNumber != DEFAULT_CUSTOMIZED_COLUMN_NUMBER )
        {
            for ( int nColumnNumber = FIRST_COLUMN_NUMBER; nColumnNumber <= _nColumnNumber; nColumnNumber++ )
            {
                boolean isFiltered = isCustomizedColumnFilterAuthorized( nColumnNumber );
                _listCustomizedColumn.add( new CustomizedColumn( nColumnNumber, isFiltered, locale ) );
            }
        }

        // Populate the list of CustomizedColumn
        if ( listDirectory != null && !listDirectory.isEmpty( ) )
        {
            for ( Directory directory : listDirectory )
            {
                populateCustomizedColumnLists( directory.getIdDirectory( ) );
            }
        }
    }

    /**
     * Check if the CustomizedColumn with the specified number can be filtered or not
     * 
     * @param nCustomizedColumnNumber
     *            The number of the CustomizedColumn to analyze
     * @return true if the CustomizedColumn with the given number can be filtered false otherwise
     */
    private boolean isCustomizedColumnFilterAuthorized( int nCustomizedColumnNumber )
    {
        String strFilterColumnPropertyKey = String.format( PROPERTY_FILTER_COLUMN_KEY_PATTERN, nCustomizedColumnNumber );
        String strFiltercolumnPropertyValue = AppPropertiesService.getProperty( strFilterColumnPropertyKey );

        return Boolean.parseBoolean( strFiltercolumnPropertyValue );
    }

    /**
     * Populate the list of CustomizedColumn of the factory for a specified Directory identifier
     * 
     * @param nIdDirectory
     *            The identifier of the Directory to populate the list of Entry from
     */
    private void populateCustomizedColumnLists( int nIdDirectory )
    {
        if ( _nColumnNumber != DEFAULT_CUSTOMIZED_COLUMN_NUMBER )
        {
            for ( int nColumnNumber = FIRST_COLUMN_NUMBER; nColumnNumber <= _nColumnNumber; nColumnNumber++ )
            {
                String strPropertyColumnKey = String.format( PROPERTY_COLUMN_VALUE_KEY_PATTERN, nColumnNumber );
                List<IEntry> listEntry = fillEntryListFromTitle( nIdDirectory, strPropertyColumnKey );

                CustomizedColumn customizedColumn = findCustomizedColumnByNumber( nColumnNumber );
                customizedColumn.addColumnListEntry( listEntry );
            }
        }
    }

    /**
     * Fill the given list with all the IEntry of the specified directory which have the same title than the value of the property.
     * 
     * @param nIdDirectory
     *            The identifier of the directory to retrieve the IEntry from
     * @param strPropertyEntryName
     *            The property key to retrieve the IEntry title value from
     * @return the list of entry retrieve from the directory from the given property key
     */
    private List<IEntry> fillEntryListFromTitle( int nIdDirectory, String strPropertyEntryName )
    {
        List<IEntry> listIEntry = new ArrayList<>( );

        EntryFilter entryFilter = new EntryFilter( );
        entryFilter.setIdDirectory( nIdDirectory );
        entryFilter.setIsGroup( EntryFilter.FILTER_FALSE );
        entryFilter.setIsComment( EntryFilter.FILTER_FALSE );

        String entryNameList = AppPropertiesService.getProperty( strPropertyEntryName );
        String [ ] entryNameTab = entryNameList.split( PROPERTY_KEY_VALUES_SEPARATOR );
        List<IEntry> entryList = EntryHome.getEntryList( entryFilter, _plugin );

        for ( String entryTitle : entryNameTab )
        {
            entryList.stream( ).filter( entry -> entryTitle.trim( ).equals( entry.getTitle( ) ) ).forEachOrdered( listIEntry::add );
        }

        return listIEntry;
    }

    /**
     * Return the CustomizedColumn from the list of CustomizedColumn of the factory from a specific number
     * 
     * @param nColumnNumber
     *            The number of the column to retrieve the CustomizedColumn from the list
     * @return the CustomizedColumn associate to the specified column number
     */
    private CustomizedColumn findCustomizedColumnByNumber( int nColumnNumber )
    {
        CustomizedColumn customizedColumnResult = null;

        for ( CustomizedColumn customizedColumn : _listCustomizedColumn )
        {
            if ( customizedColumn.getCustomizedColumnNumber( ) == nColumnNumber )
            {
                customizedColumnResult = customizedColumn;

                break;
            }
        }

        return customizedColumnResult;
    }

    /**
     * Create the list of RecordFieldItem associated the list of CustomizedColumn
     * 
     * @return the list of RecordFieldItem associated the list of CustomizedColumn
     */
    public List<RecordFieldItem> createRecordFieldItemList( )
    {
        List<RecordFieldItem> listRecordFieldItem = new ArrayList<>( );

        for ( CustomizedColumn customizedColumn : _listCustomizedColumn )
        {
            if ( customizedColumn.isFilterAuthorized( ) )
            {
                RecordFieldItem recordFieldItem = new RecordFieldItem( customizedColumn.getCustomizedColumnNumber( ) );
                recordFieldItem.setRecordFieldValue( DEFAULT_RECORD_FIELD_VALUE );

                listRecordFieldItem.add( recordFieldItem );
            }
        }

        return listRecordFieldItem;
    }

    /**
     * Return the list of CustomizedColumn of the Factory
     * 
     * @return the list of CustomizedColumn
     */
    public List<CustomizedColumn> createCustomizedColumnList( )
    {
        return _listCustomizedColumn;
    }
}
