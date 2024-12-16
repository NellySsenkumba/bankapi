package org.nelly.bankapi.contollers;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class PingController {
    @RequestMapping()
    public ResponseEntity<JSONObject> ping() {
        JSONObject response = new JSONObject();
        response.put("message", "pong");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}
