package com.ikram.merchante.demo.controllers;

import com.ikram.merchante.demo.infrastructures.VaultCredentialsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Vault")
public class VaultController {

    @GetMapping("/Refresh") public String refreshSecrets() {
        VaultCredentialsManager.loadAndRefreshContextCredentials();
        return "Secrets from Vault are refreshed";
    }
}
