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
package fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.DirectoryRecordItem;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.filter.IFilterable;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.util.IRecordListPosition;

/**
 * Interface for Filter of a RecordPanelDisplay
 */
public interface IRecordPanelDisplay extends IFilterable, IRecordListPosition
{
    /**
     * Return the boolean which tell if the PanelDisplay is for the active Panel or not
     * 
     * @return the boolean which tell if the PanelDisplay is for the active Panel or not
     */
    boolean isActive( );

    /**
     * Set the boolean which tell if the PanelDisplay is for the active Panel or not
     * 
     * @param bActive
     *            The boolean which tell if the PanelDisplay is for the active Panel or not
     */
    void setActive( boolean bActive );

    /**
     * Return the technical code of the RecordPanel
     * 
     * @return the technical code of the RecordPanel
     */
    String getTechnicalCode( );

    /**
     * Return the record number of the RecordPanelDisplay
     * 
     * @return the record number of the RecordPanelDisplay
     */
    int getRecordNumber( );

    /**
     * Return the template of the RecordPanelDisplay
     * 
     * @return the template of the RecordPanelDisplay
     */
    String getTemplate( );

    /**
     * Set the RecordPanel of the RecordPanelDisplay
     * 
     * @param recordPanel
     *            The RecordPanel of the RecordPanelDisplay
     */
    void setRecordPanel( IRecordPanel recordPanel );

    /**
     * Return the list of DirectoryRecordItem of the RecordPanelDisplay
     * 
     * @return the list of DirectoryRecordItem of the RecordPanelDisplay
     */
    List<DirectoryRecordItem> getDirectoryRecordItemList( );

    /**
     * Set the list of DirectoryRecordItem of the RecordPanelDisplay
     * 
     * @param listDirectoryRecordItem
     *            The list of DirectoryRecordItem to set to the RecordPanelDisplay
     */
    void setDirectoryRecordItemList( List<DirectoryRecordItem> listDirectoryRecordItem );

    /**
     * Configure the RecordPanelDisplay by setting its name, its title and if is active or not
     * 
     * @param request
     *            The request to retrieve the information from
     */
    void configureRecordPanelDisplay( HttpServletRequest request );

    /**
     * Build the template of the RecordPanelDisplay
     * 
     * @param locale
     *            The locale used to build the template
     * @return the built template of the RecordPanelDisplay
     */
    String buildTemplate( Locale locale );
}
