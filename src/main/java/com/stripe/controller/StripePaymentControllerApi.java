package com.stripe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.CustomerData;

@RestController
@RequestMapping("/api")
public class StripePaymentControllerApi {
	
	@Value("${stripe.apikey}")
	String stripeKey;
	
	@RequestMapping("/createCustomer")
	public CustomerData createCustomer(@RequestBody CustomerData customerData) throws StripeException {
		
		Stripe.apiKey = stripeKey;
		
		Map<String, Object> params = new HashMap<>();
		params.put("name", customerData.getName());
		params.put("email", customerData.getEmail());
		
		Customer customer = Customer.create(params);
		customerData.setCustomerId(customer.getId());
		return customerData;
	}
	
	@RequestMapping("/getAllCustomer")
	public List<CustomerData> getAllCustomer() throws StripeException {
		
		Stripe.apiKey = stripeKey;
		
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		
		CustomerCollection customer = Customer.list(params);
		List<CustomerData> allCustomer = new ArrayList<CustomerData>();
		
		for(int i = 0; i < customer.getData().size(); i++) {
			CustomerData customerData = new CustomerData();
			customerData.setCustomerId(customer.getData().get(i).getId());
			customerData.setName(customer.getData().get(i).getName());
			customerData.setEmail(customer.getData().get(i).getEmail());
			allCustomer.add(customerData);
		}
		
		return allCustomer;
	}
}
