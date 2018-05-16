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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.querypart;

import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.RecordParameters;

/**
 * Interface used for the record filter associated to a RecordPanelInitializer
 */
public interface IRecordPanelInitializerQueryPart
{
    /**
     * Build the query of the RecordPanelInitializer from the given RecordParameters. This method will use all the information of the RecordParameters to build
     * the query associated to the current PanelInitializer.
     * 
     * @param recordParameters
     *            The RecordParameters to use for building the query
     */
    void buildRecordPanelInitializerQuery( RecordParameters recordParameters );

    /**
     * Return the select query part of the RecordPanelInitializer
     * 
     * @return the select query part of the RecordPanelInitializer
     */
    String getRecordPanelInitializerSelectQuery( );

    /**
     * Return the from query part of the RecordPanelInitializer
     * 
     * @return the from query part of the RecordPanelInitializer
     */
    String getRecordPanelInitializerFromQuery( );

    /**
     * Return the list of join queries for a RecordPanelInitializer
     * 
     * @return the list of join queries for a RecordPanelInitializer
     */
    List<String> getRecordPanelInitializerJoinQueries( );
}
