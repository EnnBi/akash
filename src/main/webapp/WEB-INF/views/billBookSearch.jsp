<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
<style>
	.select2-container--default .select2-selection--multiple .select2-selection__choice{
	font-size: 1rem !important;
	}
	.select2-container--default .select2-selection--single .select2-selection__rendered{
	line-height: 10px
	}
	</style>
<div class="col-12 grid-margin">
	<div class="card">
		<div class="card-body">
			<h4 class="card-title">Bill Book Search</h4>
			<c:if test="${not empty success}">
				<div class="alert alert-success" role="alert">${success}</div>
			</c:if>
			<c:if test="${not empty fail}">
				<div class="alert alert-danger" role="alert">${fail}</div>
			</c:if>
			<form:form action="${pageContext.request.contextPath}/bill-book/search" class="form-sample"
				modelAttribute="billBookSearch" method="post">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Receipt No.</label>
							<div class="col-sm-8">
								<form:input type="text" class="form-control"
									path="receiptNumber"  id="receipt" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Vehicle</label>
							<div class="col-sm-8">
								<form:select class="form-control" path="vehicleId">
									<form:option value="">Select any Vehicle</form:option>
									<form:options items="${vehicles}" itemLabel="number"
										itemValue="id" />
								</form:select>
							</div>
						</div>
					</div>


				</div>

				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Name</label>
							<div class="col-sm-8">
								<form:select class="form-control" path="customerId"
									id="customer" >
									<form:option value="">Select Customer</form:option>
									<c:forEach var="customer" items="${customers}">
										<form:option value="${customer.id}"
											contact="${customer.contact}" address="${customer.address}">
											<c:out value="${customer.name} ${customer.address}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Sites</label>
							<div class="col-sm-8">
								<form:select class="form-control" path="siteId" id="site">
									<form:option value="">Select any Site</form:option>
									<form:options items="${sites}" itemLabel="name" itemValue="id" />
								</form:select>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Start Date</label>
							<div class="col-sm-8">
								<form:input type="text" class="form-control date" path="startDate"
									required="required" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">End Date</label>
							<div class="col-sm-8">
								<form:input type="text" class="form-control date" path="endDate"
									required="required" />
							</div>
						</div>
					</div>
				</div>
				<div class="form-group row float-right">
							<input type="submit" class="btn btn-success btn-fw"
								value="Submit">
						</div>
			</form:form>
		</div>
	</div>
</div>
<div class="col-lg-12 grid-margin">
	<div class="card">
		<div class="card-body">
			<h4 class="card-title">Bill Book Table</h4>${fn.length(billBooks)}
			<div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Receipt No.</th>
							<th>Date</th>
							<th>Customer Name</th>
							<th>Site</th>
							<!-- <th>Vehicle</th> -->
							<th>Total</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
					
					<c:forEach items="${billBooks}"  var="billBook"> 
						<tr>
							<td>${billBook.receiptNumber}</td>
							<td>${billBook.date}</td>
							<td>${billBook.customerName}</td>
							<td>${billBook.site}</td>
							<!-- <td>${billBook.vehicle}</td> -->
							<td>${billBook.total}</td>
							<td><a href="${pageContext.request.contextPath}/bill-book/edit/${billBook.id}"
									class="btn btn-success btn-fw" style="margin-right: 5px">Edit</a><a
									href="${pageContext.request.contextPath}/bill-book/delete/${billBook.id}"
									class="btn btn-danger btn-fw">Delete</a></td>
						</tr>
					</c:forEach>	
					</tbody>
				</table>
			</div>
		</div>
		<c:if test="${totalPages>0}">
			<ul
				class="pagination rounded-flat pagination-success d-flex justify-content-center">
				<c:if test="${currentPage !=1}">
					<li class="page-item"><a class="page-link"
						href="${pageContext.request.contextPath}/bill-book/pageno=${currentPage - 1}"><i
							class="mdi mdi-chevron-left"></i></a></li>
				</c:if>
				<c:forEach var="i" begin="1" end="${totalPages}">
					<c:choose>
						<c:when test="${i==currentPage}">
							<li class="page-item active"><a class="page-link">${i}</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a class="page-link"
								href="<c:url value="/bill-book/pageno=${i}"/>">${i}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${currentPage!= totalPages}">
					<li class="page-item"><a class="page-link"
						href="${pageContext.request.contextPath}/bill-book/pageno=${currentPage + 1}"><i
							class="mdi mdi-chevron-right"></i></a></li>
				</c:if>
			</ul>
		</c:if>
	</div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery.slim.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/select2.min.js" defer></script>
<script>
$(document).ready(function(){
	$('#customer').select2();
});
</script>