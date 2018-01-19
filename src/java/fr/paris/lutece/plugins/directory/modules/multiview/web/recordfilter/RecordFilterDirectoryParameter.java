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
package fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.IRecordFilterItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.recordfilter.RecordFilterDirectoryItem;

/**
 * Implementation of IRecordFilterParameter for the Directory filter
 */
public class RecordFilterDirectoryParameter implements IRecordFilterParameter
{
    // Constants
    private static final String PARAMETER_DIRECTORY_FILTER = "search_directory";
    private static final String MARK_DIRECTORY_FILTER = "id_directory";

    // Variables
    private final RecordFilterDirectoryItem _recordFilterDirectoryItem;

    /**
     * Constructor
     */
    public RecordFilterDirectoryParameter( )
    {
        _recordFilterDirectoryItem = new RecordFilterDirectoryItem( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueFromRequest( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_DIRECTORY_FILTER );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordFilterItem getRecordFilterItem( )
    {
        return _recordFilterDirectoryItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordFilterModelMark( )
    {
        return MARK_DIRECTORY_FILTER;
    }
}
