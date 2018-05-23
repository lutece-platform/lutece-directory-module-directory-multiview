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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a line of the Multiview table
 */
public class RecordColumnLineTemplate
{
    // Variables
    private final int _nIdRecord;
    private List<String> _listRecordColumnCellTemplate;

    /**
     * Constructor
     * 
     * @param nIdRecord
     *            The identifier of the Record associate to the RecordColumnLineTemplate
     */
    public RecordColumnLineTemplate( int nIdRecord )
    {
        _nIdRecord = nIdRecord;
        _listRecordColumnCellTemplate = new ArrayList<>( );
    }

    /**
     * Return the identifier of the record of the RecordColumnLineTemplate
     * 
     * @return the identifier of the record of the RecordColumnLineTemplate
     */
    public int getIdRecord( )
    {
        return _nIdRecord;
    }

    /**
     * Return the list of the RecordColumnCell template
     * 
     * @return the list of the RecordColumnCell template
     */
    public List<String> getRecordColumnCellTemplateList( )
    {
        return _listRecordColumnCellTemplate;
    }

    /**
     * Set the list of the RecordColumnCell template
     * 
     * @param listRecordColumnCellTemplate
     *            The list of the RecordColumnCell template
     */
    public void setRecordColumnCellTemplate( List<String> listRecordColumnCellTemplate )
    {
        _listRecordColumnCellTemplate = listRecordColumnCellTemplate;
    }

    /**
     * Add a record column cell template
     * 
     * @param strRecordColumnCell
     *            The record column cell template to add to the RecordColumn
     */
    public void addRecordColumnCellTemplate( String strRecordColumnCell )
    {
        _listRecordColumnCellTemplate.add( strRecordColumnCell );
    }
}
