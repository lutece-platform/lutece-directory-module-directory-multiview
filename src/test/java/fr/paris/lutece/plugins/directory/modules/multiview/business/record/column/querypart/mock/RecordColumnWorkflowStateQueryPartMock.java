/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.mock;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.modules.multiview.business.record.column.querypart.impl.RecordColumnWorkflowStateQueryPart;

/**
 * Mock for RecordColumnWorkflowStateQueryPart class
 */
public class RecordColumnWorkflowStateQueryPartMock extends RecordColumnWorkflowStateQueryPart
{
    // Constants
    private static final String WORKFLOW_STATE_SELECT_QUERY_PART = "workflow_state_name";
    private static final String WORKFLOW_STATE_FORM_QUERY_PART = StringUtils.EMPTY;
    private static final String WORKFLOW_STATE_JOIN_WORKFLOW_RESOURCE_QUERY_PART = "LEFT JOIN workflow_resource_workflow AS wf_resource_workflow ON wf_resource_workflow.id_resource = record.id_record";
    private static final String WORKFLOW_STATE_JOIN_WORKFLOW_STATE_QUERY_PART = "LEFT JOIN workflow_state AS ws_workflow_state ON ws_workflow_state.id_state = wf_resource_workflow.id_state";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordColumnSelectQuery( )
    {
        return WORKFLOW_STATE_SELECT_QUERY_PART;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordColumnFromQuery( )
    {
        return WORKFLOW_STATE_FORM_QUERY_PART;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRecordColumnJoinQueries( )
    {
        List<String> listRecordColumnJoinQueries = new ArrayList<>( );
        listRecordColumnJoinQueries.add( WORKFLOW_STATE_JOIN_WORKFLOW_RESOURCE_QUERY_PART );
        listRecordColumnJoinQueries.add( WORKFLOW_STATE_JOIN_WORKFLOW_STATE_QUERY_PART );

        return listRecordColumnJoinQueries;
    }
}
