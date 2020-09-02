package ir.afarinesh.realhope.modules.user_management.features.authentication.adapter;

import ir.afarinesh.realhope.core.usecase.UseCaseFruit;
import ir.afarinesh.realhope.core.usecase.UseCasePlant;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.AuthenticationUseCase;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.AuthenticationUseCase.*;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.commands.AuthenticationCommand;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.exceptions.AuthenticationUseCaseException;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.fruits.AuthenticationFruit;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AuthenticationRestController {

    private final AuthenticationUseCase authenticationUseCase;

    public AuthenticationRestController(AuthenticationUseCase authenticationUseCase) {
        this.authenticationUseCase = authenticationUseCase;
    }

    @PostMapping("/login")
    public UseCaseFruit<AuthenticationFruit> authenticate(@RequestBody UseCasePlant<AuthenticationCommand> command) {
        try {
            return authenticationUseCase
                    .authenticate(command);
        } catch (AuthenticationUseCaseException e) {
            return new UseCaseFruit<>(
                    null,
                    false,
                    e.getMessage()
            );
        }
    }

    @PostMapping("/security/test")
    public String testSecurity(@RequestBody String message) {
        return message;
    }
}
