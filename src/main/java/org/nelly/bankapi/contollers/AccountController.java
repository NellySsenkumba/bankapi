package org.nelly.bankapi.contollers;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/")
    public ResponseEntity<JSONObject> createAccount(@RequestBody JSONObject account) {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<JSONArray> listAccount() {
        return new ResponseEntity<>(accountService.listAccount(), HttpStatus.OK);
    }

    @GetMapping("/{id}/")
    public ResponseEntity<JSONObject> singleAccount(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.singleAccount(id), HttpStatus.OK);
    }

    @GetMapping("/logs/{id}/")
    public ResponseEntity<JSONArray> accountLogs(@PathVariable Long id, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        return new ResponseEntity<>(accountService.accountLogs(id,startDate,endDate), HttpStatus.OK);
    }


}
