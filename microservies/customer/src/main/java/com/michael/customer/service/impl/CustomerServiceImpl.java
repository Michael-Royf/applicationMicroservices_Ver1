package com.michael.customer.service.impl;

import com.michael.amqp.RabbitMQMessageProducer;
import com.michael.clients.fraud.FraudCheckResponse;
import com.michael.clients.fraud.FraudClient;
import com.michael.clients.notification.NotificationRequest;
import com.michael.customer.entity.Customer;
import com.michael.customer.exception.domain.CustomerIsFraudsterException;
import com.michael.customer.exception.domain.CustomerNotFoundException;
import com.michael.customer.exception.domain.EmailAlreadyExistException;
import com.michael.customer.payload.request.CustomerRequest;
import com.michael.customer.payload.response.CustomerResponse;
import com.michael.customer.repository.CustomerRepository;
import com.michael.customer.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FraudClient fraudClient;

//    @Autowired
//    private NotificationClient notificationClient;

    @Autowired
    private RabbitMQMessageProducer rabbitMQMessageProducer;


    @Override
    public CustomerResponse registerCustomer(CustomerRequest customerRequest) {
        Optional<Customer> customerDB = customerRepository.findByEmail(customerRequest.getEmail());
        if (customerDB.isPresent()) {
            throw new EmailAlreadyExistException("Email already exist!");
        }

        Customer customer = Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .email(customerRequest.getEmail())
                .build();
        customer = customerRepository.saveAndFlush(customer);

//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
//                "http://FRAUD/api/v1/fraud-check/{customerId}",
//                FraudCheckResponse.class,
//                customer.getId()
//        );

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());


        if (fraudCheckResponse.getIsFraudster()) {
            throw new CustomerIsFraudsterException("Fraudster");
        }

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .toCustomerId(customer.getId())
                .toCustomerEmail(customer.getEmail())
                .message(String.format("Hi %s, welcome to our website...", customer.getFirstName()))
                .build();
        //    notificationClient.sendNotification(notificationRequest);


        rabbitMQMessageProducer.publish(notificationRequest, "internal.exchange",
                "internal.notification.routing-key");

        return mapper.map(customer, CustomerResponse.class);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> mapper.map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = findCustomer(customerId);
        return mapper.map(customer, CustomerResponse.class);
    }

    @Override
    public CustomerResponse updateCustomer(Long customerId, CustomerRequest customerRequest) {
        Customer customer = findCustomer(customerId);
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setEmail(customerRequest.getEmail());
        customer = customerRepository.save(customer);
        return mapper.map(customer, CustomerResponse.class);
    }

    @Override
    public String deleteCustomer(Long customerId) {
        Customer customer = findCustomer(customerId);
        customerRepository.delete(customer);
        return "Customer with id: " + customerId + " was deleted";
    }

    private Customer findCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id: " + customerId + " not found"));
    }
}
