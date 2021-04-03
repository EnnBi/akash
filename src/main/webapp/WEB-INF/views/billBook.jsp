<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="col-12 grid-margin">
	<div class="card">
		<div class="card-body">
			<h4 class="card-title">Bill Book</h4>
			<form:form action="/bill-book/save" class="form-sample"
				modelAttribute="billBook" method="post">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Receipt No.</label>
							<div class="col-sm-8">
								<form:input type="text" class="form-control"
									path="receiptNumber" required="required" id="receipt"/>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Date</label>
							<div class="col-sm-8">
								<form:input type="date" class="form-control" path="date" required="required"/>
							</div>
						</div>
					</div>

				</div>
				<div class="row">

					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Vehicle</label>
							<div class="col-sm-8">
								<form:select class="form-control" path="vehicle">
									<form:option value="">Select any Vehicle</form:option>
									<form:options items="${vehicles}" itemLabel="number"
										itemValue="id"/>
								</form:select>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Phone No.</label>
							<div class="col-sm-8">
								<input class="form-control" type="text" id="contact" readonly="true"/>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Name</label>
							<div class="col-sm-8">
								<form:select class="form-control" path="customer" id="customer" required="required">
									<form:option value="">Select Customer</form:option>
									<c:forEach var="customer" items="${customers}">
										<form:option value="${customer.id}" contact="${customer.contact}" address="${customer.address}">
											<c:out value="${customer.name} ${customer.address}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group row">
							<label class="col-sm-4 col-form-label">Address</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="address" readonly="true">
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<div class="form-group row">
							<label class="col-sm-2 col-form-label">Sites</label>
							<div class="col-sm-10">
								<form:select class="form-control" path="site" id="site">
									<option value="">Select any Site</option>
								</form:select>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-md-12">
						<div class="form-group row">
							<div class="col-sm-11">
								<p class="card-description">Sales</p>
							</div>
							<div class="float-right">
								<button type="button" class="btn btn-primary" id="add">Add</button>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<div class="row sales-row">
					<div class="col-md-12">
						<div class="form-group row">
							<div class="col-sm-3">
								<form:select class="form-control product" required="required"
									path="sales[0].product">
									<form:option value="">Select Products</form:option>
									<form:options items="${products}" itemLabel="name"
										itemValue="id" />
								</form:select>
							</div>
							<div class="col-sm-2">
								<form:select class="form-control size" path="sales[0].size" required="required">
									<option value="">Select Size</option>
								</form:select>
							</div>
							<div class="col-sm-2">
								<form:input type="text" class="form-control quantity" pattern="[0-9]*"
									placeholder="Quantity" path="sales[0].quantity" required="required"/>
							</div>
							<div class="col-sm-2">
								<form:input type="text" class="form-control unit" pattern="[0-9]*"
									placeholder="Unit Price" path="sales[0].unitPrice" required="required"/>
							</div> 
							<div class="col-sm-2">
								<form:input type="text" class="form-control amount" 
									placeholder="Amount" path="sales[0].amount" readonly="true"/>
							</div>
							<div class="col-sm-1">
								<button type="button" class="btn btn-danger delete">
									<i class="mdi mdi-delete"></i>
								</button>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group row">
									<label class="col-sm-4 col-form-label">Loaders</label>
									<div class="col-sm-8">
										<form:select class="form-control" path="loaders" id="loaders"
											multiple="multiple">
											<c:forEach var="customer" items="${labours}">
												<form:option value="${customer.id}">
													<c:out value="${customer.name}--${customer.address}" />
												</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group row">
									<label class="col-sm-4 col-form-label">Unloaders</label>
									<div class="col-sm-8">
										<form:select class="form-control" path="unloaders" id="unloaders"
											multiple="multiple">
											<c:forEach var="customer" items="${labours}">
												<form:option value="${customer.id}">
													<c:out value="${customer.name}--${customer.address}" />
												</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group row">
									<div class="col-sm-6 col-form-label">
										<label class="float-right">Loading:</label>
									</div>
									<div class="col-sm-4">
										<form:input type="text" class="form-control" pattern="[0-9]*"
											path="loadingAmount" id="loadingAmount"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 ">
								<div class="form-group row">
									<div class="col-sm-6 col-form-label">
										<label class="float-right">Unloading:</label>
									</div>
									<div class="col-sm-4">
										<form:input type="text" class="form-control" pattern="[0-9]*"
											path="unloadingAmount" id="unloadingAmount"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 ">
								<div class="form-group row">
									<div class="col-sm-6 col-form-label">
										<label class="float-right">Carriage:</label>
									</div>
									<div class="col-sm-4">
										<form:input type="text" class="form-control" path="carraige" id="carraige" pattern="[0-9]*"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 ">
								<div class="form-group row">
									<div class="col-sm-6 col-form-label">
										<label class="float-right">Total:</label>
									</div>
									<div class="col-sm-4">
										<form:input type="text" class="form-control" path="total" id="total" readonly="true"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 ">
								<div class="form-group row">
									<div class="col-sm-6 col-form-label">
										<label class="float-right">Paid:</label>
									</div>
									<div class="col-sm-4">
										<form:input type="text" class="form-control" path="paid" id="paid" pattern="[0-9]*" required="required"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 ">
								<div class="form-group row">
									<div class="col-sm-6 col-form-label">
										<label class="float-right">Balance:</label>
									</div>
									<div class="col-sm-4">
										<form:input type="text" class="form-control" path="balance" readonly="true"/>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group row float-right">
							<input type="submit" class="btn btn-success btn-fw"
								value="Submit">
						</div>
					</div>
				</div>
			</form:form>

		</div>
	</div>
