package org.nelly.bankapi.contollers;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.services.TransactionLogsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction-logs")
public class TransactionLogsController {
    private final TransactionLogsService transactionLogsService;


    @GetMapping("/")
    public ResponseEntity<JSONArray> listTransactionLogs() {
        return new ResponseEntity<>(transactionLogsService.listTransactionLogs(), HttpStatus.OK);
    }
}
