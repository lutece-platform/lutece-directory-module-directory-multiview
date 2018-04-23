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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart;

import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.factory.IRecordColumnQueryPartFactory;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Facade for a RecordColumn to build its request
 */
public class RecordColumnQueryPartFacade
{
    // Variables
    private final List<IRecordColumnQueryPartFactory> _listRecordColumnQueryPartFactory;

    /**
     * Constructor
     */
    public RecordColumnQueryPartFacade( )
    {
        _listRecordColumnQueryPartFactory = SpringContextService.getBeansOfType( IRecordColumnQueryPartFactory.class );
    }

    /**
     * Constructor
     * 
     * @param listRecordColumnQueryPartFactory
     *            The list of IRecordColumnQueryPartFactory to use for the facade
     */
    public RecordColumnQueryPartFacade( List<IRecordColumnQueryPartFactory> listRecordColumnQueryPartFactory )
    {
        _listRecordColumnQueryPartFactory = listRecordColumnQueryPartFactory;
    }

    /**
     * Return the appropriate QueryPart for the given RecordColumn
     * 
     * @param recordColumn
     *            The RecordColumn to retrieve the associated QueryPart
     * @return the IRecordColumnQueryPart linked to the given column or null if not found
     */
    public IRecordColumnQueryPart getRecordFilterQueryPart( IRecordColumn recordColumn )
    {
        IRecordColumnQueryPart recordColumnQueryPart = null;

        if ( recordColumn != null && _listRecordColumnQueryPartFactory != null && !_listRecordColumnQueryPartFactory.isEmpty( ) )
        {
            for ( IRecordColumnQueryPartFactory recordColumnQueryPartFactory : _listRecordColumnQueryPartFactory )
            {
                recordColumnQueryPart = recordColumnQueryPartFactory.buildRecordColumnQueryPart( recordColumn );
                if ( recordColumnQueryPart != null )
                {
                    break;
                }
            }
        }

        return recordColumnQueryPart;
    }
}
