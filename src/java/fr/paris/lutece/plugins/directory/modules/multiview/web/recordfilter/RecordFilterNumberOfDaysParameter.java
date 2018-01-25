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
package fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.IRecordFilterItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.RecordFilterNumberOfDaysItem;
import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.web.columnfilter.IColumnFilter;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Implementation of IRecordFilterParameter for the Number of Days filter
 */
public class RecordFilterNumberOfDaysParameter implements IRecordFilterParameter
{
    // Constants
    private static final String PARAMETER_NUMBER_OF_DAYS_FILTER = "search_open_since";
    private static final String MARK_NUMBER_OF_DAYS_FILTER = "number_of_days";

    // Variables
    private final RecordFilterNumberOfDaysItem _recordFilterNumberOfDaysItem;
    private final NumberOfDaysColumnFilter _numberOfDaysColumnFilter;

    /**
     * Constructor
     */
    public RecordFilterNumberOfDaysParameter( )
    {
        _recordFilterNumberOfDaysItem = new RecordFilterNumberOfDaysItem( );
        _numberOfDaysColumnFilter = new NumberOfDaysColumnFilter( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueFromRequest( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_NUMBER_OF_DAYS_FILTER );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordFilterItem getRecordFilterItem( )
    {
        return _recordFilterNumberOfDaysItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordFilterModelMark( )
    {
        return MARK_NUMBER_OF_DAYS_FILTER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IColumnFilter getColumnFilter( )
    {
        return _numberOfDaysColumnFilter;
    }

    /**
     * Implementation of the IColumnFilter interface for the Directory
     */
    private class NumberOfDaysColumnFilter implements IColumnFilter
    {
        // Constants
        private static final int LAST_DAY_VALUE = 1;
        private static final int LAST_WEEK_VALUE = 7;
        private static final int LAST_MONTH_VALUE = 31;

        // Messages keys
        private static final String DEFAULT_DAY_MESSAGE_KEY = "module.directory.multiview.records_list.search_since_default";
        private static final String LAST_DAY_MESSAGE_KEY = "module.directory.multiview.records_list.search_since_last_day";
        private static final String LAST_WEEK_MESSAGE_KEY = "module.directory.multiview.records_list.search_since_last_week";
        private static final String LAST_MONTH_MESSAGE_KEY = "module.directory.multiview.records_list.search_since_last_month";

        // Variables
        private String _strFilterTemplate;
        private Locale _locale;

        /**
         * {@inheritDoc}
         */
        @Override
        public void populateListValue( )
        {
            // There is no list to populate for the number of days filter
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ReferenceList createReferenceList( )
        {
            ReferenceList referenceList = new ReferenceList( );

            // Default value
            String strDefaultDayItemName = I18nService.getLocalizedString( DEFAULT_DAY_MESSAGE_KEY, _locale );
            referenceList.addItem( DirectoryMultiviewConstants.DEFAULT_FILTER_VALUE, strDefaultDayItemName );

            // Last day filter
            String strLastDayItemName = I18nService.getLocalizedString( LAST_DAY_MESSAGE_KEY, _locale );
            referenceList.addItem( LAST_DAY_VALUE, strLastDayItemName );

            // Last week filter
            String strLastWeekItemName = I18nService.getLocalizedString( LAST_WEEK_MESSAGE_KEY, _locale );
            referenceList.addItem( LAST_WEEK_VALUE, strLastWeekItemName );

            // Last month filter
            String strLastMonthItemName = I18nService.getLocalizedString( LAST_MONTH_MESSAGE_KEY, _locale );
            referenceList.addItem( LAST_MONTH_VALUE, strLastMonthItemName );

            return referenceList;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void buildTemplate( RecordAssignmentFilter filter, HttpServletRequest request )
        {
            String strTemplateResult = StringUtils.EMPTY;
            _locale = request.getLocale( );

            Map<String, Object> model = new LinkedHashMap<>( );
            model.put( MARK_FILTER_LIST, createReferenceList( ) );
            model.put( MARK_FILTER_LIST_VALUE, getRecordFilterItem( ).getItemValue( filter ) );
            model.put( MARK_FILTER_NAME, PARAMETER_NUMBER_OF_DAYS_FILTER );

            HtmlTemplate htmlTemplate = AppTemplateService.getTemplate( FILTER_TEMPLATE_NAME, request.getLocale( ), model );
            if ( htmlTemplate != null )
            {
                strTemplateResult = htmlTemplate.getHtml( );
            }

            _strFilterTemplate = strTemplateResult;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTemplate( )
        {
            return _strFilterTemplate;
        }
    }
}
