package org.nelly.bankapi.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.models.Customer;
import org.nelly.bankapi.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService implements IService {

    private final CustomerRepository customerRepository;

    public JSONObject registerCustomer(JSONObject request) {
        requires(
                List.of("firstName", "lastName", "email", "phoneNumber", "address", "dob", "nin"),
                request
        );
        Customer customer = Customer.builder()
                .firstName(request.getString("firstName"))
                .lastName(request.getString("lastName"))
                .email(request.getString("email"))
                .phoneNumber(request.getString("phoneNumber"))
                .address(request.getString("address"))
                .dob(LocalDate.parse(request.getString("dob"), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .nin(request.getString("nin"))
                .build();
        return JSONObject.from(customerRepository.save(customer));
    }

    public JSONArray listCustomer() {
        return JSONArray.from(customerRepository.findAll());
    }

    public JSONObject singleCustomer(Long id) {
        return JSONObject.from(customerRepository.findById(id).orElseThrow());
    }

    public JSONObject editCustomer(Long id, JSONObject request) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        if (request.containsKey("firstName")) {
            customer.setFirstName(request.getString("firstName"));
        }
        if (request.containsKey("lastName")) {
            customer.setLastName(request.getString("lastName"));
        }

        return JSONObject.from(customerRepository.save(customer));
    }
}
