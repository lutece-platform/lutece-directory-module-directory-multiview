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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.configuration.RecordPanelConfiguration;

/**
 * Abstract class for implementation of the IRecordPanel
 */
public abstract class AbstractRecordPanel implements IRecordPanel
{
    // Constants
    private static final String DEFAULT_RECORD_PANEL_TITLE = StringUtils.EMPTY;
    private static final String DEFAULT_RECORD_PANEL_TECHNICAL_CODE = StringUtils.EMPTY;

    // Variables
    private RecordPanelConfiguration _recordPanelConfiguration;
    private List<DirectoryRecordItem> _listDirectoryRecordItem = new ArrayList<>( );

    /**
     * {@inheritDoc}
     */
    @Override
    public RecordPanelConfiguration getRecordPanelConfiguration( )
    {
        return _recordPanelConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( )
    {
        String strTitle = DEFAULT_RECORD_PANEL_TITLE;

        if ( _recordPanelConfiguration != null )
        {
            strTitle = _recordPanelConfiguration.getTitle( );
        }

        return strTitle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTechnicalCode( )
    {
        String strTechnicalCode = DEFAULT_RECORD_PANEL_TECHNICAL_CODE;

        if ( _recordPanelConfiguration != null )
        {
            strTechnicalCode = _recordPanelConfiguration.getTechnicalCode( );
        }

        return strTechnicalCode;
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
     * Set the RecordPanelConfiguration of the RecordPanel
     * 
     * @param recordPanelConfiguration
     *            The RecordPanelconfiguration to set the RecordPanel
     */
    protected void setRecordPanelConfiguration( RecordPanelConfiguration recordPanelConfiguration )
    {
        _recordPanelConfiguration = recordPanelConfiguration;
    }
}
