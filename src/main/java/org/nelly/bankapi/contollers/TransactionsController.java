package org.nelly.bankapi.contollers;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionsController {
    private final TransactionService transactionService;

    @PostMapping("/deposit/")
    public ResponseEntity<JSONObject> depositMoney(@RequestBody JSONObject request) {
        return new ResponseEntity<>(transactionService.depositMoney(request), HttpStatus.CREATED);
    }

    @PostMapping("/withdraw/")
    public ResponseEntity<JSONObject> withdrawMoney(@RequestBody JSONObject request) {
        return new ResponseEntity<>(transactionService.withdrawMoney(request), HttpStatus.CREATED);
    }

    @PostMapping("/transfer/")
    public ResponseEntity<JSONObject> transferMoney(@RequestBody JSONObject request) {
        return new ResponseEntity<>(transactionService.transferMoney(request), HttpStatus.CREATED);
    }
}
