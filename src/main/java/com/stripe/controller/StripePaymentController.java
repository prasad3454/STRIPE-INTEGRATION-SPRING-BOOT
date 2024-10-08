package com.stripe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.CustomerData;

@Controller
public class StripePaymentController {
	
	@Value("${stripe.apikey}")
	String stripeKey;
	
	@RequestMapping("/index")
	public String index(Model model) throws StripeException {
		
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
		
		model.addAttribute("customer", allCustomer);
		return "index";
	}
	
	@RequestMapping("/createCustomer")
	public String createCustomer(CustomerData customerData) {
		return "create-customer";
	}
	
	@RequestMapping("/addCustomer")
	public String addCustomer(CustomerData customerData) throws StripeException {
		
		Stripe.apiKey = stripeKey;
		Map<String, Object> params = new HashMap<>();
		params.put("name", customerData.getName());
		params.put("email", customerData.getEmail());
		
		Customer customer = Customer.create(params);
		return "success";
	}
}
