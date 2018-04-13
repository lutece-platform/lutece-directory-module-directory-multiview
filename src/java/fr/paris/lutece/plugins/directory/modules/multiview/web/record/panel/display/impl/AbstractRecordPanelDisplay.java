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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.RecordFilterItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.IRecordPanelDisplay;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.util.IRecordListPosition;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;

/**
 * Abstract class for RecordPanelDisplay class
 */
public abstract class AbstractRecordPanelDisplay implements IRecordPanelDisplay, IRecordListPosition
{
    // Template
    private static final String TEMPLATE_RECORD_PANEL = "admin/plugins/directory/modules/multiview/panel/record_panel_template.html";

    // Marks
    private static final String MARK_PANEL_ACTIVE = "panel_active";
    private static final String MARK_PANEL_TECHNICAL_CODE = "panel_technical_code";
    private static final String MARK_PANEL_TITLE = "panel_title";
    private static final String MARK_PANEL_RECORD_NUMBER = "panel_recordNumber";

    // Variables
    private int _nPosition;
    private boolean _bActive;
    private String _strTemplate;
    private IRecordPanelDisplay _recordFilterPanelDisplay;
    private List<DirectoryRecordItem> _listDirectoryRecordItem = new ArrayList<>( );
    private IRecordFilter _recordFilter;
    private IRecordPanel _recordPanel;

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
    public int getRecordNumber( )
    {
        return _listDirectoryRecordItem.size( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplate( )
    {
        return _strTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive( )
    {
        return _bActive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActive( boolean bActive )
    {
        _bActive = bActive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTechnicalCode( )
    {
        String strTechnicalCode = StringUtils.EMPTY;

        IRecordPanel recordPanel = getRecordPanel( );
        if ( recordPanel != null )
        {
            strTechnicalCode = recordPanel.getTechnicalCode( );
        }

        return strTechnicalCode;
    }

    /**
     * Return the RecordFilterPanelDisplay of the RecordPanelDisplay
     * 
     * @return the RecordFilterPanelDisplay of the RecordPanelDisplay
     */
    public IRecordPanelDisplay getRecordFilterDisplay( )
    {
        return _recordFilterPanelDisplay;
    }

    /**
     * Set the RecordFilterPanelDisplay to the panel
     * 
     * @param recordFilterPanelDisplay
     *            The RecordFilterPanelDisplay to set to the panel
     */
    public void setRecordFilterListPanelDisplay( IRecordPanelDisplay recordFilterPanelDisplay )
    {
        _recordFilterPanelDisplay = recordFilterPanelDisplay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DirectoryRecordItem> getDirectoryRecordItemList( )
    {
        return _listDirectoryRecordItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDirectoryRecordItemList( List<DirectoryRecordItem> listDirectoryRecordItem )
    {
        _listDirectoryRecordItem = listDirectoryRecordItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordFilter getRecordFilter( )
    {
        return _recordFilter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRecordFilter( IRecordFilter recordFilter )
    {
        _recordFilter = recordFilter;
    }

    /**
     * Return the recordPanel of the RecordPanelDisplay
     * 
     * @return the recordPanel
     */
    public IRecordPanel getRecordPanel( )
    {
        return _recordPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRecordPanel( IRecordPanel recordPanel )
    {
        _recordPanel = recordPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void configureRecordPanelDisplay( HttpServletRequest request );

    /**
     * Get the map of all parameter names and values used by the filter
     * 
     * @param request
     *            The request used to retrieve the informations of the filter
     * @return the map which contains all the parameter names and values of the filter
     */
    protected abstract Map<String, Object> getFilterDisplayMapValues( HttpServletRequest request );

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildTemplate( Locale locale )
    {
        String strTechnicalCode = StringUtils.EMPTY;
        String strTitleKey = StringUtils.EMPTY;

        IRecordPanel recordPanel = getRecordPanel( );
        if ( recordPanel != null )
        {
            strTechnicalCode = recordPanel.getTechnicalCode( );
            strTitleKey = I18nService.getLocalizedString( recordPanel.getTitleKey( ), locale );
        }

        Map<String, Object> model = new LinkedHashMap<>( );
        model.put( MARK_PANEL_ACTIVE, _bActive );
        model.put( MARK_PANEL_TECHNICAL_CODE, strTechnicalCode );
        model.put( MARK_PANEL_TITLE, strTitleKey );
        model.put( MARK_PANEL_RECORD_NUMBER, getRecordNumber( ) );

        String strRecordPanelDisplayTemplate = AppTemplateService.getTemplate( TEMPLATE_RECORD_PANEL, locale, model ).getHtml( );
        _strTemplate = strRecordPanelDisplayTemplate;

        return _strTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecordFilterItem createRecordFilterItem( HttpServletRequest request )
    {
        RecordFilterItem recordFilterItem = null;

        if ( _recordFilter != null )
        {
            recordFilterItem = new RecordFilterItem( );

            Map<String, Object> mapKeyNameValues = getFilterDisplayMapValues( request );
            recordFilterItem.setMapFilterNameValues( mapKeyNameValues );

            _recordFilter.setRecordFilterItem( recordFilterItem );
        }

        return recordFilterItem;
    }

    /**
     * Initialize the RecordPanelDisplay by setting its name, its title and if is active or not
     * 
     * @param request
     *            The request to retrieve the information from
     */
    protected void initPanel( HttpServletRequest request )
    {
        String strPanelTechnicalCode = StringUtils.EMPTY;

        IRecordPanel recordPanel = getRecordPanel( );
        if ( recordPanel != null )
        {
            strPanelTechnicalCode = recordPanel.getTechnicalCode( );
        }

        boolean bIsSelectedPanel = isSelectedPanel( request, strPanelTechnicalCode );
        setActive( bIsSelectedPanel );
    }

    /**
     * Check if the panel is the selected panel or not. Activate the default panel if not found
     * 
     * @param request
     *            The HttpServletRequest to retrieve the information from
     * @param strPanelTechnicalCode
     *            The name of the panel to analyze
     * @return true if the panel of the given name is the panel to analyze false otherwise
     */
    private boolean isSelectedPanel( HttpServletRequest request, String strPanelTechnicalCode )
    {
        boolean bIsSelectedPanel = Boolean.FALSE;

        // We will retrieve the name of the current selected panel
        String strRecordPanelSelected = request.getParameter( DirectoryMultiviewConstants.PARAMETER_SELECTED_PANEL );
        if ( StringUtils.isNotBlank( strRecordPanelSelected ) )
        {
            bIsSelectedPanel = strPanelTechnicalCode.equals( strRecordPanelSelected );
        }
        else
        {
            // If there is no selected panel we will see if there was a previous selected one
            String strPreviousRecordPanelSelected = request.getParameter( DirectoryMultiviewConstants.PARAMETER_CURRENT_SELECTED_PANEL );
            if ( StringUtils.isNotBlank( strPreviousRecordPanelSelected ) )
            {
                bIsSelectedPanel = strPanelTechnicalCode.equals( strPreviousRecordPanelSelected );
            }
        }

        return bIsSelectedPanel;
    }
}
