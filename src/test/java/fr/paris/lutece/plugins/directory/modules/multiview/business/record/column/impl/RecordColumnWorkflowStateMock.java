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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;

/**
 * Mock implementation of the RecordColumn for the RecordColumnWorkflowState
 */
public class RecordColumnWorkflowStateMock implements IRecordColumn
{
    // Variables
    private int _nPosition = NumberUtils.INTEGER_MINUS_ONE;
    private String _strRecordColumnTitle;

    /**
     * Constructor
     * 
     * @param nRecordColumnPosition
     *            The position of the RecordColumn
     * @param strRecordColumnTitle
     *            The title of the RecrdColumn
     */
    public RecordColumnWorkflowStateMock( int nRecordColumnPosition, String strRecordColumnTitle )
    {
        _nPosition = nRecordColumnPosition;
        _strRecordColumnTitle = strRecordColumnTitle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRecordColumnPosition( )
    {
        return _nPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRecordColumnPosition( int nPosition )
    {
        _nPosition = nPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordColumnTitle( )
    {
        return _strRecordColumnTitle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRecordColumnTitle( String strRecordColumnTitle )
    {
        _strRecordColumnTitle = strRecordColumnTitle;
    }
}
