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

import fr.paris.lutece.plugins.directory.business.DirectoryFilter;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.list.RecordListFacade;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.IRecordPanelDisplay;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.service.DirectoryResourceIdService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

/**
 * Service for the module-directory-multiview
 */
public class DirectoryMultiviewService implements IDirectoryMultiviewService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void populateRecordColumns( IRecordPanel recordPanel, List<IRecordColumn> listRecordColumn, List<IRecordFilter> listRecordFilter )
    {
        RecordListFacade recordListFacade = SpringContextService.getBean( RecordListFacade.BEAN_NAME );
        recordListFacade.populateRecordColumns( recordPanel, listRecordColumn, listRecordFilter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordPanelDisplay findActiveRecordPanel( List<IRecordPanelDisplay> listRecordPanelDisplay )
    {
        IRecordPanelDisplay recordPanelDisplayActive = null;

        if ( listRecordPanelDisplay != null && !listRecordPanelDisplay.isEmpty( ) )
        {
            for ( IRecordPanelDisplay recordPanelDisplay : listRecordPanelDisplay )
            {
                if ( recordPanelDisplay.isActive( ) )
                {
                    recordPanelDisplayActive = recordPanelDisplay;
                    break;
                }
            }
        }

        return recordPanelDisplayActive;
    }

    /**
     * Filter by authorized directory
     * 
     * @param recordPanel
     * @param request
     */
    @Override
    public void filterByAuthorizedDirectory( IRecordPanel recordPanel, HttpServletRequest request )
    {
        List<Integer> listDirectory = RBACService
                .getAuthorizedCollection( DirectoryHome.getDirectoryList( new DirectoryFilter( ), PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME ) ),
                        DirectoryResourceIdService.PERMISSION_VISUALISATION_RECORD, AdminUserService.getAdminUser( request ) ).stream( )
                .map( directory -> directory.getIdDirectory( ) ).collect( Collectors.toList( ) );

        recordPanel.setDirectoryRecordItemList( recordPanel.getDirectoryRecordItemList( ).stream( )
                .filter( item -> listDirectory.contains( item.getIdDirectory( ) ) ).collect( Collectors.toList( ) ) );

    }
}
