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
package fr.paris.lutece.plugins.directory.modules.multiview.web.recordlistpanel;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService;
import fr.paris.lutece.plugins.directory.modules.multiview.service.search.DirectoryMultiviewSearchService;
import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewConstants;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.AssignmentService;
import fr.paris.lutece.portal.service.i18n.I18nService;

/**
 * Abstract class which implements the IRecordListPanel interface
 */
public abstract class AbstractRecordListPanel implements IRecordListPanel
{
    // Constants
    protected static final int DEFAULT_RECORD_NUMBER = NumberUtils.INTEGER_ZERO;

    // Service
    @Inject
    private transient IDirectoryMultiviewService _directoryMultiviewService;

    // Variables
    private String _strTitle;
    private String _strName;
    private int _nPosition;
    private boolean _bIsActive = Boolean.FALSE;
    private int _nRecordNumber = DEFAULT_RECORD_NUMBER;
    private Map<String, RecordAssignment> _mapRecordAssignment = new LinkedHashMap<>( );

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * Set the title of the panel
     * 
     * @param strTitle
     *            The title of the panel
     */
    protected void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return _strName;
    }

    /**
     * Set the name of the panel
     * 
     * @param strName
     *            The name of the panel to set
     */
    protected void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPosition( )
    {
        return _nPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition( int nPosition )
    {
        _nPosition = nPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive( )
    {
        return _bIsActive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActive( boolean bActive )
    {
        _bIsActive = bActive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRecordNumber( )
    {
        return _nRecordNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRecordNumber( int nRecordNumber )
    {
        _nRecordNumber = nRecordNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, RecordAssignment> getRecordAssignmentMap( )
    {
        return _mapRecordAssignment;
    }

    /**
     * Set the RecordAssignmentMap for the panel
     * 
     * @param mapRecordAssignment
     *            The RecordAssignmentMap to set
     */
    protected void setRecordAssignmentMap( Map<String, RecordAssignment> mapRecordAssignment )
    {
        _mapRecordAssignment = mapRecordAssignment;
    }

    /**
     * Initialize a Panel with all its attributes
     * 
     * @param request
     *            The request to retrieve the parameter values from
     * @param collectionDirectory
     *            The collection of directory which contain the records to associate to the panel
     * @param strPanelName
     *            The name of the panel
     * @param strPanelKeyTitle
     *            The key of the title of the panel
     * @param recordAssignmentFilter
     *            The filter to use to retrieve the records for the panel
     */
    protected void initPanel( HttpServletRequest request, Collection<Directory> collectionDirectory, String strPanelName, String strPanelKeyTitle,
            RecordAssignmentFilter recordAssignmentFilter )
    {
        // set the name of the panel
        setName( strPanelName );

        // Set the title of the panel
        setTitle( I18nService.getLocalizedString( strPanelKeyTitle, request.getLocale( ) ) );

        // Check if its the active panel
        boolean bIsSelectedPanel = isSelectedPanel( request, strPanelName );
        setActive( bIsSelectedPanel );

        // Create the RecordAssignmentMap of the panel and calculate the number of records
        Map<String, RecordAssignment> mapRecordAssignment = createRecordAssignmentMap( request, recordAssignmentFilter, collectionDirectory );
        setRecordAssignmentMap( mapRecordAssignment );
        setRecordNumber( mapRecordAssignment.size( ) );
    }

    /**
     * Make the record search with the given filter for the specified list of Directory and return the number of records that the search result returns.
     * 
     * @param request
     *            The request to retrieve the parameters from
     * @param filter
     *            The filter to use for the search
     * @param collectionDirectory
     *            The list of Directory to search on
     * @return the number of records of the result of the search
     */
    protected Map<String, RecordAssignment> createRecordAssignmentMap( HttpServletRequest request, RecordAssignmentFilter filter,
            Collection<Directory> collectionDirectory )
    {
        String strSearchText = request.getParameter( DirectoryMultiviewConstants.PARAMETER_SEARCHED_TEXT );
        List<RecordAssignment> recordAssignmentList = AssignmentService.getRecordAssignmentFiltredList( filter );
        Map<String, RecordAssignment> recordAssignmentMap = _directoryMultiviewService.populateRecordAssignmentMap( recordAssignmentList );

        return DirectoryMultiviewSearchService.filterBySearchedText( recordAssignmentMap, collectionDirectory, request, DirectoryUtils.getPlugin( ),
                strSearchText );
    }

    /**
     * Check if the panel is the selected panel or not. Activate the default panel if not found
     * 
     * @param request
     *            The HttpServletRequest to retrieve the information from
     * @param strPanelName
     *            The name of the panel to analyze
     * @return true if the panel of the given name is the panel to analyze false otherwise
     */
    protected boolean isSelectedPanel( HttpServletRequest request, String strPanelName )
    {
        boolean bIsSelectedPanel = Boolean.FALSE;

        // We will retrieve the name of the current selected panel
        String strRecordListPanelSelected = request.getParameter( DirectoryMultiviewConstants.PARAMETER_SELECTED_PANEL );
        if ( StringUtils.isNotBlank( strRecordListPanelSelected ) )
        {
            bIsSelectedPanel = strPanelName.equals( strRecordListPanelSelected );
        }
        else
        {
            // If there is no selected panel we will see if there was a previous selected one
            String strPreviousRecordListPanelSelected = request.getParameter( DirectoryMultiviewConstants.PARAMETER_CURRENT_SELECTED_PANEL );
            if ( StringUtils.isNotBlank( strPreviousRecordListPanelSelected ) )
            {
                bIsSelectedPanel = strPanelName.equals( strPreviousRecordListPanelSelected );
            }
        }

        return bIsSelectedPanel;
    }
}