</div>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.slim.min.js"
	integrity="sha512-6ORWJX/LrnSjBzwefdNUyLCMTIsGoNP6NftMy2UAm1JBm6PRZCO1d7OHBStWpVFZLO+RerTvqX/Z9mBFfCJZ4A=="
	crossorigin="anonymous"></script>

<script type="text/javascript">
	$(document).ready(
			function() {
				$("#add").click(
						function() {
							$(".sales-row:last").clone().insertAfter(
									".sales-row:last");
							$(".sales-row:last").find(':input').val('');
							updateIndex();
						});

				$(document).on('click', '.delete', function() {
					if ($('.sales-row').length > 1) {
						$(this).parent().parent().parent().parent().remove();
						updateIndex();
						updateTotal();
					}

				});

				$(document).on(
						'change',
						'.product',
						function() {

							var productElement = $(this);
							var sizeElmnt = productElement.parent().parent()
									.parent().find('.size');
							var id = productElement.val();
							var url = "/product/" + id + "/sizes";
							console.log(url);
							$.get(url,
									function(data) {
										console.table(data);
										sizeElmnt.find('option').not(':first')
												.remove();
										$.each(data, function(key, value) {
											sizeElmnt.append($(
													"<option></option>").attr(
													"value", value.id).text(
													value.name));
										});
									});

						});
				
				$('#customer').change(function(){
					
					var id = $(this).val();
					var address =$('option:selected', this).attr('address');
					var contact = $('option:selected', this).attr('contact');
					$('#address').val(address);
					$('#contact').val(contact);
					
					var url = "/user/"+id+"/sites";
					$.get(url,
							function(data) {
								console.table(data);
								$('#site').find('option').not(':first')
										.remove();
								$.each(data, function(key, value) {
									$('#site').append($(
											"<option></option>").attr(
											"value", value.id).text(
											value.name));
								});
							});
				});

				function updateIndex() {
					$('.sales-row').get().forEach(
							function(entry, index, array) {
								console.log()
								$(entry).find('input, select').each(
										function() {
											var name = $(this).attr('name')
													.replace(/\[(.+?)\]/g,
															"[" + index + "]");
											$(this).attr('name', name)
										});
							});
				}
				
				$(document).on('change','.quantity,.unit',function(){
					var currentElmnt = $(this);
					var quantity,unit,amount = 1;
					if(currentElmnt.attr('class').search('quantity')>-1){
						quantity=currentElmnt.val();
						unit = currentElmnt.closest('.sales-row').find('.unit').val();
					}
					else{
						unit=currentElmnt.val();
						quantity = currentElmnt.closest('.sales-row').find('.quantity').val();
					}
					amount = currentElmnt.closest('.sales-row').find('.amount').val();
					if(unit &&quantity)
						currentElmnt.closest('.sales-row').find('.amount').val(unit*quantity);
					
					updateTotal();
				});
				
				$('#loadingAmount,#unloadingAmount,#carraige').change(function(){
					if($(this).attr('id').search('loadingAmount')>-1)
						if(Number($('#loadingAmount').val()))
							$('#loaders').attr('required','required');
						else
							$('#loaders').removeAttr('required');
					
					if($(this).attr('id').search('unloadingAmount')>-1)
						if(Number($('#unloadingAmount').val()))
							$('#unloaders').attr('required','required');
						else
							$('#unloaders').removeAttr('required');
						
					updateTotal();
					
				});
				
				$('#paid').change(function(){
					updatePaid();
				});
				
				function updateTotal(){
					var amount=0;
					$('.sales-row').find('.amount').each(function(){
						var amt = $(this).val();
						amount = amount + Number(amt);
					});
					var loadingAmt=$('#loadingAmount').val()?$('#loadingAmount').val():0;
					var unloadingAmt=$('#unloadingAmount').val()?$('#unloadingAmount').val():0;
					var carraige=$('#carraige').val()?$('#carraige').val():0;
					amount+=Number(loadingAmt);
					amount+=Number(unloadingAmt);
					amount+=Number(carraige);
					$('#total').val(amount);
					$('#balance').val(amount);
					updatePaid();
				}
				
				function updatePaid(){
					var total =Number($('#total').val());
					var paid =Number($('#paid').val());
					$('#balance').val(total-paid);
				}
				
				$('#receipt').change(function(){
					var number=$(this).val();
					var url = "/bill-book/receipt/"+number;
					$.get(url,function(data){
						if(data){
							alert('Receipt Number already exists');
							$('#receipt').val('');
						}
					});
				});
				
			});
</script>
