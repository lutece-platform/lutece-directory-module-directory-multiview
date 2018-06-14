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
package fr.paris.lutece.plugins.directory.modules.multiview.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.list.RecordListFacade;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.factory.RecordPanelDisplayFactory;

/**
 * Implementation of the IDirectoryMultiviewAuthorizationService interface
 */
public class DirectoryMultiviewAuthorizationService implements IDirectoryMultiviewAuthorizationService
{
    // Variables
    private final IRecordPanel _recordPanel;
    private final RecordListFacade _recordListFacade;
    private final RecordColumnFactory _recordColumnFactory;

    /**
     * Constructor
     * 
     * @param recordPanel
     *            The RecordPanel on which the authorization is based
     * @param recordListFacade
     *            The RecordListFacade to use by the service
     * @param recordColumnFactory
     *            The RecordColumnFactory to use by the service
     */
    public DirectoryMultiviewAuthorizationService( IRecordPanel recordPanel, RecordListFacade recordListFacade, RecordColumnFactory recordColumnFactory )
    {
        _recordPanel = recordPanel;
        _recordListFacade = recordListFacade;
        _recordColumnFactory = recordColumnFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserAuthorizedOnRecord( HttpServletRequest request, int nIdRecord )
    {
        boolean bIsUserAuthorizedOnRecord = Boolean.FALSE;

        if ( nIdRecord != NumberUtils.INTEGER_MINUS_ONE && _recordPanel != null )
        {
            List<IRecordColumn> listRecordColumn = _recordColumnFactory.buildRecordColumnList( );

            // Rebuild all the RecordPanelInitializer to reset the previous data
            RecordPanelDisplayFactory recordPanelDisplayFactory = new RecordPanelDisplayFactory( );
            recordPanelDisplayFactory.buildRecordPanelDisplayInitializer( request, _recordPanel );

            _recordListFacade.populateRecordColumns( _recordPanel, listRecordColumn, new ArrayList<>( ) );
            List<DirectoryRecordItem> listDirectoryRecordItem = _recordPanel.getDirectoryRecordItemList( );

            if ( listDirectoryRecordItem != null && !listDirectoryRecordItem.isEmpty( ) )
            {
                List<Integer> listIdRecord = listDirectoryRecordItem.stream( ).map( DirectoryRecordItem::getIdRecord ).collect( Collectors.toList( ) );
                bIsUserAuthorizedOnRecord = listIdRecord.contains( nIdRecord );
            }
        }

        return bIsUserAuthorizedOnRecord;
    }
}
