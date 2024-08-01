var suspensionInterceptor = axios.interceptors.response.use(
	response => response,
	error => {
		if (error.response && error.response.status === 451) {
			alert(`Account suspended!`);
			redirectLogout();
		}

		return Promise.reject(error);

	});

window.onload = redirectHome;

function redirectHome() {
	redirectUrlPath("/home");
}


function redirectLogin() {
	redirectUrlPath("/login");
}

function redirectAllPizzasAdmin() {
	redirectUrlPathWithScript("/admin/allPizzas");
}

function redirectAddPizza() {
	redirectUrlPath("/admin/createPizza");
}

function redirectInvalidPath() {
	redirectUrlPath("/admin/invalidPath");
}

function redirectPizzaDetails(pizzaId) {
	redirectUrlPath("/admin/pizzaDetails/" + pizzaId);
}

function redirectUpdatePizza(pizzaId) {
	redirectUrlPath("/admin/updatePizza/" + pizzaId);
}

function redirectAllSizesByPizza(pizzaId) {
	redirectUrlPathWithScript("/admin/allSizes/" + pizzaId);
}

function redirectAllSizes() {
	redirectUrlPathWithScript("/admin/allSizes");
}

function redirectAddPizzaSize() {
	redirectUrlPath("/admin/createPizzaSize");
}

function redirectUpdatePizzaSize(pizzaSizeId) {
	redirectUrlPath("/admin/updatePizzaSize/" + pizzaSizeId);
}

function redirectPizzaSizeDetails(pizzaSizeId) {
	redirectUrlPath("/admin/pizzaSizeDetails/" + pizzaSizeId);
}

function redirectSizeError() {
	redirectUrlPath(`/admin/existingSizeError`);
}

function redirectRegister() {
	redirectUrlPath("/register");
}

function redirectAllPizzasUser() {
	redirectUrlPathWithScript("/pizzas/allPizzas");
}

function redirectPizzaDetailsUser(pizzaId) {
	redirectUrlPath("/pizzas/pizzaDetails/" + pizzaId);
}

function redirectAddPizzaToCart(pizzaId) {
	redirectUrlPath("/carts/addToCart/" + pizzaId);
}

function redirectCart() {
	redirectUrlPath("/carts/cart");
}

function redirectAddressConfirm() {
	redirectUrlPath("/orders/confirmUserData");
}

function redirectCartError() {
	redirectUrlPath("/carts/cartError");
}

function redirectPhoneConfirmation() {
	redirectUrlPath("/orders/phoneConfirmation");
}

function redirectOrderConfirmation() {
	redirectUrlPath("/orders/orderConfirmation");
}

function redirectCancelOrder() {
	redirectUrlPath("/orders/cancelOrder");
}

function redirectOrderCompleted() {
	redirectUrlPath("/orders/orderCompleted");
}

function redirectAllCustomers() {
	redirectUrlPathWithScript("/admin/allCustomers");
}

function redirectCustomerDetails(customerId) {
	redirectUrlPath("/admin/customerDetails/" + customerId);
}

function redirectAllOrders() {
	redirectUrlPathWithScript("/admin/allOrders");
}

function redirectOrderDetails(orderId) {
	redirectUrlPath("/admin/orderDetails/" + orderId);
}

function redirectRegistrationCompleted() {
	redirectUrlPath(`/registerComplete`);
}

function redirectRegistrationError() {
	redirectUrlPath(`/registerFail`);
}

function redirectItemAdded() {
	redirectUrlPath(`/carts/itemAdded`);
}

function login() {
	var formData = new FormData(document.getElementById("loginForm"));
	var serializedFormData = new URLSearchParams(formData).toString();

	document.cookie = "JSESSIONID=" + Math.random() + "; SameSite=None; Secure";

	axios.post('http://localhost:8080/login', serializedFormData, {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded'
		},
		withCredentials: true
	})
		.then(() => {
			confirmLoginPass();
		})
		.catch(() => {
			alert("Failed!");
		});
}


function confirmLoginPass() {
	axios.post(`http://localhost:8080/loginPassConfirm`)
		.then(() => {
			window.location.href = `/`;
		})

		.catch(() => {
			redirectUrlPath(`/loginErrorPage`);
		})
};


function redirectLogout() {
	axios.post(`http://localhost:8080/loggedout`)
		.then(() => {
			window.location.href = "/";
		})

		.catch(() => {
			alert("Logout error!");
		})
}

function addressConfirmed() {
	var formData = new FormData(document.getElementById("shippingAddressForm"));
	var serializedFormData = new URLSearchParams(formData).toString();
	if (validateShippingAddress()) {
		axios.post(`http://localhost:8080/orders/confirmShippingAddress`, serializedFormData)
			.then(() => {
				redirectPhoneConfirmation();
			})

			.catch(() => {
				alert("Failed!");
			})
	}
}

function phoneConfirmed() {
	var formData = new FormData(document.getElementById("customerForm"));
	var serializedFormData = new URLSearchParams(formData).toString();
	if (validateCustomer()) {
		axios.post(`http://localhost:8080/orders/phoneConfirmation`, serializedFormData)
			.then(() => {
				redirectOrderConfirmation();
			})

			.catch(() => {
				alert("Failed!");
			})
	}
}

