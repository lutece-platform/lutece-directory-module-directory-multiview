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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.factory.IRecordColumnDisplayFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.column.display.factory.RecordColumnDisplayFactoryFacade;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.util.RecordListPositionComparator;

/**
 * Factory for RecordColumnDisplay objects
 */
public class RecordColumnDisplayFactory
{
    /**
     * Create the list of all RecordColumnDisplay ordered by their position
     * 
     * @param listRecordColumn
     *            The list of IRecordColumn to use for build the list of RecordColumnDisplay
     * @return the list of all RecordColumnDisplay ordered by their position
     */
    public List<IRecordColumnDisplay> createRecordColumnDisplayList( List<IRecordColumn> listRecordColumn )
    {
        List<IRecordColumnDisplay> listRecordColumnDisplay = new ArrayList<>( );
        List<IRecordColumnDisplayFactory> listRecordColumnDisplayFactory = new RecordColumnDisplayFactoryFacade( ).buildRecordColumnDisplayFactoryList( );

        if ( listRecordColumn != null && !listRecordColumn.isEmpty( ) && listRecordColumnDisplayFactory != null && !listRecordColumnDisplayFactory.isEmpty( ) )
        {
            for ( IRecordColumn recordColumn : listRecordColumn )
            {
                IRecordColumnDisplay recordColumnDisplay = null;
                for ( IRecordColumnDisplayFactory recordColumnDisplayFactory : listRecordColumnDisplayFactory )
                {
                    recordColumnDisplay = recordColumnDisplayFactory.buildRecordColumnDisplay( recordColumn );
                    if ( recordColumnDisplay != null )
                    {
                        recordColumnDisplay.setPosition( recordColumn.getRecordColumnPosition( ) );
                        listRecordColumnDisplay.add( recordColumnDisplay );
                        break;
                    }
                }
            }

            // Sort the list by the position of each elements
            Collections.sort( listRecordColumnDisplay, new RecordListPositionComparator( ) );
        }

        return listRecordColumnDisplay;
    }
}
