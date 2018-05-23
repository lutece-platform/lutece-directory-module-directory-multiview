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
package fr.paris.lutece.plugins.directory.modules.multiview.business.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.directory.modules.multiview.service.DirectoryMultiviewService;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.IRecordPanelDisplay;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.impl.AbstractRecordPanelDisplay;
import fr.paris.lutece.plugins.directory.modules.multiview.web.record.panel.display.impl.RecordPanelRecordsDisplay;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Test for the DirectoryMultiviewService class
 */
public class DirectoryMultiviewServiceTest extends LuteceTestCase
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown( ) throws Exception
    {
        super.tearDown( );
    }
    
    /**
     * Test of the method {@link fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService#findActiveRecordPanel(java.util.List)}
     */
    public void testFindActiveRecordPanel( )
    {
        IRecordPanelDisplay recordPanelDisplayExpected = new RecordPanelRecordsDisplay( );
        recordPanelDisplayExpected.setActive( Boolean.TRUE );
        
        List<IRecordPanelDisplay> listRecordPanelDisplay = new ArrayList<>( );
        listRecordPanelDisplay.add( recordPanelDisplayExpected );
        
        DirectoryMultiviewService directoryMultiviewService = new DirectoryMultiviewService( );
        IRecordPanelDisplay recordPanelDisplayResult = directoryMultiviewService.findActiveRecordPanel( listRecordPanelDisplay );
        
        assertThat( recordPanelDisplayResult, is( recordPanelDisplayExpected ) );
    }
    
    /**
     * Test of the method {@link fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService#findActiveRecordPanel(java.util.List)}
     * with several panels
     */
    public void testFindActiveRecordPanelWithSeveralPanels( )
    {
        IRecordPanelDisplay recordPanelDisplayExpected = new RecordPanelRecordsDisplay( );
        recordPanelDisplayExpected.setActive( Boolean.TRUE );
        
        List<IRecordPanelDisplay> listRecordPanelDisplay = new ArrayList<>( );
        listRecordPanelDisplay.add( new RecordPanelDisplayMockOne( ) );
        listRecordPanelDisplay.add( recordPanelDisplayExpected );
        listRecordPanelDisplay.add( new RecordPanelDisplayMockTwo( ) );
        
        DirectoryMultiviewService directoryMultiviewService = new DirectoryMultiviewService( );
        IRecordPanelDisplay recordPanelDisplayResult = directoryMultiviewService.findActiveRecordPanel( listRecordPanelDisplay );
        
        assertThat( recordPanelDisplayResult, is( recordPanelDisplayExpected ) );
    }
    
    /**
     * Test of the method {@link fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService#findActiveRecordPanel(java.util.List)}
     * with null list of RecordPanelDisplay
     */
    public void testFindActiveRecordPanelWithNullList( )
    {
        IRecordPanelDisplay recordPanelDisplayExpected = null;
        
        List<IRecordPanelDisplay> listRecordPanelDisplay = null;
        
        DirectoryMultiviewService directoryMultiviewService = new DirectoryMultiviewService( );
        IRecordPanelDisplay recordPanelDisplayResult = directoryMultiviewService.findActiveRecordPanel( listRecordPanelDisplay );
        
        assertThat( recordPanelDisplayResult, is( recordPanelDisplayExpected ) );
    }
    
    /**
     * Test of the method {@link fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService#findActiveRecordPanel(java.util.List)}
     * with empty list of RecordPanelDisplay
     */
    public void testFindActiveRecordPanelWithEmptyList( )
    {
        IRecordPanelDisplay recordPanelDisplayExpected = null;
        
        List<IRecordPanelDisplay> listRecordPanelDisplay = new ArrayList<>( );
        
        DirectoryMultiviewService directoryMultiviewService = new DirectoryMultiviewService( );
        IRecordPanelDisplay recordPanelDisplayResult = directoryMultiviewService.findActiveRecordPanel( listRecordPanelDisplay );
        
        assertThat( recordPanelDisplayResult, is( recordPanelDisplayExpected ) );
    }
    
    /**
     * Test of the method {@link fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService#findActiveRecordPanel(java.util.List)}
     * with none active panels
     */
    public void testFindActiveRecordPanelWithNoneActivePanels( )
    {
        IRecordPanelDisplay recordPanelDisplayExpected = null;
        
        List<IRecordPanelDisplay> listRecordPanelDisplay = new ArrayList<>( );
        listRecordPanelDisplay.add( new RecordPanelDisplayMockOne( ) );
        listRecordPanelDisplay.add( new RecordPanelDisplayMockTwo( ) );
        
        DirectoryMultiviewService directoryMultiviewService = new DirectoryMultiviewService( );
        IRecordPanelDisplay recordPanelDisplayResult = directoryMultiviewService.findActiveRecordPanel( listRecordPanelDisplay );
        
        assertThat( recordPanelDisplayResult, is( recordPanelDisplayExpected ) );
    }
    
    /**
     * Test of the method {@link fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewService#findActiveRecordPanel(java.util.List)}
     * with several active panels
     */
    public void testFindActiveRecordPanelWithSeveralActivePanels( )
    {
        IRecordPanelDisplay recordPanelDisplayExpected = new RecordPanelDisplayMockTwo( );
        recordPanelDisplayExpected.setActive( Boolean.TRUE );
        
        List<IRecordPanelDisplay> listRecordPanelDisplay = new ArrayList<>( );
        listRecordPanelDisplay.add( new RecordPanelDisplayMockOne( ) );
        listRecordPanelDisplay.add( recordPanelDisplayExpected );
        
        IRecordPanelDisplay recordPanelRecordsDisplay = new RecordPanelRecordsDisplay( );
        recordPanelRecordsDisplay.setActive( Boolean.TRUE );
        listRecordPanelDisplay.add( recordPanelRecordsDisplay );
        
        DirectoryMultiviewService directoryMultiviewService = new DirectoryMultiviewService( );
        IRecordPanelDisplay recordPanelDisplayResult = directoryMultiviewService.findActiveRecordPanel( listRecordPanelDisplay );
        
        assertThat( recordPanelDisplayResult, is( recordPanelDisplayExpected ) );
    }
    
    /**
     * Mock implementation of the IRecordPanelDisplay
     */
    private class RecordPanelDisplayMockOne extends AbstractRecordPanelDisplay { }
    
    /**
     * Other mock implementation of the IRecordPanelDisplay
     */
    private class RecordPanelDisplayMockTwo extends AbstractRecordPanelDisplay { }
}
