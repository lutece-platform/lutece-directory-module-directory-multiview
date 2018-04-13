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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCell;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.IRecordColumnDisplay;
import fr.paris.lutece.portal.service.template.AppTemplateService;

/**
 * Builder class for the template of RecordColumnDisplay and RecordFilterDisplay objects
 */
public final class RecordListTemplateBuilder
{
    // Templates
    private static final String TEMPLATE_MULTI_DIRECTORY_TABLE = "admin/plugins/directory/modules/multiview/includes/include_manage_multi_record_directory_table.html";

    // Marks
    private static final String DIRECTORY_RECORD_ITEM_LIST = "directory_record_item_list";
    private static final String RECORD_COLUMN_HEADER_TEMPLATE_LIST = "record_column_header_template_list";
    private static final String RECORD_PART_LINE_TEMPLATE_LIST = "record_part_line_template_list";

    /**
     * Constructor
     */
    private RecordListTemplateBuilder( )
    {

    }

    /**
     * Build the template of the table of all records
     * 
     * @param listRecordColumnDisplay
     *            The list of all RecordColumnDisplay objects to build the global template of the record columns from
     * @param listDirectoryRecordItem
     *            The list of all DirectoryItem used to build the tab with all records
     * @param locale
     *            The locale to used for build template
     * @param strSortUrl
     *            The url to use for sort a column
     * @return the global template of all RecordColumnDisplay objects
     */
    public static String buildTableTemplate( List<IRecordColumnDisplay> listRecordColumnDisplay, List<DirectoryRecordItem> listDirectoryRecordItem,
            Locale locale, String strSortUrl )
    {
        String strTableTemplate = StringUtils.EMPTY;

        if ( listRecordColumnDisplay != null && !listRecordColumnDisplay.isEmpty( ) && listDirectoryRecordItem != null && !listDirectoryRecordItem.isEmpty( ) )
        {
            // Build the list of all record column header template
            List<String> listRecordColumnHeaderTemplate = buildRecordColumnHeaderTemplateList( listRecordColumnDisplay, locale, strSortUrl );

            // Build the list of all RecordColumnLineTemplate
            List<RecordColumnLineTemplate> listRecordColumnLineTemplate = buildRecordColumnLineTemplateList( listRecordColumnDisplay, listDirectoryRecordItem,
                    locale );

            // Build the model
            Map<String, Object> model = new LinkedHashMap<>( );
            model.put( RECORD_COLUMN_HEADER_TEMPLATE_LIST, listRecordColumnHeaderTemplate );
            model.put( DIRECTORY_RECORD_ITEM_LIST, listDirectoryRecordItem );
            model.put( RECORD_PART_LINE_TEMPLATE_LIST, listRecordColumnLineTemplate );

            strTableTemplate = AppTemplateService.getTemplate( TEMPLATE_MULTI_DIRECTORY_TABLE, locale, model ).getHtml( );
        }

        return strTableTemplate;
    }

    /**
     * Build the list of all record column header template
     * 
     * @param listRecordColumnDisplay
     *            The list of all record column display to retrieve the header template from
     * @param locale
     *            The locale to use for build the template
     * @param strSortUrl
     *            The url to use for sort a column (can be null)
     * @return the list of all record column header template
     */
    private static List<String> buildRecordColumnHeaderTemplateList( List<IRecordColumnDisplay> listRecordColumnDisplay, Locale locale, String strSortUrl )
    {
        List<String> listRecordColumnHeaderTemplate = new ArrayList<>( );

        if ( listRecordColumnDisplay != null && !listRecordColumnDisplay.isEmpty( ) )
        {
            for ( IRecordColumnDisplay recordColumnDisplay : listRecordColumnDisplay )
            {
                String strRecordColumnDisplayHeaderTemplate = recordColumnDisplay.buildRecordColumnHeaderTemplate( strSortUrl, locale );
                listRecordColumnHeaderTemplate.add( strRecordColumnDisplayHeaderTemplate );
            }
        }

        return listRecordColumnHeaderTemplate;
    }

