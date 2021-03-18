<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="col-lg-12 grid-margin">
	<div class="card">
		<div class="card-body">
			<h4 class="card-title">AppUser Table</h4>
			<%-- <p class="card-description">
				Add class
				<code>.table-striped</code>
				
			</p> --%>
			<c:if test="${not empty success}">
				<div class="alert alert-success" role="alert">
				  	${success}
				</div>
			</c:if>
			<c:if test="${not empty fail}">
				<div class="alert alert-danger" role="alert">
				  	${fail}
				</div>
			</c:if>
			<div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>User Type </th>
							<th>Name </th>
							<th>Contact </th>
							<th>Ledger Number </th>
							<th>Account Number </th>
							<th>Action</th>

						</tr>
					</thead>
					<c:url var='updatelink' value="/app-user/edit" />

						<c:url var="deletelink" value="/app-user/delete" />
					<c:forEach items="${list}" var="templist">
						

						<tbody>
							<tr>

								 <td>${templist.userType.name}</td>
								<td>${templist.name}</td>
								<td>${templist.contact}</td>
								<td>${templist.ledgerNumber}</td>
								<td>${templist.accountNumber}</td>
								<td><a href="${updatelink}/${templist.id}" class="btn btn-success btn-fw">Edit</a><a  
									href="${deletelink}/${templist.id}" class="btn btn-danger btn-fw">Delete</a></td>
							</tr>
						</tbody>
					</c:forEach>
				</table>
				 <div class="float-right">
	            <nav aria-label="Page navigation example">
	            <c:if test="${totalPages>0}">
				  <ul class="pagination">
					  <c:if test="${currentPage !=1}">  
					    <li class="page-item"><a class="page-link" href="/app-user/pageno=${currentPage - 1}">Previous</a></li>
					  </c:if>
					  <c:forEach var="i"  begin="1" end="${totalPages}">
						  	<c:choose>
								<c:when test="${i==currentPage}">
										<li class="page-item active"><a class="page-link">${i}</a></li>
								</c:when>
								<c:otherwise>
										<li class="page-item"><a class="page-link" 
											href="<c:url value="/app-user/pageno=${i}"/>"        
											>${i}</a></li>
								</c:otherwise>
							</c:choose>				   
					  </c:forEach>
					  <c:if test="${currentPage!= totalPages}">
					    	<li class="page-item"><a class="page-link" href="/app-user/pageno=${currentPage + 1}">Next</a></li>
					  </c:if>
				  </ul>
				  </c:if>
				</nav>          
	 		</div>
			</div>
		</div>
		<!-- <ul
			class="pagination rounded-flat pagination-success d-flex justify-content-center">
			<li class="page-item"><a class="page-link" href="#"><i
					class="mdi mdi-chevron-left"></i></a></li>
			<li class="page-item active"><a class="page-link" href="#">1</a></li>
			<li class="page-item"><a class="page-link" href="#">2</a></li>
			<li class="page-item"><a class="page-link" href="#">3</a></li>
			<li class="page-item"><a class="page-link" href="#">4</a></li>
			<li class="page-item"><a class="page-link" href="#"><i
					class="mdi mdi-chevron-right"></i></a></li>
		</ul>
	</div> -->

</div>