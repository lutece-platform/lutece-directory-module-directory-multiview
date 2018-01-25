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

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryFilter;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.IRecordFilterItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.RecordFilterDirectoryItem;
import fr.paris.lutece.plugins.directory.modules.multiview.util.ReferenceListFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.columnfilter.IColumnFilter;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Implementation of IRecordFilterParameter for the Directory filter
 */
public class RecordFilterDirectoryParameter implements IRecordFilterParameter
{
    // Constants
    private static final String PARAMETER_DIRECTORY_FILTER = "search_directory";
    private static final String MARK_DIRECTORY_FILTER = "id_directory";

    // Variables
    private final RecordFilterDirectoryItem _recordFilterDirectoryItem;
    private final DirectoryColumnFilter _directoryColumnFilter;

    /**
     * Constructor
     */
    public RecordFilterDirectoryParameter( )
    {
        _recordFilterDirectoryItem = new RecordFilterDirectoryItem( );
        _directoryColumnFilter = new DirectoryColumnFilter( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueFromRequest( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_DIRECTORY_FILTER );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordFilterItem getRecordFilterItem( )
    {
        return _recordFilterDirectoryItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordFilterModelMark( )
    {
        return MARK_DIRECTORY_FILTER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IColumnFilter getColumnFilter( )
    {
        return _directoryColumnFilter;
    }

    /**
     * Implementation of the IColumnFilter interface for the Directory
     */
    private final class DirectoryColumnFilter implements IColumnFilter
    {
        // Constants
        private static final String DIRECTORY_CODE_ATTRIBUTE = "idDirectory";
        private static final String DIRECTORY_NAME_ATTRIBUTE = "title";
        private static final int DIRECTORY_DISABLED_FILTER_VALUE = 1;
        private static final int ID_WORKFLOW_UNSET = 0;

        // Variables
        private final List<Directory> _listDirectory;
        private String _strFilterTemplate;

        /**
         * Constructor
         */
        DirectoryColumnFilter( )
        {
            _listDirectory = new ArrayList<>( );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void populateListValue( )
        {
            DirectoryFilter filter = new DirectoryFilter( );
            filter.setIsDisabled( DIRECTORY_DISABLED_FILTER_VALUE );
            for ( Directory directory : DirectoryHome.getDirectoryList( filter, DirectoryUtils.getPlugin( ) ) )
            {
                if ( directory.getIdWorkflow( ) > ID_WORKFLOW_UNSET )
                {
                    _listDirectory.add( directory );
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ReferenceList createReferenceList( )
        {
            ReferenceListFactory referenceListFactory = new ReferenceListFactory( _listDirectory, DIRECTORY_CODE_ATTRIBUTE, DIRECTORY_NAME_ATTRIBUTE );

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
            model.put( MARK_FILTER_NAME, PARAMETER_DIRECTORY_FILTER );

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
