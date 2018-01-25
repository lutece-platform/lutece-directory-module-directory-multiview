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
package fr.paris.lutece.plugins.directory.modules.multiview.web.columnfilter;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;
import fr.paris.lutece.util.ReferenceList;

/**
 * Interface for the ColumnFilter Object
 */
public interface IColumnFilter
{
    // Template
    String FILTER_TEMPLATE_NAME = "admin/plugins/directory/modules/multiview/record_filter.html";
    
    // Marks
    String MARK_FILTER_LIST = "filter_list";
    String MARK_FILTER_NAME = "filter_name";
    String MARK_FILTER_LIST_VALUE = "filter_list_value";
    
    /**
     * Populate the list of object to filter on
     */
    void populateListValue( );
    
    /**
     * Create the ReferenceList of the filter object list
     * 
     * @return the ReferenceList object created from the list of object of the filter
     */
    ReferenceList createReferenceList( );
    
    /**
     * Build the HtmlTemplate from the filter value with the given request
     * 
     * @param filter
     *          The RecordAssignmentFilter to retrieve the value from
     * @param request
     *          The HttpServletRequest
     */
    void buildTemplate( RecordAssignmentFilter filter, HttpServletRequest request );
    
    /**
     * Return the template of the Filter
     * 
     * @return the template of the filter
     */
    String getTemplate( );
}
