<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="col-12 grid-margin">
	<div class="card">
		<div class="card-body">
			<!-- <h4 class="card-title">Horizontal Two column</h4> -->
			<c:if test="${not empty success}">
				<div class="alert alert-success" role="alert">${success}</div>
			</c:if>
			<c:if test="${not empty fail}">
				<div class="alert alert-danger" role="alert">${fail}</div>
			</c:if>

			<c:choose>
				<c:when test="${edit}">
					<c:set value="/raw-material/update" var="action" />
					<c:set var="caption" value="Update" />
				</c:when>
				<c:otherwise>
					<c:set value="/raw-material/save" var="action" />
					<c:set var="caption" value="Save" />

				</c:otherwise>
			</c:choose>

			<form:form action="${action}" modelAttribute="rawMaterial" method="post">
				<form:hidden path="id" />
				<input type="hidden" name="p" value="${currentPage}" />
				<p class="card-description">Raw Material Info</p>
				
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<span class="input-group-addon label-left" id="basic-addon2">Select Dealer</span>
							<form:select path="dealer" class="form-control">
								 <form:option value=""></form:option>
								<form:options items="${userList}" itemLabel="name"
									itemValue="id"  />
							</form:select>
							<form:errors class="err" path="dealer" />
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<span class="input-group-addon label-left" id="basic-addon2">Select Any MaterialType</span>
							<form:select path="material" class=" form-control">
								 <form:option value=""></form:option> 
								<form:options items="${rawList}" itemLabel="name"
									itemValue="id" />
							</form:select>
							<form:errors class="err" path="material" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-3 col-form-label">Chalan Number</label>
							<div class="col-sm-9">
								<form:input type="text" class="form-control" path="chalanNumber" />
								<form:errors path="chalanNumber" />
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-3 col-form-label">Quantity</label>
							<div class="col-sm-9">
								<form:input type="text" class="form-control" path="quantity" />
								<form:errors path="quantity" />
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-3 col-form-label">Amount</label>
							<div class="col-sm-9">
								<form:input type="text" class="form-control" path="amount" />
								<form:errors path="amount" />
							</div>
						</div>
					</div>
				</div>

				
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-3 col-form-label">Unit</label>
							<div class="col-sm-9">
								<form:input type="text" class="form-control" path="unit" />
								<form:errors path="unit" />
							</div>
						</div>
					</div>
				</div>
				<div class="form-group row float-right">
					<input type="submit" class="btn btn-success btn-fw"
						" value="${caption}">
				</div>
			</form:form>
		</div>

	</div>
</div>