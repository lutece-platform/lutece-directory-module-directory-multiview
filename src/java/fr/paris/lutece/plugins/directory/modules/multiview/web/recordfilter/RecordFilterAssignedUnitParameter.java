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
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.IRecordFilterItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.RecordFilterAssignedUnitItem;
import fr.paris.lutece.plugins.directory.modules.multiview.util.ReferenceListFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.columnfilter.IColumnFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.web.columnfilter.RecordFieldHelperColumnFilter;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Implementation of IRecordFilterParameter for the Assigned Unit id filter
 */
public class RecordFilterAssignedUnitParameter implements IRecordFilterParameter
{
    // Constants
    private static final String PARAMETER_ASSIGNED_UNIT_FILTER = "search_assigned_unit";
    private static final String MARK_ASSIGNED_UNIT_FILTER = "id_assigned_unit";

    // Variables
    private final RecordFilterAssignedUnitItem _recordFilterAssignedUnitItem;
    private final AssignedUnitColumnFilter _assignedUnitColumnFilter;

    /**
     * Constructor
     * 
     * @param request
     *            The HttpServletRequest to set
     */
    public RecordFilterAssignedUnitParameter( HttpServletRequest request )
    {
        _recordFilterAssignedUnitItem = new RecordFilterAssignedUnitItem( );
        _assignedUnitColumnFilter = new AssignedUnitColumnFilter( request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueFromRequest( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_ASSIGNED_UNIT_FILTER );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordFilterItem getRecordFilterItem( )
    {
        return _recordFilterAssignedUnitItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordFilterModelMark( )
    {
        return MARK_ASSIGNED_UNIT_FILTER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IColumnFilter getColumnFilter( )
    {
        return _assignedUnitColumnFilter;
    }

    /**
     * Implementation of the IColumnFilter interface for the AssignedUnit
     */
    private final class AssignedUnitColumnFilter extends RecordFieldHelperColumnFilter implements IColumnFilter
    {
        // Constants
        private static final String UNIT_CODE_ATTRIBUTE = "idUnit";
        private static final String UNIT_NAME_ATTRIBUTE = "label";

        // Messages
        private static final String MESSAGE_UNIT_ATTRIBUTE_DEFAULT_NAME = "module.directory.multiview.manage_directory_multirecord.unit.attribute.defaultName";

        // Variables
        private final List<Unit> _listAssignedUnitFilter;
        private final Map<Integer, RecordAssignment> _mapRecordAssignment;
        private Locale _locale;
        private String _strFilterTemplate;

        /**
         * Constructor
         * 
         * @param request
         *            The HttpServletRequest to set
         */
        AssignedUnitColumnFilter( HttpServletRequest request )
        {
            super( );
            _listAssignedUnitFilter = new ArrayList<>( );
            _mapRecordAssignment = createRecordAssignmentFilterMap( request );
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
                    RecordAssignment recordAssignmentFromRecord = _mapRecordAssignment.get( record.getIdRecord( ) );
                    _listAssignedUnitFilter.add( recordAssignmentFromRecord.getAssignedUnit( ) );
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ReferenceList createReferenceList( )
        {
            ReferenceListFactory referenceListFactory = new ReferenceListFactory( _listAssignedUnitFilter, UNIT_CODE_ATTRIBUTE, UNIT_NAME_ATTRIBUTE );
            referenceListFactory.setDefaultName( I18nService.getLocalizedString( MESSAGE_UNIT_ATTRIBUTE_DEFAULT_NAME, _locale ) );

            return referenceListFactory.createReferenceList( );
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
            model.put( MARK_FILTER_NAME, PARAMETER_ASSIGNED_UNIT_FILTER );

            HtmlTemplate htmlTemplate = AppTemplateService.getTemplate( FILTER_TEMPLATE_NAME, _locale, model );
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
