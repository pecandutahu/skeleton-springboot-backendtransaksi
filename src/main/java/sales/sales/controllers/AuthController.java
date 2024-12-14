package sales.sales.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sales.sales.dto.LoginUserRequest;
import sales.sales.dto.WebResponse;
import sales.sales.dto.TokenResponse;
import sales.sales.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
        path = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )

    public WebResponse<TokenResponse> login(@Valid @RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);

        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();

    }
    
}
