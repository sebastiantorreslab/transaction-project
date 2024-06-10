package com.msuser.msuser.util;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

public class keycloakProvider {

    private static final String SERVER_URL = "http://localhost:9090";
    private static final String REALM_NAME = "triwal-realm-dev";
    private static final String REALM_MASTER ="master";
    private static final String ADMIN_CLI = "admin-cli";
    private static final String USER_CONSOLE = "admin";
    private static final String USER_PASS = "admin";
    private static final String CLIENT_SECRET = "2AW8lVZlq25DVBwI8UTLRYaK95bQsg9p"; // todo: ENV


    public static RealmResource getRealmResource(){
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(USER_PASS)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();

        return keycloak.realm(REALM_NAME);
    }


    public static UsersResource getUserResource(){
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }




}
