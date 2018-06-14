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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.impl;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCell;
import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordDirectoryNameConstants;
import fr.paris.lutece.portal.service.template.AppTemplateService;

/**
 * Implementation of the IRecordColumnDisplay for the Directory column
 */
public class RecordColumnDisplayDirectory extends AbstractRecordColumnDisplay
{
    // Templates
    private static final String RECORD_COLUMN_HEADER_TEMPLATE = "admin/plugins/directory/modules/multiview/column/header/record_column_directory_header.html";
    private static final String RECORD_COLUMN_CELL_TEMPLATE = "admin/plugins/directory/modules/multiview/column/cell/record_column_directory_cell.html";

    // Marks
    private static final String MARK_DIRECTORY_COLUMN_TITLE = "column_title";
    private static final String MARK_DIRECTORY_TITLE = "directory_title";

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildRecordColumnHeaderTemplate( String strSortUrl, Locale locale )
    {
        Map<String, Object> model = new LinkedHashMap<>( );
        model.put( MARK_DIRECTORY_COLUMN_TITLE, getRecordColumnTitle( ) );

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
        String strDirectoryTitle = StringUtils.EMPTY;
        if ( recordColumnCell != null )
        {
            Object objDirectoryTitle = recordColumnCell.getRecordColumnCellValueByName( RecordDirectoryNameConstants.COLUMN_DIRECTORY_TITLE );
            if ( objDirectoryTitle != null )
            {
                strDirectoryTitle = String.valueOf( objDirectoryTitle );
            }
        }

        Map<String, Object> model = new LinkedHashMap<>( );
        model.put( MARK_DIRECTORY_TITLE, strDirectoryTitle );

        String strRecordColumnDirectoryTemplate = AppTemplateService.getTemplate( RECORD_COLUMN_CELL_TEMPLATE, locale, model ).getHtml( );

        return strRecordColumnDirectoryTemplate;
    }
}
