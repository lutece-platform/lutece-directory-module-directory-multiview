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
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.IRecordColumn;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.RecordColumnFactory;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.RecordColumnDirectoryMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.RecordColumnRecordDateCreationMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.impl.RecordColumnWorkflowStateMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.list.RecordListDAOMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.list.RecordListFacade;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.IRecordPanel;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.configuration.RecordPanelConfiguration;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.impl.RecordPanelRecords;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.IRecordPanelInitializer;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.impl.RecordPanelDirectoryInitializerMock;
import fr.paris.lutece.plugins.directory.modules.multiview.business.record.panel.initializer.impl.RecordPanelRecordsInitializerMock;
import fr.paris.lutece.plugins.directory.modules.multiview.service.DirectoryMultiviewAuthorizationService;
import fr.paris.lutece.plugins.directory.modules.multiview.service.IDirectoryMultiviewAuthorizationService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Test class for the DirectoryMultiviewAuthorizationService implementation
 */
public class DirectoryMultiviewAuthorizationServiceTest extends LuteceTestCase
{
    // Variables
    private IRecordPanel _recordPanel;
    private RecordColumnFactory _recordColumnFactory;
    
    /**
     * {@inheritDoc}
     */
    @Before
    public void setUp( ) throws Exception
    {
        super.setUp( );
        
        IRecordPanelInitializer recordPanelDirectoryInitializer = new RecordPanelDirectoryInitializerMock( );
        IRecordPanelInitializer recordPanelRecordsInitializer = new RecordPanelRecordsInitializerMock( );
        
        List<IRecordPanelInitializer> listRecordPanelInitializer = new ArrayList<>( );
        listRecordPanelInitializer.add( recordPanelDirectoryInitializer );
        listRecordPanelInitializer.add( recordPanelRecordsInitializer );
        
        RecordPanelConfiguration recordPanelConfiguration = new RecordPanelConfiguration( "code_technique", 1, "titre", listRecordPanelInitializer );
        _recordPanel = new RecordPanelRecords( recordPanelConfiguration );
        
        List<IRecordColumn> listRecordColumn = new ArrayList<>( );
        listRecordColumn.add( new RecordColumnDirectoryMock( 1, "directory" ) );
        listRecordColumn.add( new RecordColumnRecordDateCreationMock( 2, "date creation" ) );
        listRecordColumn.add( new RecordColumnWorkflowStateMock( 3, "workflow state" ) );
        _recordColumnFactory = new RecordColumnFactory( listRecordColumn );
    }
    
    /**
     * {@inheritDoc}
     */
    @After
    public void tearDown( ) throws Exception
    {
        super.tearDown( );
    }
    
    /**
     * Test of the method {@link DirectoryMultiviewAuthorizationService#isUserAuthorizedOnRecord(int)} on a record on which the user is authorized
     */
    public void testIsUserAthorizedOnRecordOnAuthorizedRecord( )
    {
        int nIdRecord = 3;
        List<Integer> listAuthorizedId = Arrays.asList( 1, 2, 3, 4 );
        
        RecordListDAOMock recordListDAOMock = new RecordListDAOMock( listAuthorizedId );
        RecordListFacade recordListFacade = new RecordListFacade( recordListDAOMock );
        IDirectoryMultiviewAuthorizationService directoryMultiviewAuthorizationService = new DirectoryMultiviewAuthorizationService( _recordPanel, recordListFacade, _recordColumnFactory );
        
        boolean bIsUserAuthorize = directoryMultiviewAuthorizationService.isUserAuthorizedOnRecord( nIdRecord );
        assertThat( bIsUserAuthorize, is( Boolean.TRUE ) );
    }
    
    /**
     * Test of the method {@link DirectoryMultiviewAuthorizationService#isUserAuthorizedOnRecord(int)} on a record on which the user is not authorized
     */
    public void testIsUserAthorizedOnRecordOnUnauthorizedRecord( )
    {
        int nIdRecord = 6;
        List<Integer> listAuthorizedId = Arrays.asList( 1, 2, 3, 4 );
        
        RecordListDAOMock recordListDAOMock = new RecordListDAOMock( listAuthorizedId );
        RecordListFacade recordListFacade = new RecordListFacade( recordListDAOMock );
        IDirectoryMultiviewAuthorizationService directoryMultiviewAuthorizationService = new DirectoryMultiviewAuthorizationService( _recordPanel, recordListFacade, _recordColumnFactory );
        
        boolean bIsUserAuthorize = directoryMultiviewAuthorizationService.isUserAuthorizedOnRecord( nIdRecord );
        assertThat( bIsUserAuthorize, is( Boolean.FALSE ) );
    }
    
    /**
     * Test of the method {@link DirectoryMultiviewAuthorizationService#isUserAuthorizedOnRecord(int)} with a bad id for a record
     */
    public void testIsUserAthorizedWithBadIdRecord( )
    {
        int nIdRecord = -1;
        List<Integer> listAuthorizedId = Arrays.asList( 1, 2, 3, 4 );
        
        RecordListDAOMock recordListDAOMock = new RecordListDAOMock( listAuthorizedId );
        RecordListFacade recordListFacade = new RecordListFacade( recordListDAOMock );
        IDirectoryMultiviewAuthorizationService directoryMultiviewAuthorizationService = new DirectoryMultiviewAuthorizationService( _recordPanel, recordListFacade, _recordColumnFactory );
        
        boolean bIsUserAuthorize = directoryMultiviewAuthorizationService.isUserAuthorizedOnRecord( nIdRecord );
        assertThat( bIsUserAuthorize, is( Boolean.FALSE ) );
    }
    
    /**
     * Test of the method {@link DirectoryMultiviewAuthorizationService#isUserAuthorizedOnRecord(int)} with an empty panel
     */
    public void testIsUserAthorizedWithEmptyPanel( )
    {
        int nIdRecord = 2;
        List<Integer> listAuthorizedId = Arrays.asList( 1, 2, 3, 4 );
        
        RecordListDAOMock recordListDAOMock = new RecordListDAOMock( listAuthorizedId );
        RecordListFacade recordListFacade = new RecordListFacade( recordListDAOMock );
        IDirectoryMultiviewAuthorizationService directoryMultiviewAuthorizationService = new DirectoryMultiviewAuthorizationService( null, recordListFacade, _recordColumnFactory );
        
        boolean bIsUserAuthorize = directoryMultiviewAuthorizationService.isUserAuthorizedOnRecord( nIdRecord );
        assertThat( bIsUserAuthorize, is( Boolean.FALSE ) );
    }
}
