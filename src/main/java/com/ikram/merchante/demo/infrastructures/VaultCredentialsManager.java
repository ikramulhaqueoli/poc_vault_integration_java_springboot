package com.ikram.merchante.demo.infrastructures;

import org.springframework.stereotype.Service;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

import java.util.*;

@Service
public class VaultCredentialsManager {
    private static final HashMap<String, String> _credentials = new HashMap<>();

    public static void loadCredentials() {
        var vaultTemplate = getVaultTemplate();

        String[] paths = {"me-test-postgresql-db"};
        loadCredentialsFromSecret(paths, vaultTemplate);
        loadPostgreSqlCredentials(vaultTemplate);
    }

    public static void loadAndRefreshContextCredentials() {
        loadCredentials();
        DataSourceConfig.refreshCredentials();
    }

    public static String getCredential(String key) {
        return _credentials.get(key);
    }

    private static VaultTemplate getVaultTemplate() {
        VaultEndpoint endpoint = VaultEndpoint.create("localhost", 8200);
        endpoint.setScheme("http");
        return new VaultTemplate(
                endpoint,
                new TokenAuthentication("root"));
    }

    private static void loadCredentialsFromSecret(String[] paths, VaultTemplate vaultTemplate) {
        for (String path : paths) {
            var responseData = vaultTemplate.read("secret/data/" + path).getData();

            Objects.requireNonNull(responseData)
                    .forEach((key, value) -> {
                        if (value instanceof HashMap) {
                            Map<String, Object> valueMap = (HashMap<String, Object>) value;
                            valueMap.forEach((subKey, subValue) ->
                                    {
                                        if (subValue != null) {
                                            _credentials.put(path + ":" + subKey, subValue.toString());
                                        }
                                    }
                                    );
                        } else {
                            _credentials.put(key, value.toString());
                        }
                    });
        }
    }

    private static void loadPostgreSqlCredentials(VaultTemplate vaultTemplate) {
        var responseData = vaultTemplate.read("database/postgresql/creds/me-test-db-superadmin").getData();
        Objects.requireNonNull(responseData).forEach((key, value) ->
                _credentials.put("me-test-postgresql-db:" + key, value.toString()));
    }
}
