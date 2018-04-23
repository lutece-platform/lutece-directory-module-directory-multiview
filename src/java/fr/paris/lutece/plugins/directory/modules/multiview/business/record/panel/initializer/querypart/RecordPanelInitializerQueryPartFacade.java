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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart.factory.IRecordPanelInitializerQueryPartFactory;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Facade for the RecordPanelInitializerQueryPart objects
 */
public class RecordPanelInitializerQueryPartFacade
{
    // Variables
    private final List<IRecordPanelInitializerQueryPartFactory> _listRecordPanelInitializerQueryPartFactory;

    /**
     * Constructor
     */
    public RecordPanelInitializerQueryPartFacade( )
    {
        _listRecordPanelInitializerQueryPartFactory = SpringContextService.getBeansOfType( IRecordPanelInitializerQueryPartFactory.class );
    }
    
    /**
     * Constructor
     * 
     * @param listRecordPanelInitializerQueryPartFactory
     *          The list of IRecordPanelInitializerQueryPartFactory to use for the Facade
     */
    public RecordPanelInitializerQueryPartFacade( List<IRecordPanelInitializerQueryPartFactory> listRecordPanelInitializerQueryPartFactory )
    {
        _listRecordPanelInitializerQueryPartFactory = listRecordPanelInitializerQueryPartFactory;
    }

    /**
     * Retrieve the IRecordPanelInitializerQueryPart associate to the given RecordPanelInitializer
     * 
     * @param recordPanelInitializer
     *            The RecordPanelInitializer to retrieve the RecordPanelInitializerQueryPart associated
     * @return the IRecordPanelInitializerQueryPart associate to the given RecordPanelInitializer or null if not found
     */
    public IRecordPanelInitializerQueryPart getRecordPanelInitializerQueryPart( IRecordPanelInitializer recordPanelInitializer )
    {
        IRecordPanelInitializerQueryPart recordPanelInitializerQueryPart = null;

        if ( recordPanelInitializer != null && !CollectionUtils.isEmpty( _listRecordPanelInitializerQueryPartFactory ) )
        {
            for ( IRecordPanelInitializerQueryPartFactory recordPanelInitializerQueryPartFactory : _listRecordPanelInitializerQueryPartFactory )
            {
                recordPanelInitializerQueryPart = recordPanelInitializerQueryPartFactory.buildRecordPanelInitializerQueryPart( recordPanelInitializer );

                if ( recordPanelInitializerQueryPart != null )
                {
                    break;
                }
            }
        }

        return recordPanelInitializerQueryPart;
    }
}
