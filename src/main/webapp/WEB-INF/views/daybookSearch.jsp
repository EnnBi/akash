<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="col-md-12 grid-margin stretch-card">
	<div class="card">
		<div class="card-body">
			<h4 class="card-title">Day Book</h4>
			<form:form action="/day-book/search" method="post"
				modelAttribute="dayBookSearch" id="form">

				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Person Type</label>
							<div class="col-sm-8">
								<form:select class="form-control" path="" id="type">
									<form:option value="">Select Person Type</form:option>
									<form:options items="${userTypes}" itemLabel="name"
										itemValue="name" />
								</form:select>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Name</label>
							<div class="col-sm-8">
								<form:select class="form-control" path="user" id="users">
									<form:option value="">Select Person</form:option>
									<form:options items="${customers}" itemLabel="name"
										itemValue="id" />
								</form:select>
							</div>
						</div>
					</div>

				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Transaction By</label>
							<div class="col-sm-3">
								<div class="form-radio">
									<label class="form-check-label"> <form:radiobutton
											class="form-check-input transactionBy" path="transactionBy"
											id="membershipRadios1" value="Cash" /> Cash <i
										class="input-helper"></i></label>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-radio">
									<label class="form-check-label"> <form:radiobutton
											class="form-check-input transactionBy" path="transactionBy"
											id="membershipRadios2" value="Cheque" /> Cheque <i
										class="input-helper"></i></label>
								</div>
							</div>
							<div class="col-sm-2">
								<div class="form-radio">
									<label class="form-check-label"> <form:radiobutton
											class="form-check-input transactionBy" path="transactionBy"
											id="membershipRadios2" value="TT" /> TT <i
										class="input-helper"></i></label>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Transaction No.</label>
							<div class="col-sm-8">
								<form:input type="text" class="form-control"
									path="transactionNumber" id="transactionNumber" />
							</div>
						</div>
					</div>

				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Start Date</label>
							<div class="col-sm-8">
								<form:input type="date" class="form-control" required="required"
									path="startDate" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">End Date</label>
							<div class="col-sm-8">
								<form:input type="date" class="form-control" required="required"
									path="endDate" />
							</div>
						</div>
					</div>

				</div>

				<div class="row">

					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label" id="accountNumberLbl">Account
								Number</label>
							<div class="col-sm-8">
								<form:select class="form-control" path="accountNumber"
									id="accountNumber">
									<form:option value="">Select Account Numbers</form:option>
									<form:options items="${accounts}" itemLabel="accountNumber"
										itemValue="accountNumber" />
								</form:select>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Transaction Type</label>
							<div class="col-sm-4">
								<div class="form-radio">
									<label class="form-check-label"> <form:radiobutton
											class="form-check-input" path="transactionType"
											id="membershipRadios1" value="Revenue" /> Revenue <i
										class="input-helper"></i></label>
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-radio">
									<label class="form-check-label"> <form:radiobutton
											class="form-check-input" path="transactionType"
											id="membershipRadios2" value="Expenditure" /> Expenditure <i
										class="input-helper"></i></label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Status</label>
							<div class="col-sm-4">
								<div class="form-radio">
									<label class="form-check-label"> <form:radiobutton
											class="form-check-input" path="status" id="membershipRadios1"
											value="Pending" /> Pending <i class="input-helper"></i></label>
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-radio">
									<label class="form-check-label"> <form:radiobutton
											class="form-check-input" path="status" id="membershipRadios2"
											value="Success" /> Success <i class="input-helper"></i></label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<div class="form-group row float-right"
							style="margin-bottom: 0rem;">
							<input type="submit" class="btn btn-success btn-fw"
								value="Submit">
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>
<div class="col-lg-12 grid-margin">
	<div class="card">
		<div class="card-body">
			<h4 class="card-title">Manufacture Table</h4>
			<div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Date</th>
							<th>Transaction Type</th>
							<th>Name</th>
							<th>Amount</th>
							<th>Responsible Person</th>
							<th>Account No.</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${dayBooks}" var="dayBook">
							<tr>
								<td>${dayBook.date}</td>
								<td>${dayBook.transactionType}</td>
								<td>${dayBook.user.name}</td>
								<td>${dayBook.amount}</td>
								<td>${dayBook.responsiblePerson}</td>
								<td>${dayBook.accountNumber}</td>
								<td><a href="/day-book/edit/${dayBook.id}"
									class="btn btn-success btn-fw" style="margin-right: 5px">Edit</a><a
									href="/day-book/delete/${dayBook.id}"
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
						href="/day-book/pageno=${currentPage - 1}"><i
							class="mdi mdi-chevron-left"></i></a></li>
				</c:if>
				<c:forEach var="i" begin="1" end="${totalPages}">
					<c:choose>
						<c:when test="${i==currentPage}">
							<li class="page-item active"><a class="page-link">${i}</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a class="page-link"
								href="<c:url value="/day-book/pageno=${i}"/>">${i}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${currentPage!= totalPages}">
					<li class="page-item"><a class="page-link"
						href="/day-book/pageno=${currentPage + 1}"><i
							class="mdi mdi-chevron-right"></i></a></li>
				</c:if>
			</ul>
		</c:if>
	</div>

</div>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.slim.min.js"
	integrity="sha512-6ORWJX/LrnSjBzwefdNUyLCMTIsGoNP6NftMy2UAm1JBm6PRZCO1d7OHBStWpVFZLO+RerTvqX/Z9mBFfCJZ4A=="
	crossorigin="anonymous"></script>
<script type="text/javascript">
	$('#type').change(
			function() {
				var name = $(this).val();
				var url = "/user-type/" + name + "/users";
				$.get(url, function(data) {
					$('#users').find('option').not(':first').remove();
					$.each(data, function(key, value) {
						$('#users').append(
								$("<option></option>").attr("value", value.id)
										.text(value.name));
					});
				});
			})
</script>