<jsp:useBean id="managedirectoryfilterDirectoryFilter" scope="session" class="fr.paris.lutece.plugins.directory.modules.filter.web.DirectoryFilterJspBean" />
<% String strContent = managedirectoryfilterDirectoryFilter.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
