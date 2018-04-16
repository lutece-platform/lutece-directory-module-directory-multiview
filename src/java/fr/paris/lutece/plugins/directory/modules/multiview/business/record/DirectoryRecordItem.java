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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnCell;

/**
 * This object represent the couple of the identifier of a directory and the identifier of a record for a line of the Multiview record list
 */
public class DirectoryRecordItem
{
    // Variables
    private int _nIdDirectory;
    private int _nIdRecord;
    private final List<RecordColumnCell> _listRecordColumnCell;

    /**
     * Constructor
     */
    public DirectoryRecordItem( )
    {
        _listRecordColumnCell = new ArrayList<>( );
    }

    /**
     * Return the identifier of the directory of the DirectoryRecordItem
     * 
     * @return the nIdDirectory
     */
    public int getIdDirectory( )
    {
        return _nIdDirectory;
    }

    /**
     * Set the identifier of the Directory
     * 
     * @param nIdDirectory
     *            The identifier of the directory to set
     */
    public void setIdDirectory( int nIdDirectory )
    {
        _nIdDirectory = nIdDirectory;
    }

    /**
     * Return the identifier of the record of the DirectoryRecordItem
     * 
     * @return the nIdRecord
     */
    public int getIdRecord( )
    {
        return _nIdRecord;
    }

    /**
     * Set the identifier of the Record
     * 
     * @param nIdRecord
     *            The identifier of the Record to set
     */
    public void setIdRecord( int nIdRecord )
    {
        _nIdRecord = nIdRecord;
    }

    /**
     * Return the list of all RecordColumnCell for the DirectoryItem
     * 
     * @return the list of all RecordColumCell
     */
    public List<RecordColumnCell> getDirectoryRecordCellValues( )
    {
        return _listRecordColumnCell;
    }

    /**
     * Add a RecordColumnCell for the directoryRecordItem
     * 
     * @param recordColumnCell
     *            The RecordColumnCell to add to the DirectoryRecordItem
     */
    public void addRecordColumnCell( RecordColumnCell recordColumnCell )
    {
        _listRecordColumnCell.add( recordColumnCell );
    }
}