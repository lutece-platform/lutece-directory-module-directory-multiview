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
package fr.paris.lutece.plugins.directory.modules.multiview.service;

import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.IRecordPanelDisplay;

/**
 * Interface of the service for the module-directory-multiview
 */
public interface IDirectoryMultiviewService
{
    // Constants
    String BEAN_NAME = "directory-multiview.directoryMultiviewService";

    /**
     * Populate the RecordColumns from a list of RecordFilter
     * 
     * @param listRecordColumn
     *            The list of all RecordColumn to use to be populated
     * @param listRecordFilter
     *            The list of RecordFilter to use for retrieving the data of the columns to populate
     * @return the RecordListValues containing all the data of the RecordColumns
     */
    List<DirectoryRecordItem> populateRecordColumns( List<IRecordColumn> listRecordColumn, List<IRecordFilter> listRecordFilter );

    /**
     * Find the RecordPanel which is active in the given list
     * 
     * @param listRecordPanelDisplay
     *            The list to retrieve the active AbstractRecordPanelDisplay
     * @return the IRecordFilterPanelDisplay which is active or null if not found
     */
    IRecordPanelDisplay findActiveRecordPanel( List<IRecordPanelDisplay> listRecordPanelDisplay );
}
