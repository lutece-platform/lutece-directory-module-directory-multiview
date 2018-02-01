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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.modules.multiview.business.customizedcolumn.CustomizedColumnFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.web.recordfilter.IRecordFilterParameter;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;

/**
 * Interface of the service for the module-directory-multiview
 */
public interface IDirectoryMultiviewService
{
    // Constants
    String BEAN_NAME = "directory-multiview.directoryMultiviewService";

    /**
     * Fill the filter value from the request
     * 
     * @param request
     *            The HttpServletRequest to retrieve the value from
     * @param listRecordFilterParameter
     *            The list of all IRecordFilterParameter use for filter the records
     * @param customizedColumnFactory
     *            The factory to use for creating the new RecordFieldItem list for the new filter
     * @return the filter The filter to set the value on
     */
    RecordAssignmentFilter getRecordAssignmentFilter( HttpServletRequest request, List<IRecordFilterParameter> listRecordFilterParameter,
            CustomizedColumnFactory customizedColumnFactory );

    /**
     * Populate the list of ResourcesActions with the value containing in the the list of the customizedColumn inside the Factory
     * 
     * @param resourceActions
     *            The list of ResourceActions to populate
     * @param customizedColumnFactory
     *            The Factory which contains the list of CustomizedColumn to retrieve the data from
     */
    void populateResourceActionList( List<Map<String, Object>> resourceActions, CustomizedColumnFactory customizedColumnFactory );

    /**
     * Populate Record Precisions
     * 
     * @param resourceActions
     *            The list of all Resources Actions
     * @param listEntry
     *            The list of IEntry to retrieve data from
     * @param strMarkName
     *            The name of the mark to store the value inside
     */
    void populateRecord( List<Map<String, Object>> resourceActions, List<IEntry> listEntry, String strMarkName );

    /**
     * Populate the Map which associate for each id record its last RecordAssignment
     * 
     * @param listRecordAssignment
     *            The list of RecordAssignment to populate the map from
     * @return the Map which associate for each id record its last RecordAssignment
     */
    Map<String, RecordAssignment> populateRecordAssignmentMap( List<RecordAssignment> listRecordAssignment );
}
