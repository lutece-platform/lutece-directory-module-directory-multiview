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
package fr.paris.lutece.plugins.directory.modules.multiview.web.recordlistpanel;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentFilter;

/**
 * Interface for a RecordListPanel
 */
public interface IRecordListPanel
{
    /**
     * Return the name of the panel
     * 
     * @return the name of the panel
     */
    String getName( );

    /**
     * Return the title of the panel
     * 
     * @return the title of the panel
     */
    String getTitle( );

    /**
     * Return the position of the panel
     * 
     * @return the position of the panel
     */
    int getPosition( );

    /**
     * Set the position of the panel
     * 
     * @param nPosition
     *            The position of the panel to set
     */
    void setPosition( int nPosition );

    /**
     * Return the boolean which tell if the panel is active or not
     * 
     * @return the boolean which tell if the panel is active or not
     */
    boolean isActive( );

    /**
     * Set the boolean which tell if the panel is active or not
     * 
     * @param bActive
     *            The boolean which tell if the panel is active or not
     */
    void setActive( boolean bActive );

    /**
     * Return the number of records of the panel
     * 
     * @return the number of records of the panel
     */
    int getRecordNumber( );

    /**
     * Set the number of records of the panel
     * 
     * @param nRecordNumber
     *            The number of records of the panel
     */
    void setRecordNumber( int nRecordNumber );

    /**
     * Return the Map which associate for each record is RecordAssignement
     * 
     * @return the map of all RecordAssignment
     */
    Map<String, RecordAssignment> getRecordAssignmentMap( );

    /**
     * Process the RecordAssignmentFilter for the RecordListPanel
     * 
     * @param request
     *            The request to retrieve the data from
     * @param recordAssignmentFilter
     *            The filter to process for the panel
     * @param collectionDirectory
     *            The collection of Directory used to configure the panel
     */
    void configureRecordListPanel( HttpServletRequest request, RecordAssignmentFilter recordAssignmentFilter, Collection<Directory> collectionDirectory );
}
