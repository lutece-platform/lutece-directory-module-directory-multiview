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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart;

import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.querypart.factory.IRecordFilterQueryPartFactory;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Facade for a RecordFilter to build its request
 */
public class RecordFilterQueryPartFacade
{
    // Variables
    private final List<IRecordFilterQueryPartFactory> _listRecordFilterQueryPartFactory;

    /**
     * Constructor
     */
    public RecordFilterQueryPartFacade( )
    {
        _listRecordFilterQueryPartFactory = SpringContextService.getBeansOfType( IRecordFilterQueryPartFactory.class );
    }

    /**
     * Constructor
     * 
     * @param listRecordFilterQueryPartFactory
     *            The list of IRecordFilterQueryPartFactory to use for the Facade
     */
    public RecordFilterQueryPartFacade( List<IRecordFilterQueryPartFactory> listRecordFilterQueryPartFactory )
    {
        _listRecordFilterQueryPartFactory = listRecordFilterQueryPartFactory;
    }

    /**
     * Return the appropriate QueryPart for the given RecordFilter
     * 
     * @param recordFilter
     *            The RecordFilter to retrieve the associated QueryPart
     * @return the IRecordFilterQueryPart linked to the given filter or null if not found
     */
    public IRecordFilterQueryPart getRecordFilterQueryPart( IRecordFilter recordFilter )
    {
        IRecordFilterQueryPart recordFilterQueryPart = null;

        if ( recordFilter != null && _listRecordFilterQueryPartFactory != null && !_listRecordFilterQueryPartFactory.isEmpty( ) )
        {
            for ( IRecordFilterQueryPartFactory recordFilterQueryPartFactory : _listRecordFilterQueryPartFactory )
            {
                recordFilterQueryPart = recordFilterQueryPartFactory.buildRecordFilterQueryPart( recordFilter );
                if ( recordFilterQueryPart != null )
                {
                    break;
                }
            }
        }

        return recordFilterQueryPart;
    }
}
