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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.impl;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCell;
import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordFilterColumnNameConstants;
import fr.paris.lutece.portal.service.template.AppTemplateService;

/**
 * Implementation of the IRecordColumnDisplay for the Entry column
 */
public class RecordColumnDisplayEntry extends AbstractRecordColumnDisplay
{
    // Templates
    private static final String RECORD_COLUMN_HEADER_TEMPLATE = "admin/plugins/directory/modules/multiview/column/header/record_column_entry_header.html";
    private static final String RECORD_COLUMN_CELL_TEMPLATE = "admin/plugins/directory/modules/multiview/column/cell/record_column_entry_cell.html";

    // Marks
    private static final String MARK_ENTRY_VALUE_COLUMN_TITLE = "column_title";
    private static final String MARK_ENTRY_VALUE_COLUMN_POSITION = "entry_column_position";
    private static final String MARK_ENTRY_VALUE = "entry_value";

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildRecordColumnHeaderTemplate( String strSortUrl, Locale locale )
    {
        Map<String, Object> model = new LinkedHashMap<>( );
        model.put( MARK_ENTRY_VALUE_COLUMN_TITLE, getRecordColumnTitle( ) );
        model.put( MARK_ENTRY_VALUE_COLUMN_POSITION, getPosition( ) );

        String strColumnHeaderTemplate = AppTemplateService.getTemplate( RECORD_COLUMN_HEADER_TEMPLATE, locale, model ).getHtml( );
        setRecordColumnHeaderTemplate( strColumnHeaderTemplate );

        return strColumnHeaderTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildRecordColumnCellTemplate( RecordColumnCell recordColumnCell, Locale locale )
    {
        String strEntryValue = StringUtils.EMPTY;
        if ( recordColumnCell != null )
        {
            String strEntryValueName = String
                    .format( RecordFilterColumnNameConstants.COLUMN_ENTRY_VALUE_PATTERN, getRecordColumnPosition( ) );
            Object objEntryValue = recordColumnCell.getRecordColumnCellValueByName( strEntryValueName );
            if ( objEntryValue != null )
            {
                strEntryValue = String.valueOf( objEntryValue );
            }
        }

        Map<String, Object> model = new LinkedHashMap<>( );
        model.put( MARK_ENTRY_VALUE, strEntryValue );

        String strRecordColumnEntryTemplate = AppTemplateService.getTemplate( RECORD_COLUMN_CELL_TEMPLATE, locale, model ).getHtml( );

        return strRecordColumnEntryTemplate;
    }

    /**
     * Return the position of the RecordColumn or {@linkplain NumberUtils.INTEGER_MINUS_ONE} if doesn't exist
     * 
     * @return the position of the RecordColumn
     */
    private int getRecordColumnPosition( )
    {
        int nRecordColumnPosition = NumberUtils.INTEGER_MINUS_ONE;
        IRecordColumn recordColumn = getRecordColumn( );
        if ( recordColumn != null )
        {
            nRecordColumnPosition = recordColumn.getRecordColumnPosition( );
        }

        return nRecordColumnPosition;
    }
}