    /**
     * Build the list of all RecordColumnLineTemplate
     * 
     * @param listRecordColumnDisplay
     *            The list of record column display to use for build the map of line template
     * @param listDirectoryRecordItem
     *            The list of record item to use for build the map of line template
     * @param locale
     *            The locale to use for build the template
     * @return the list of all RecordColumnLineTemplate
     */
    private static List<RecordColumnLineTemplate> buildRecordColumnLineTemplateList( List<IRecordColumnDisplay> listRecordColumnDisplay,
            List<DirectoryRecordItem> listDirectoryRecordItem, Locale locale )
    {
        List<RecordColumnLineTemplate> listRecordColumnLineTemplate = new ArrayList<>( );

        if ( listRecordColumnDisplay != null && !listRecordColumnDisplay.isEmpty( ) && listDirectoryRecordItem != null && !listDirectoryRecordItem.isEmpty( ) )
        {
            for ( DirectoryRecordItem directoryRecordItem : listDirectoryRecordItem )
            {
                int nIdRecord = directoryRecordItem.getIdRecord( );
                int nIdDirectory = directoryRecordItem.getIdDirectory( );
                RecordColumnLineTemplate recordColumnLineTemplate = new RecordColumnLineTemplate( nIdRecord, nIdDirectory );

                List<RecordColumnCell> listRecordColumnCell = directoryRecordItem.getDirectoryRecordCellValues( );
                populatePartLineTemplateFromCellValues( recordColumnLineTemplate, listRecordColumnCell, listRecordColumnDisplay, locale );

                listRecordColumnLineTemplate.add( recordColumnLineTemplate );
            }
        }

        return listRecordColumnLineTemplate;
    }

    /**
     * Populate the given RecordColumnLineTemplate with the value from the list of RecordcolumnCell and the display from the given list of IRecordColumnDisplay
     * 
     * @param recordColumnLineTemplate
     *            The RecordColumnLineTemplate to populate
     * @param listRecordColumnCell
     *            The list of RecordColumnCell to retrieve the values from
     * @param listRecordColumnDisplay
     *            The list of IRecordColumnDisplayto use for the column
     * @param locale
     *            The locale to use for build the templates
     */
    private static void populatePartLineTemplateFromCellValues( RecordColumnLineTemplate recordColumnLineTemplate, List<RecordColumnCell> listRecordColumnCell,
            List<IRecordColumnDisplay> listRecordColumnDisplay, Locale locale )
    {
        if ( listRecordColumnCell != null && !listRecordColumnCell.isEmpty( ) )
        {
            for ( int index = 0; index < listRecordColumnCell.size( ); index++ )
            {
                // We will increment the index to retrieve the record column display because the first column is at position 1
                int nColumnDisplayPosition = index + 1;
                IRecordColumnDisplay recordColumnDisplay = findRecordColumnDisplayByPosition( nColumnDisplayPosition, listRecordColumnDisplay );
                if ( recordColumnDisplay != null )
                {
                    RecordColumnCell recordColumnCell = listRecordColumnCell.get( index );
                    String strRecordColunmCellTemplate = recordColumnDisplay.buildRecordColumnCellTemplate( recordColumnCell, locale );
                    recordColumnLineTemplate.addRecordColumnCellTemplate( strRecordColunmCellTemplate );
                }
            }
        }
    }

    /**
     * Find the RecordColumnDisplay in the given with the specified position or null if not found
     * 
     * @param nRecordColumnPosition
     *            The position of the RecordColumnDisplay to retrieve
     * @param listRecordColumnDisplay
     *            The list of RecordColumnDisplay from where to find the column with the specified position
     * @return the RecordColumnDisplay with the specified position nor null if not found
     */
    private static IRecordColumnDisplay findRecordColumnDisplayByPosition( int nRecordColumnPosition, List<IRecordColumnDisplay> listRecordColumnDisplay )
    {
        IRecordColumnDisplay recordColumnDisplayResult = null;

        if ( listRecordColumnDisplay != null && !listRecordColumnDisplay.isEmpty( ) )
        {
            for ( IRecordColumnDisplay recordColumnDisplay : listRecordColumnDisplay )
            {
                if ( recordColumnDisplay.getPosition( ) == nRecordColumnPosition )
                {
                    recordColumnDisplayResult = recordColumnDisplay;
                    break;
                }
            }
        }

        return recordColumnDisplayResult;
    }
}
