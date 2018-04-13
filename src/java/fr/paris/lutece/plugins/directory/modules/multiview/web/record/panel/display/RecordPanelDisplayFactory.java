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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.factory.IRecordPanelDisplayFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.factory.RecordPanelDisplayFactoryFacade;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.util.RecordListPositionComparator;

/**
 * Factory for RecordPanel objects
 */
public final class RecordPanelDisplayFactory
{
    // Constants
    private static final int INDEX_LIST_PANEL_FIRST_POSITION = NumberUtils.INTEGER_ZERO;
    private static final boolean ACTIVE_LIST_PANEL = Boolean.TRUE;

    /**
     * Constructor
     */
    private RecordPanelDisplayFactory( )
    {

    }

    /**
     * Create the list of all RecordPanelDisplay ordered by their position
     * 
     * @param request
     *            The HttpServletRequest to retrieve the information from
     * @param listRecordPanel
     *            The list of all IRecordPanel used for building IRecordFilterDisplay
     * @param listRecordFilter
     *            The list of IRecordFilter used for building IRecordFilterDisplay
     * @return the list of all RecordPanelDisplay ordered by their position
     */
    public static List<IRecordPanelDisplay> createRecordPanelDisplayList( HttpServletRequest request, List<IRecordPanel> listRecordPanel,
            List<IRecordFilter> listRecordFilter )
    {
        List<IRecordPanelDisplay> listRecordPanelDisplay = new ArrayList<>( );
        List<IRecordPanelDisplayFactory> listRecordPanelDisplayFactory = RecordPanelDisplayFactoryFacade.buildRecordPanelDisplayFactoryList( );

        if ( listRecordFilter != null && !listRecordFilter.isEmpty( ) && listRecordPanelDisplayFactory != null && !listRecordPanelDisplayFactory.isEmpty( ) )
        {
            IRecordPanelDisplay recordPanelDisplay = null;
            for ( IRecordPanel recordPanel : listRecordPanel )
            {
                for ( IRecordPanelDisplayFactory recordPanelDisplayFactory : listRecordPanelDisplayFactory )
                {
                    recordPanelDisplay = recordPanelDisplayFactory.buildRecordPanelDisplay( recordPanel, listRecordFilter );
                    if ( recordPanelDisplay != null )
                    {
                        recordPanelDisplay.configureRecordPanelDisplay( request );
                        recordPanelDisplay.createRecordFilterItem( request );

                        listRecordPanelDisplay.add( recordPanelDisplay );
                        break;
                    }
                }
            }

            // Sort the list by the position of each elements
            Collections.sort( listRecordPanelDisplay, new RecordListPositionComparator( ) );

            // Manage the active RecordDisplayPanel of the list
            manageActiveRecordPanelDisplay( listRecordPanelDisplay );
        }

        return listRecordPanelDisplay;
    }

    /**
     * Check if there is an active RecordDisplay Panel in the given list and if there is no one set the first element in the list (which is in first position)
     * as active
     * 
     * @param listRecordPanelDisplay
     *            The list of RecordDisplayPanel to analyze
     */
    private static void manageActiveRecordPanelDisplay( List<IRecordPanelDisplay> listRecordPanelDisplay )
    {
        boolean bActivePanelPresent = Boolean.FALSE;

        for ( IRecordPanelDisplay recordPanelDisplaySorted : listRecordPanelDisplay )
        {
            if ( recordPanelDisplaySorted.isActive( ) )
            {
                bActivePanelPresent = Boolean.TRUE;
                break;
            }
        }

        if ( !bActivePanelPresent )
        {
            listRecordPanelDisplay.get( INDEX_LIST_PANEL_FIRST_POSITION ).setActive( ACTIVE_LIST_PANEL );
        }
    }
}
