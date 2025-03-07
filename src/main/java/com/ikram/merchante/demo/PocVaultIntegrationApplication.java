package com.ikram.merchante.demo;

import com.ikram.merchante.demo.infrastructures.VaultCredentialsManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PocVaultIntegrationApplication {

	public static void main(String[] args) {
		VaultCredentialsManager.loadCredentials();

		SpringApplication.run(PocVaultIntegrationApplication.class, args);
	}

}
