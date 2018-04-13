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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter.IRecordFilter;

/**
 * Interface for the Panel element
 */
public interface IRecordPanel
{
    /**
     * Return the RecordFilter of the RecordPanel
     * 
     * @return the recordFilter of the RecordPanel
     */
    IRecordFilter getRecordFilter( );

    /**
     * Return the title key of the panel
     * 
     * @return the title key of the panel
     */
    String getTitleKey( );

    /**
     * Set the title key of the panel
     * 
     * @param strTitleKey
     *            The title key to set to the panel
     */
    void setTitleKey( String strTitleKey );

    /**
     * Return the technical code of the panel
     * 
     * @return the technical code of the panel
     */
    String getTechnicalCode( );

    /**
     * Set the technical code of the panel
     * 
     * @param strTechnicalCode
     *            The technical code to set to the panel
     */
    void setTechnicalCode( String strTechnicalCode );
}
