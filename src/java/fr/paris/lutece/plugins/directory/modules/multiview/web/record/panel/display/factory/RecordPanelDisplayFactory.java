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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.configuration.RecordPanelConfiguration;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer;
import fr.paris.lutece.plugins.directory.modules.multiview.util.DirectoryMultiviewConstants;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.IRecordPanelDisplay;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.IRecordPanelDisplayInitializer;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.factory.IRecordPanelDisplayInitializerFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.initializer.factory.RecordDisplayInitializerFactoryFacade;
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
     * @return the list of all RecordPanelDisplay ordered by their position
     */
    public static List<IRecordPanelDisplay> createRecordPanelDisplayList( HttpServletRequest request, List<IRecordPanel> listRecordPanel )
    {
        List<IRecordPanelDisplay> listRecordPanelDisplay = new ArrayList<>( );
        List<IRecordPanelDisplayFactory> listRecordPanelDisplayFactory = RecordPanelDisplayFactoryFacade.buildRecordPanelDisplayFactoryList( );

        if ( !CollectionUtils.isEmpty( listRecordPanelDisplayFactory ) )
        {
            IRecordPanelDisplay recordPanelDisplay = null;
            for ( IRecordPanel recordPanel : listRecordPanel )
            {
                for ( IRecordPanelDisplayFactory recordPanelDisplayFactory : listRecordPanelDisplayFactory )
                {
                    recordPanelDisplay = recordPanelDisplayFactory.buildRecordPanelDisplay( recordPanel );
                    if ( recordPanelDisplay != null )
                    {
                        configureRecordPanelDisplay( request, recordPanelDisplay );

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
     * Configure the RecordPanelDisplay by defining if its active or not and build all the RecordPanelInitializer with their RecordParameters from its
     * RecordPanel
     * 
     * @param request
     *            The request to retrieve the information from
     * @param recordPanelDisplay
     *            The recordPanelInitializer to configure with the information from the request
     */
    private static void configureRecordPanelDisplay( HttpServletRequest request, IRecordPanelDisplay recordPanelDisplay )
    {
        boolean bIsSelectedPanel = isSelectedPanel( request, recordPanelDisplay.getTechnicalCode( ) );
        recordPanelDisplay.setActive( bIsSelectedPanel );

        buildRecordPanelDisplayInitializer( request, recordPanelDisplay.getRecordPanel( ) );
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
    private static boolean isSelectedPanel( HttpServletRequest request, String strPanelTechnicalCode )
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

    /**
     * Build the list of all RecordPanelDisplayInitializer of all RecordPanelInitializer of the RecordPänel
     * 
     * @param request
     *            The HttpServletRequest used to build the list of all RecordPanelDisplayInitializer
     * @param recordPanel
     *            The IRecordPanel to the RecordPanelInitializer from
     */
    private static void buildRecordPanelDisplayInitializer( HttpServletRequest request, IRecordPanel recordPanel )
    {
        List<IRecordPanelDisplayInitializerFactory> listRecordPanelDisplayInitializerFactory = RecordDisplayInitializerFactoryFacade
                .buildRecordPanelDisplayInitializerList( );

        if ( !CollectionUtils.isEmpty( listRecordPanelDisplayInitializerFactory ) && recordPanel != null )
        {
            RecordPanelConfiguration recordPanelConfiguration = recordPanel.getRecordPanelConfiguration( );

            if ( recordPanelConfiguration != null )
            {
                for ( IRecordPanelDisplayInitializerFactory recordPanelDisplayInitializerFactory : listRecordPanelDisplayInitializerFactory )
                {
                    List<IRecordPanelInitializer> listRecordPanelInitializer = recordPanelConfiguration.getListRecordPanelInitializer( );

                    buildPanelDisplayInitializerRecordParameters( request, recordPanelDisplayInitializerFactory, listRecordPanelInitializer );
                }
            }
        }
    }

    /**
     * Build the RecordPanelDisplayInitializer associated to the given IRecordPanelDisplayInitializerFactory from the specified list of IRecordPanelInitializer
     * and build its RecordParameters with the request
     * 
     * @param request
     *            The request used to build the RecordParameters of the RecordPanelInitializer
     * @param recordPanelDisplayInitializerFactory
     *            The IRecordPanelDisplayInitializerFactory used to build the IRecordPanelDisplayInitializer
     * @param listRecordPanelInitializer
     *            The list of all RecordPanelInitializer to retrieve those which is associated to the given IRecordPanelDisplayInitializerFactory
     */
    private static void buildPanelDisplayInitializerRecordParameters( HttpServletRequest request,
            IRecordPanelDisplayInitializerFactory recordPanelDisplayInitializerFactory, List<IRecordPanelInitializer> listRecordPanelInitializer )
    {
        if ( recordPanelDisplayInitializerFactory != null && !CollectionUtils.isEmpty( listRecordPanelInitializer ) )
        {
            for ( IRecordPanelInitializer recordPanelInitializer : listRecordPanelInitializer )
            {
                IRecordPanelDisplayInitializer recordPanelDisplayInitializer = recordPanelDisplayInitializerFactory
                        .buildRecordPanelDisplay( recordPanelInitializer );
                if ( recordPanelDisplayInitializer != null )
                {
                    recordPanelDisplayInitializer.setRecordPanelInitializer( recordPanelInitializer );
                    recordPanelDisplayInitializer.buildRecordParameters( request );
                }
            }
        }
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
