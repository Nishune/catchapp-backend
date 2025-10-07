package com.catchapp.api;

import com.catchapp.api.dto.PasswordChangeRequest;
import com.catchapp.api.dto.PasswordChangeResponse;
import com.catchapp.service.UserService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserResourceTest {

    private UserService userService;
    private UserResource userResource;
    private SecurityContext securityContext;
    private Principal principal;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userResource = new UserResource();
        userResource.userService = userService;
        securityContext = mock(SecurityContext.class);
        principal = mock(Principal.class);
    }

    @Test
    void shouldChangePasswordWhenValidRequest() {

        PasswordChangeRequest req = new PasswordChangeRequest();
        req.setOldPassword("oldpass123");
        req.setNewPassword("newpass456");

        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("testuser");

        Response response = userResource.changePassword(req, securityContext);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isInstanceOf(PasswordChangeResponse.class);
        PasswordChangeResponse body = (PasswordChangeResponse) response.getEntity();
        assertThat(body.getMessage()).isEqualTo("Password changed successfully");

        verify(userService).changePassword("testuser", "oldpass123", "newpass456");
    }

    @Test
    void shouldFailWhenOldPasswordIncorrect() {

        PasswordChangeRequest req = new PasswordChangeRequest();
        req.setOldPassword("wrongOld");
        req.setNewPassword("newpass456");

        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("testuser");

        doThrow(new IllegalArgumentException("Incorrect current password"))
                .when(userService).changePassword(anyString(), anyString(), anyString());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userResource.changePassword(req, securityContext)
        );

        assertThat(thrown.getMessage()).isEqualTo("Incorrect current password");
        verify(userService).changePassword("testuser", "wrongOld", "newpass456");
    }
    @Test
    void shouldFailWhenMissingOrInvalidToken() {

        PasswordChangeRequest req = new PasswordChangeRequest();
        req.setOldPassword("oldpass123");
        req.setNewPassword("newpass456");

        when(securityContext.getUserPrincipal()).thenReturn(null);


        Response response = userResource.changePassword(req, securityContext);


        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        assertThat(response.getEntity()).isInstanceOf(PasswordChangeResponse.class);
        PasswordChangeResponse body = (PasswordChangeResponse) response.getEntity();
        assertThat(body.getMessage()).isEqualTo("Missing or invalid token");

        verify(userService, never()).changePassword(anyString(), anyString(), anyString());
    }
}
