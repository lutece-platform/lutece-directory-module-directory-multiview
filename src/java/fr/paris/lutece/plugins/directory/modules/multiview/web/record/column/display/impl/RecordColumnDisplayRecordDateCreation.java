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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCell;
import fr.paris.lutece.plugins.directory.modules.multiview.util.RecordFilterColumnNameConstants;
import fr.paris.lutece.portal.service.template.AppTemplateService;

/**
 * Implementation of the IRecordColumnDisplay for the column of the creation date of a record
 */
public class RecordColumnDisplayRecordDateCreation extends AbstractRecordColumnDisplay
{
    // Templates
    private static final String RECORD_COLUMN_HEADER_TEMPLATE = "admin/plugins/directory/modules/multiview/column/header/record_column_record_date_creation_header.html";
    private static final String RECORD_COLUMN_CELL_TEMPLATE = "admin/plugins/directory/modules/multiview/column/cell/record_column_record_date_creation_cell.html";

    // Marks
    private static final String MARK_RECORD_DATE_CREATION_COLUMN_TITLE = "column_title";
    private static final String MARK_SORT_URL = "sort_url";
    private static final String MARK_RECORD_DATE_CREATION = "record_date_creation";
    private static final String MARK_COLUMN_SORT_ATTRIBUTE = "column_sort_attribute";

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildRecordColumnHeaderTemplate( String strSortUrl, Locale locale )
    {
        Map<String, Object> model = new LinkedHashMap<>( );
        model.put( MARK_SORT_URL, buildCompleteSortUrl( strSortUrl ) );
        model.put( MARK_RECORD_DATE_CREATION_COLUMN_TITLE, getRecordColumnTitle( ) );
        model.put( MARK_COLUMN_SORT_ATTRIBUTE, RecordFilterColumnNameConstants.COLUMN_RECORD_DATE_CREATION );

        String strRecordDateCreationHeaderTemplate = AppTemplateService.getTemplate( RECORD_COLUMN_HEADER_TEMPLATE, locale, model ).getHtml( );
        setRecordColumnHeaderTemplate( strRecordDateCreationHeaderTemplate );

        return strRecordDateCreationHeaderTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildRecordColumnCellTemplate( RecordColumnCell recordColumnCell, Locale locale )
    {
        Date dateRecordDateCreation = null;
        if ( recordColumnCell != null )
        {
            Object objRecordDateCreation = recordColumnCell.getRecordColumnCellValueByName( RecordFilterColumnNameConstants.COLUMN_RECORD_DATE_CREATION );
            if ( objRecordDateCreation != null )
            {
                dateRecordDateCreation = (Date) objRecordDateCreation;
            }
        }

        Map<String, Object> model = new LinkedHashMap<>( );
        model.put( MARK_RECORD_DATE_CREATION, dateRecordDateCreation );

        String strRecordDateCreationTemplate = AppTemplateService.getTemplate( RECORD_COLUMN_CELL_TEMPLATE, locale, model ).getHtml( );

        return strRecordDateCreationTemplate;
    }
}
