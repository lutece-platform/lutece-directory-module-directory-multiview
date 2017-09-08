<jsp:useBean id="managedirectoryfilterconditionDirectoryFilterCondition" scope="session" class="fr.paris.lutece.plugins.directory.modules.filter.web.DirectoryFilterConditionJspBean" />
<% String strContent = managedirectoryfilterconditionDirectoryFilterCondition.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