function registerUser() {
	if (validateRegForm()) {
		var formData = new FormData(document.getElementById(`registrationForm`));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/register`, serializedFormData)
			.then(() => {
				redirectRegistrationCompleted();
			})

			.catch((error) => {
				if (error.response.status === 409) {
					redirectRegistrationError();
				} else {
					alert(`Failed!`);
				}
			})
	}
}


function addPizza() {
	if (validatePizza()) {
		var formData = new FormData(document.getElementById(`pizzaForm`));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/admin/createPizza`, serializedFormData)
			.then(() => {
				redirectAllPizzasAdmin();
			})

			.catch(() => {
				alert(`Failed!`);

			})
	}
}

function addPizzaToCart() {
	if (validateCartItem()) {
		var formData = new FormData(document.getElementById(`cartItemForm`));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/carts/storeCartItem`, serializedFormData)
			.then(() => {
				redirectItemAdded();
			})

			.catch((error) => {
				if (error.response.status === 406) {
					alert(error.response.data);
				} else {
					alert(`Failed!`);
				}
			})
	}
}

function addPizzaSize() {
	if (validatePizzaSize()) {
		var formData = new FormData(document.getElementById(`pizzaSizeForm`));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/admin/createPizzaSize`, serializedFormData)
			.then(() => {
				redirectAllSizes();
			})

			.catch((error) => {
				if (error.response.status === 409) {
					redirectSizeError();
				} else {
					alert(`Failed!`);
				}
			})
	}
}

function deletePizza(pizzaId) {
	if (confirm("Are you sure you want to remove this pizza?")) {
		axios.get(`http://localhost:8080/admin/deletePizza/${pizzaId}`)
			.then(() => {
				redirectAllPizzasAdmin();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}


function deletePizzaSize(pizzaSizeId) {
	if (confirm("Are you sure you want to remove this pizza size?")) {
		axios.get(`http://localhost:8080/admin/deletePizzaSize/${pizzaSizeId}`)
			.then(() => {
				redirectAllSizes();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}



function eraseItem(itemId) {
	if (confirm("Remove this item from cart?")) {
		axios.get(`http://localhost:8080/carts/deleteItem/${itemId}`)
			.then(() => {
				redirectCart();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function eraseAllItems(cartId) {
	if (confirm("Are you sure you want to clear your cart?")) {
		axios.get(`http://localhost:8080/carts/deleteAllItems/${cartId}`)
			.then(() => {
				redirectCart();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function redirectValidateCart(cartId) {
	axios.get(`http://localhost:8080/carts/validateCart/${cartId}`)
		.then(() => {
			redirectAddressConfirm();
		})

		.catch((error) => {
			if (error.response.status === 422) {
				redirectCartError();
			} else {
				alert(`Failed!`);
			}

		})
}

function executeOrder() {
	axios.get(`http://localhost:8080/orders/createOrder`)
		.then(() => {
			redirectOrderCompleted();
		})

		.catch(() => {
			alert("Failed!");
		})
}

function suspendUser(userId) {
	if (confirm("Are you sure you want to suspend this user?")) {
		axios.get(`http://localhost:8080/admin/suspendUser/${userId}`)
			.then(() => {
				redirectAllCustomers();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function reactivateUser(userId) {
	if (confirm("Are you sure you want to reactivate this user?")) {
		axios.get(`http://localhost:8080/admin/clearSuspension/${userId}`)
			.then(() => {
				redirectAllCustomers();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function deleteOrder(orderId) {
	if (confirm("Are you sure you want to clear this order?")) {
		axios.get(`http://localhost:8080/admin/deleteOrder/${orderId}`)
			.then(() => {
				redirectAllOrders();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function deleteCustomer(customerId) {
	if (confirm("Are you sure you want to remove this customer?")) {
		axios.get(`http://localhost:8080/admin/deleteCustomer/${customerId}`)
			.then(() => {
				redirectAllCustomers();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function searchPizza() {
	if (validateKeyword()) {
		var keyword = document.getElementById(`keyword`).value;
		redirectUrlPathWithScript(`/pizzas/searchPizza/${keyword}`);
	}
}


function redirectUrlPath(path) {
	axios.get(path)
		.then(response => {
			document.getElementById(`axiosLoadedContent`).innerHTML = response.data;
		})
		.catch(error => {
			console.error(`Error loading home page:`, error);
		});
};

function redirectUrlPathWithScript(path) {
	axios.get(path)
		.then(response => {
			var contentDiv = document.getElementById(`axiosLoadedContent`);
			contentDiv.innerHTML = response.data;

			var scripts = contentDiv.querySelectorAll(`script`);
			scripts.forEach((oldScript) => {
				var newScript = document.createElement(`script`);
				newScript.type = `text/javascript`;
				if (oldScript.src) {
					newScript.src = oldScript.src;
				} else {
					newScript.textContent = oldScript.textContent;
				}
				document.body.appendChild(newScript);
				oldScript.parentNode.removeChild(oldScript);
			});
		})
		.catch(error => {
			console.error(`Error loading login page:`, error);
		});
};

