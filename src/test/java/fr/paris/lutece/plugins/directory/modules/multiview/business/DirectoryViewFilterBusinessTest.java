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

public class DirectoryViewFilterBusinessTest extends LuteceTestCase
{
    private final static String NAME1 = "Name1";
    private final static String NAME2 = "Name2";
    private final static String STYLE1 = "Style1";
    private final static String STYLE2 = "Style2";
    private final static int POSITION1 = 1;
    private final static int POSITION2 = 2;

    public void testBusiness( )
    {
        // Initialize an object
        DirectoryViewFilter directoryFilter = new DirectoryViewFilter( );
        directoryFilter.setName( NAME1 );
        directoryFilter.setStyle( STYLE1 );
        directoryFilter.setPosition( POSITION1 );

        // Create test
        DirectoryViewFilterHome.create( directoryFilter );
        DirectoryViewFilter directoryFilterStored = DirectoryViewFilterHome.findByPrimaryKey( directoryFilter.getId( ) );
        assertEquals( directoryFilterStored.getName( ), directoryFilter.getName( ) );
        assertEquals( directoryFilterStored.getStyle( ), directoryFilter.getStyle( ) );
        assertEquals( directoryFilterStored.getPosition( ), directoryFilter.getPosition( ) );

        // Update test
        directoryFilter.setName( NAME2 );
        directoryFilter.setStyle( STYLE2 );
        directoryFilter.setPosition( POSITION2 );
        DirectoryViewFilterHome.update( directoryFilter );
        directoryFilterStored = DirectoryViewFilterHome.findByPrimaryKey( directoryFilter.getId( ) );
        assertEquals( directoryFilterStored.getName( ), directoryFilter.getName( ) );
        assertEquals( directoryFilterStored.getStyle( ), directoryFilter.getStyle( ) );
        assertEquals( directoryFilterStored.getPosition( ), directoryFilter.getPosition( ) );

        // List test
        DirectoryViewFilterHome.getDirectoryFiltersList( );

        // Delete test
        DirectoryViewFilterHome.remove( directoryFilter.getId( ) );
        directoryFilterStored = DirectoryViewFilterHome.findByPrimaryKey( directoryFilter.getId( ) );
        assertNull( directoryFilterStored );

    }

}
