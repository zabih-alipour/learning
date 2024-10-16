package com.alipour.learning;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class MyCustomResource implements RealmResourceProvider {
    protected static final Logger logger = Logger.getLogger(MyCustomResource.class);

    private final KeycloakSession session;

    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {

    }

    @GET
    @Path("say-hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sayHello() {
        return Response.ok(Map.of("message","Hi, I'm a custom endpoint that is developed by zabix")).build();
    }

    @GET
    @Path("who-am-i")
    @Produces(MediaType.APPLICATION_JSON)
    public Response whoAmI() {
        final AppAuthManager.BearerTokenAuthenticator authenticator = new AppAuthManager.BearerTokenAuthenticator(session);
        final AuthenticationManager.AuthResult authResult = authenticator.authenticate();
        if (Objects.isNull(authResult)) {
            throw new NotAuthorizedException("Bearer");
        } else if (Objects.isNull(authResult.getToken().getIssuedFor())) {
            throw new ForbiddenException("Token is not properly issued for admin-cli");
        }
        //--------------------------------------------------------------------------------------------------------------
        // Some logs
        //--------------------------------------------------------------------------------------------------------------
        final KeycloakContext context = session.getContext();
        logger.infof("context.getContextPath()", context.getContextPath());
        logger.infof("context.getRealm().getName()", context.getRealm().getName());

        return Response.ok(Map.of("message","Hi, I'm a custom secure endpoint that is developed by zabix. Hi " + authResult.getUser().getUsername())).build();
    }

    @POST
    @Path("switch-account")
    @Produces(MediaType.APPLICATION_JSON)
    public Response switchAccount() {
        final AppAuthManager.BearerTokenAuthenticator authenticator = new AppAuthManager.BearerTokenAuthenticator(session);
        final AuthenticationManager.AuthResult authResult = authenticator.authenticate();
        if (Objects.isNull(authResult)) {
            throw new NotAuthorizedException("Bearer");
        } else if (Objects.isNull(authResult.getToken().getIssuedFor())) {
            throw new ForbiddenException("Token is not properly issued for admin-cli");
        }
//        final List<String> list = session.users().searchForUserStream(realm, Map.of())
//                .map(UserModel::getUsername)
//                .toList();
        return Response.ok(Map.of("message","Hi, I'm a custom endpoint that is developed by zabix for switching account, For now these are user list: " + List.of())).build();
    }

}
