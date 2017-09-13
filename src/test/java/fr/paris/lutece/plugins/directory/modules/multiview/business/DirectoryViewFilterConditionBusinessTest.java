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

public class DirectoryViewFilterConditionBusinessTest extends LuteceTestCase
{
    private final static int IDDIRECTORYFILTER1 = 1;
    private final static int IDDIRECTORYFILTER2 = 2;
    private final static int IDENTRY1 = 1;
    private final static int IDENTRY2 = 2;
    private final static int OPERATOR1 = 1;
    private final static int OPERATOR2 = 2;
    private final static int FILTERTYPE1 = 1;
    private final static int FILTERTYPE2 = 2;

    public void testBusiness( )
    {
        // Initialize an object
        DirectoryViewFilterCondition directoryFilterCondition = new DirectoryViewFilterCondition( );
        directoryFilterCondition.setIdDirectoryFilter( IDDIRECTORYFILTER1 );
        directoryFilterCondition.setIdEntry( IDENTRY1 );
        directoryFilterCondition.setOperator( OPERATOR1 );
        directoryFilterCondition.setFilterType( FILTERTYPE1 );

        // Create test
        DirectoryViewFilterConditionHome.create( directoryFilterCondition );
        DirectoryViewFilterCondition directoryFilterConditionStored = DirectoryViewFilterConditionHome.findByPrimaryKey( directoryFilterCondition.getId( ) );
        assertEquals( directoryFilterConditionStored.getIdDirectoryFilter( ), directoryFilterCondition.getIdDirectoryFilter( ) );
        assertEquals( directoryFilterConditionStored.getIdEntry( ), directoryFilterCondition.getIdEntry( ) );
        assertEquals( directoryFilterConditionStored.getOperator( ), directoryFilterCondition.getOperator( ) );
        assertEquals( directoryFilterConditionStored.getFilterType( ), directoryFilterCondition.getFilterType( ) );

        // Update test
        directoryFilterCondition.setIdDirectoryFilter( IDDIRECTORYFILTER2 );
        directoryFilterCondition.setIdEntry( IDENTRY2 );
        directoryFilterCondition.setOperator( OPERATOR2 );
        directoryFilterCondition.setFilterType( FILTERTYPE2 );
        DirectoryViewFilterConditionHome.update( directoryFilterCondition );
        directoryFilterConditionStored = DirectoryViewFilterConditionHome.findByPrimaryKey( directoryFilterCondition.getId( ) );
        assertEquals( directoryFilterConditionStored.getIdDirectoryFilter( ), directoryFilterCondition.getIdDirectoryFilter( ) );
        assertEquals( directoryFilterConditionStored.getIdEntry( ), directoryFilterCondition.getIdEntry( ) );
        assertEquals( directoryFilterConditionStored.getOperator( ), directoryFilterCondition.getOperator( ) );
        assertEquals( directoryFilterConditionStored.getFilterType( ), directoryFilterCondition.getFilterType( ) );

        // List test
        DirectoryViewFilterConditionHome.getDirectoryFilterConditionsList( );

        // Delete test
        DirectoryViewFilterConditionHome.remove( directoryFilterCondition.getId( ) );
        directoryFilterConditionStored = DirectoryViewFilterConditionHome.findByPrimaryKey( directoryFilterCondition.getId( ) );
        assertNull( directoryFilterConditionStored );

    }

}
