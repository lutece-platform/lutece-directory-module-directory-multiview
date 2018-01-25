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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.IRecordFilterItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.RecordFilterCustomizedColumnItem;
import fr.paris.lutece.plugins.directory.modules.multiview.util.ReferenceListFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.columnfilter.IColumnFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.web.columnfilter.RecordFieldHelperColumnFilter;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Implementation of IRecordFilterParameter for the Customized Column Two filter
 */
public class RecordFilterCustomizedColumnParameter implements IRecordFilterParameter
{
    // Patterns
    private static final String PATTERN_CUSTOMIZED_COLUMN_PARAMETER = "search_customized_column_%s";
    private static final String PATTERN_CUSTOMIZED_COLUMN_MARK = "customized_column_%s_value";

    // Variables
    private final RecordFilterCustomizedColumnItem _recordFilterCustomizedColumnItem;
    private final CustomizedColumnColumnFilter _customizedColumnColumnFilter;
    private final String _strCustomizedColumnParameter;
    private final String _strCustomizedColumnMark;

    /**
     * Constructor
     * 
     * @param request
     *            The HttpServletRequest to set
     * @param listEntry
     *            The list of Entry to retrieve the value for the filter list
     * @param nColumnNumber
     *            The column number of the CustomizedColumn
     */
    public RecordFilterCustomizedColumnParameter( HttpServletRequest request, List<IEntry> listEntry, int nColumnNumber )
    {
        _strCustomizedColumnParameter = String.format( PATTERN_CUSTOMIZED_COLUMN_PARAMETER, nColumnNumber );
        _strCustomizedColumnMark = String.format( PATTERN_CUSTOMIZED_COLUMN_MARK, nColumnNumber );
        _recordFilterCustomizedColumnItem = new RecordFilterCustomizedColumnItem( nColumnNumber );
        _customizedColumnColumnFilter = new CustomizedColumnColumnFilter( request, listEntry );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueFromRequest( HttpServletRequest request )
    {
        return request.getParameter( _strCustomizedColumnParameter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordFilterModelMark( )
    {
        return _strCustomizedColumnMark;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordFilterItem getRecordFilterItem( )
    {
        return _recordFilterCustomizedColumnItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IColumnFilter getColumnFilter( )
    {
        return _customizedColumnColumnFilter;
    }

    /**
     * Implementation of the IColumnFilter interface for the Directory
     */
    private final class CustomizedColumnColumnFilter extends RecordFieldHelperColumnFilter implements IColumnFilter
    {
        // Constants
        private static final String RECORDFIELD_VALUE_ATTRIBUTE = "value";

        // Messages
        private static final String MESSAGE_RECORDFIELD_ATTRIBUTE_DEFAULT_NAME = "module.directory.multiview.manage_directory_multirecord.recordfield.attribute.defaultName";

        // Variables
        private final List<RecordField> _listCustomizedColumnRecordField;
        private final List<IEntry> _listIEntryToRetrieveValueFrom;
        private final Map<Integer, RecordAssignment> _mapRecordAssignment;
        private final HttpServletRequest _request;
        private String _strFilterTemplate;

        /**
         * Constructor
         * 
         * @param request
         *            The HttpServletRequest
         * @param listEntry
         *            The list of Entry to retrieve the value from
         */
        CustomizedColumnColumnFilter( HttpServletRequest request, List<IEntry> listEntry )
        {
            super( );
            _request = request;
            _listCustomizedColumnRecordField = new ArrayList<>( );
            _listIEntryToRetrieveValueFrom = listEntry;
            _mapRecordAssignment = createRecordAssignmentFilerMap( request );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void populateListValue( )
        {
            // Retrieve the Resource Action associated to the Record of each RecordAssignment
            for ( RecordAssignment recordAssignment : _mapRecordAssignment.values( ) )
            {
                Record record = RecordHome.findByPrimaryKey( recordAssignment.getIdRecord( ), DirectoryUtils.getPlugin( ) );
                if ( record != null && _mapRecordAssignment.get( record.getIdRecord( ) ) != null )
                {
                    manageRecordField( record );
                }
            }
        }

        /**
         * Retrieve the list of RecordField associated to the record and check if we must collect the RecordField which are linked to an entry which we want to
         * filter the values.
         * 
         * @param record
         *            The Record to retrieve the RecordField from
         */
        private void manageRecordField( Record record )
        {
            RecordFieldFilter recordFieldFilter = new RecordFieldFilter( );
            recordFieldFilter.setIdRecord( record.getIdRecord( ) );

            List<RecordField> listRecordField = RecordFieldHome.getRecordFieldList( recordFieldFilter, DirectoryUtils.getPlugin( ) );
            if ( listRecordField != null && !listRecordField.isEmpty( ) )
            {
                for ( RecordField recordField : listRecordField )
                {
                    checkIfRecordFieldBelongToEntryInList( recordField );
                }
            }
        }

        /**
         * Check if a RecordField belong to an entry of the list of Entry which we want to gather values. If it's true the RecordField is added to the filter
         * list.
         * 
         * @param recordField
         *            The RecordField to analyze
         */
        private void checkIfRecordFieldBelongToEntryInList( RecordField recordField )
        {
            for ( IEntry entry : _listIEntryToRetrieveValueFrom )
            {
                if ( entry.getIdEntry( ) == recordField.getEntry( ).getIdEntry( ) )
                {
                    _listCustomizedColumnRecordField.add( recordField );
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ReferenceList createReferenceList( )
        {
            ReferenceListFactory referenceListFactory = new ReferenceListFactory( _listCustomizedColumnRecordField, RECORDFIELD_VALUE_ATTRIBUTE,
                    RECORDFIELD_VALUE_ATTRIBUTE, Boolean.FALSE );
            referenceListFactory.setDefaultName( I18nService.getLocalizedString( MESSAGE_RECORDFIELD_ATTRIBUTE_DEFAULT_NAME, _request.getLocale( ) ) );

            return referenceListFactory.createReferenceList( );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void buildTemplate( RecordAssignmentFilter filter, HttpServletRequest request )
        {
            String strTemplateResult = StringUtils.EMPTY;

            Map<String, Object> model = new LinkedHashMap<>( );
            model.put( MARK_FILTER_LIST, createReferenceList( ) );
            model.put( MARK_FILTER_LIST_VALUE, getRecordFilterItem( ).getItemValue( filter ) );
            model.put( MARK_FILTER_NAME, _strCustomizedColumnParameter );

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
