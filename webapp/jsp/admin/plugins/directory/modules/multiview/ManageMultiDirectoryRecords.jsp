<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>
<jsp:useBean id="multiDirectory" scope="session" class="fr.paris.lutece.plugins.directory.modules.multiview.web.MultiDirectoryJspBean" /><%
multiDirectory.init( request, multiDirectory.PROPERTY_RIGHT_MANAGE_MULTIVIEWDIRECTORY);
IPluginActionResult result = multiDirectory.getManageDirectoryRecord( request, response, true );

if ( result.getRedirect() != null ) {
	response.sendRedirect(result.getRedirect());
} else if ( result.getHtmlContent() != null ) { 
	
%>
<jsp:include page="../../../../AdminHeader.jsp" />
<%= result.getHtmlContent(  ) %>
<%@ include file="../../../../AdminFooter.jsp" %> 
<% } %><%@ page errorPage="../../../../ErrorPage.jsp" %>
