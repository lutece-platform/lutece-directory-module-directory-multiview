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
package fr.paris.lutece.plugins.directory.modules.multiview.business.record.filter;

/**
 * Constants class for RecordFilter objects
 */
public final class RecordFilterQueryConstants
{
    // Constants
    public static final String SELECT_KEYWORD = "SELECT ";
    public static final String FROM_KEYWORD = " FROM ";
    public static final String WHERE_KEYWORD = " WHERE ";
    public static final String WHERE_BASE_KEYWORD = " WHERE 1=1 ";
    public static final String AND_KEYWORD = " AND ";
    public static final String AND_OPEN_CLAUSE = " ( ";
    public static final String AND_CLOSE_CLAUSE = " ) ";
    public static final String SPACE_SEPARATOR = " ";
    public static final String COMMA_SEPARATOR = ", ";
    public static final String RECORD_QUERY_BASE_PART = " id_record IN ( ";
    public static final String RECORD_QUERY_SELECT_DISTINCT_PART = " SELECT DISTINCT id_record ";
    public static final String RECORD_QUERY_FROM_PART = " FROM directory_record_field ";

    /**
     * Constructor
     */
    private RecordFilterQueryConstants( )
    {

    }
}
