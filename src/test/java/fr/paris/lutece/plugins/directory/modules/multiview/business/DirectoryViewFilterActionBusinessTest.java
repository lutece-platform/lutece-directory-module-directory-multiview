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

package fr.paris.lutece.plugins.directory.modules.multiview.business;

import fr.paris.lutece.test.LuteceTestCase;

public class DirectoryViewFilterActionBusinessTest extends LuteceTestCase
{
    private final static int IDDIRECTORYFILTER1 = 1;
    private final static int IDDIRECTORYFILTER2 = 2;
    private final static int IDACTION1 = 1;
    private final static int IDACTION2 = 2;
    private final static int POSITION1 = 1;
    private final static int POSITION2 = 2;
    private final static String STYLE1 = "Style1";
    private final static String STYLE2 = "Style2";
    private final static int NBITEM1 = 1;
    private final static int NBITEM2 = 2;

    public void testBusiness( )
    {
        // Initialize an object
        DirectoryViewFilterAction directoryFilterAction = new DirectoryViewFilterAction( );
        directoryFilterAction.setIdDirectoryFilter( IDDIRECTORYFILTER1 );
        directoryFilterAction.setIdAction( IDACTION1 );
        directoryFilterAction.setPosition( POSITION1 );
        directoryFilterAction.setStyle( STYLE1 );
        directoryFilterAction.setNbItem( NBITEM1 );

        // Create test
        DirectoryViewFilterActionHome.create( directoryFilterAction );
        DirectoryViewFilterAction directoryFilterActionStored = DirectoryViewFilterActionHome.findByPrimaryKey( directoryFilterAction.getId( ) );
        assertEquals( directoryFilterActionStored.getIdDirectoryFilter( ), directoryFilterAction.getIdDirectoryFilter( ) );
        assertEquals( directoryFilterActionStored.getIdAction( ), directoryFilterAction.getIdAction( ) );
        assertEquals( directoryFilterActionStored.getPosition( ), directoryFilterAction.getPosition( ) );
        assertEquals( directoryFilterActionStored.getStyle( ), directoryFilterAction.getStyle( ) );
        assertEquals( directoryFilterActionStored.getNbItem( ), directoryFilterAction.getNbItem( ) );

        // Update test
        directoryFilterAction.setIdDirectoryFilter( IDDIRECTORYFILTER2 );
        directoryFilterAction.setIdAction( IDACTION2 );
        directoryFilterAction.setPosition( POSITION2 );
        directoryFilterAction.setStyle( STYLE2 );
        directoryFilterAction.setNbItem( NBITEM2 );
        DirectoryViewFilterActionHome.update( directoryFilterAction );
        directoryFilterActionStored = DirectoryViewFilterActionHome.findByPrimaryKey( directoryFilterAction.getId( ) );
        assertEquals( directoryFilterActionStored.getIdDirectoryFilter( ), directoryFilterAction.getIdDirectoryFilter( ) );
        assertEquals( directoryFilterActionStored.getIdAction( ), directoryFilterAction.getIdAction( ) );
        assertEquals( directoryFilterActionStored.getPosition( ), directoryFilterAction.getPosition( ) );
        assertEquals( directoryFilterActionStored.getStyle( ), directoryFilterAction.getStyle( ) );
        assertEquals( directoryFilterActionStored.getNbItem( ), directoryFilterAction.getNbItem( ) );

        // List test
        DirectoryViewFilterActionHome.getDirectoryFilterActionsList( );

        // Delete test
        DirectoryViewFilterActionHome.remove( directoryFilterAction.getId( ) );
        directoryFilterActionStored = DirectoryViewFilterActionHome.findByPrimaryKey( directoryFilterAction.getId( ) );
        assertNull( directoryFilterActionStored );

    }

}
