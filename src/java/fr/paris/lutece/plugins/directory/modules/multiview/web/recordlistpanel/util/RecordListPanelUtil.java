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
package fr.paris.lutece.plugins.directory.modules.multiview.web.recordlistpanel.util;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.web.recordlistpanel.IRecordListPanel;

/**
 * Utility class for RecordListPanel
 */
public final class RecordListPanelUtil
{
    /**
     * Constructor - Never called
     */
    private RecordListPanelUtil( )
    {
        
    }
    
    /**
     * Find the RecordListPanel which is active in the given list
     * 
     * @param listRecordListPanel
     *          The list to retrieve the active RecordListPanel
     * @return the RecordListPanel which is active or null if not found
     */
    public static IRecordListPanel findActiveRecordListPanel( List<IRecordListPanel> listRecordListPanel )
    {
        IRecordListPanel recordListPanelActive = null;
        
        if ( listRecordListPanel != null && !listRecordListPanel.isEmpty( ) )
        {
            for ( IRecordListPanel recordListPanel : listRecordListPanel )
            {
                if ( recordListPanel.isActive( ) )
                {
                    recordListPanelActive = recordListPanel;
                    break;
                }
            }
            
            // If there is no active panel we will select the first panel of the list (which must have the first position)
            if ( recordListPanelActive == null )
            {
                recordListPanelActive = listRecordListPanel.get( NumberUtils.INTEGER_ZERO );
                recordListPanelActive.setActive( Boolean.TRUE );
            }
        }
        
        return recordListPanelActive;
    }
}
