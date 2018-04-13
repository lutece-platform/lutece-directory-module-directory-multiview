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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.configuration.RecordFilterConfiguration;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.impl.standalone.IRecordFilterStandalone;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.impl.standalone.panel.IRecordFilterStandalonePanel;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.factory.IRecordFilterDisplayFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.display.factory.RecordFilterDisplayFactoryFacade;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.util.RecordListPositionComparator;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Factory for RecordFilterDisplay objects
 */
public final class RecordFilterDisplayFactory
{
    /**
     * Constructor
     */
    private RecordFilterDisplayFactory( )
    {

    }

    /**
     * Return the list of all RecordFilterDisplay ordered by their position
     * 
     * @param request
     *            The HttpServletRequest used to create the list of all RecordFilterDisplay
     * @param listRecordFilter
     *            The list of IRecordFilter used for building IRecordFilterDisplay
     * @return the list of all RecordFilterDisplay ordered by their position
     */
    public static List<IRecordFilterDisplay> createRecordFilterDisplayList( HttpServletRequest request, List<IRecordFilter> listRecordFilter )
    {
        List<IRecordFilterDisplay> listRecordFilterDisplay = new ArrayList<>( );
        List<IRecordFilterDisplayFactory> listRecordFilterDisplayFactory = RecordFilterDisplayFactoryFacade.buildRecordFilterDisplayFactoryList( );

        if ( listRecordFilter != null && !listRecordFilter.isEmpty( ) && listRecordFilterDisplayFactory != null && !listRecordFilterDisplayFactory.isEmpty( ) )
        {
            for ( IRecordFilter recordFilter : listRecordFilter )
            {
                IRecordFilterDisplay recordFilterDisplay = null;
                for ( IRecordFilterDisplayFactory recordFilterDisplayFactory : listRecordFilterDisplayFactory )
                {
                    recordFilterDisplay = recordFilterDisplayFactory.buildFilterDisplay( recordFilter );
                    if ( recordFilterDisplay != null )
                    {
                        manageRecordFilterDisplay( request, recordFilter, recordFilterDisplay );

                        listRecordFilterDisplay.add( recordFilterDisplay );
                        break;
                    }
                }
            }

            // Sort the list by the position of each elements
            Collections.sort( listRecordFilterDisplay, new RecordListPositionComparator( ) );
        }

        return listRecordFilterDisplay;
    }

    /**
     * Create the RecordFilterItem for the given RecordFilterDisplay and set is position from the given RecordFilter
     * 
     * @param request
     *            The request used to build the RecordFilterItem of the RecordFilterDisplay
     * @param recordFilter
     *            The RecordFilter used to retrieve the position of the RecordFilterDisplay
     * @param recordFilterDisplay
     *            The RecordFilterDisplay from which the RecordFilterItem must be created
     */
    private static void manageRecordFilterDisplay( HttpServletRequest request, IRecordFilter recordFilter, IRecordFilterDisplay recordFilterDisplay )
    {
        recordFilterDisplay.createRecordFilterItem( request );

        RecordFilterConfiguration recordFilterConfiguration = recordFilter.getRecordFilterConfiguration( );
        if ( recordFilterConfiguration != null )
        {
            recordFilterDisplay.setPosition( recordFilterConfiguration.getPosition( ) );
        }
    }

    /**
     * Build the list of all RecordFilterStandalone without all RecordFilterStandalonePanel except those given as parameter which is added to the result list
     * 
     * @param recordFilterStandalonePanel
     *            The RecordFilterStandalonePanel to add to the result list
     * @return the list of all RecordFilterStandalone without all RecordFilterStandalonePanel except those given as parameter which is added to the result list
     */
    public static List<IRecordFilter> buildListRecordFilter( IRecordFilterStandalonePanel recordFilterStandalonePanel )
    {
        List<IRecordFilter> listRecordFilter = new ArrayList<>( SpringContextService.getBeansOfType( IRecordFilterStandalone.class ) );

        // Remove all filter associated to the list panel objects
        if ( listRecordFilter != null && !listRecordFilter.isEmpty( ) )
        {
            Iterator<IRecordFilter> iteratorRecordFilter = listRecordFilter.iterator( );
            while ( iteratorRecordFilter.hasNext( ) )
            {
                if ( iteratorRecordFilter.next( ) instanceof IRecordFilterStandalonePanel )
                {
                    iteratorRecordFilter.remove( );
                }
            }
        }

        // Add the filter of the default list panel object
        listRecordFilter.add( (IRecordFilter) recordFilterStandalonePanel );

        return listRecordFilter;
    }
}
