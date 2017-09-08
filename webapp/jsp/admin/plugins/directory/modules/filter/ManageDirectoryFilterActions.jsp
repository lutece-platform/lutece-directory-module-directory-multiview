<jsp:useBean id="managedirectoryfilteractionDirectoryFilterAction" scope="session" class="fr.paris.lutece.plugins.directory.modules.filter.web.DirectoryFilterActionJspBean" />
<% String strContent = managedirectoryfilteractionDirectoryFilterAction.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
