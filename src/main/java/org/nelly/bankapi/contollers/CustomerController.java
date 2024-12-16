package org.nelly.bankapi.contollers;

import com.alibaba.fastjson2.JSONObject;
import org.nelly.bankapi.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/")
    public ResponseEntity<?> registerCustomer(@RequestBody JSONObject request) {
        return new ResponseEntity<>(customerService.registerCustomer(request), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<?> listCustomer() {
        return new ResponseEntity<>(customerService.listCustomer(), HttpStatus.OK);
    }

    @GetMapping("/{id}/")
    public ResponseEntity<?> singleCustomer(@PathVariable Long id) {
        return new ResponseEntity<>(customerService.singleCustomer(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/")
    public ResponseEntity<?> editCustomer(@PathVariable Long id, @RequestBody JSONObject request) {
        return new ResponseEntity<>(customerService.editCustomer(id, request), HttpStatus.OK);
    }
}
