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

import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.configuration.RecordFilterConfiguration;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.impl.standalone.panel.RecordFilterPanelRecords;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.impl.RecordPanelRecords;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.IRecordPanelDisplay;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.impl.RecordPanelRecordsDisplay;

/**
 * Implementation of the IRecordFilterDAO for a Factory on a PanelRecords filter
 */
public class RecordPanelRecordsDisplayFactory implements IRecordPanelDisplayFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordPanelDisplay buildRecordPanelDisplay( IRecordPanel recordPanel, List<IRecordFilter> listRecordFilter )
    {
        RecordPanelRecordsDisplay recordPanelRecordsFilterDisplay = null;

        if ( recordPanel instanceof RecordPanelRecords )
        {
            recordPanelRecordsFilterDisplay = new RecordPanelRecordsDisplay( );
            recordPanelRecordsFilterDisplay.setRecordPanel( recordPanel );

            // Associate the filter to the RecordFilterPanelDisplay
            manageRecordFilterPanel( recordPanelRecordsFilterDisplay, listRecordFilter );
        }

        return recordPanelRecordsFilterDisplay;
    }

    /**
     * Retrieve the RecordFilter associated to the RecordPanelRecordsFilterDisplay and configure it
     * 
     * @param recordPanelRecordsFilterDisplay
     *            The RecordPanelRecordsFilterDisplay to configure
     * @param listRecordFilter
     *            The list of IRecordFilter to retrieve the RecordFilter of the given RecordPanelRecordsFilterDisplay
     */
    private void manageRecordFilterPanel( RecordPanelRecordsDisplay recordPanelRecordsFilterDisplay, List<IRecordFilter> listRecordFilter )
    {
        for ( IRecordFilter recordFilter : listRecordFilter )
        {
            if ( recordFilter instanceof RecordFilterPanelRecords )
            {
                recordPanelRecordsFilterDisplay.setRecordFilter( recordFilter );

                RecordFilterConfiguration recordFilterConfiguration = recordFilter.getRecordFilterConfiguration( );
                if ( recordFilterConfiguration != null )
                {
                    recordPanelRecordsFilterDisplay.setPosition( recordFilterConfiguration.getPosition( ) );
                }

                break;
            }
        }
    }
}
