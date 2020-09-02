package ir.afarinesh.realhope.modules.user_management.features.authentication.application;

import ir.afarinesh.realhope.core.usecase.UseCaseFruit;
import ir.afarinesh.realhope.core.usecase.UseCasePlant;
import ir.afarinesh.realhope.entities.security.Privilege;
import ir.afarinesh.realhope.entities.security.Role;
import ir.afarinesh.realhope.entities.security.User;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.AuthenticationUseCase;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.commands.AuthenticationCommand;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.exceptions.AuthenticationUseCaseException;
import ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.fruits.AuthenticationFruit;
import ir.afarinesh.realhope.modules.user_management.features.authentication.domain.AuthenticatedPrivilege;
import ir.afarinesh.realhope.modules.user_management.features.authentication.domain.AuthenticatedRole;
import ir.afarinesh.realhope.modules.user_management.features.authentication.domain.AuthenticatedUser;
import ir.afarinesh.realhope.shares.repositories.UserSpringJpaRepository;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationApplication implements AuthenticationUseCase {

    private final MessageSource messageSource;
    private final UserSpringJpaRepository userSpringJpaRepository;

    public AuthenticationApplication(MessageSource messageSource,
                                     UserSpringJpaRepository userSpringJpaRepository) {
        this.userSpringJpaRepository = userSpringJpaRepository;
        this.messageSource = messageSource;
    }

    @Override
    public UseCaseFruit<AuthenticationFruit> authenticate(UseCasePlant<AuthenticationCommand> command) throws AuthenticationUseCaseException {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String username = command.getPlant().getUsername();
        String password = command.getPlant().getPassword();

        // Validate inputs (username, password)
        if (username == null || password == null) {
            throw new AuthenticationUseCaseException(
                    /*messageSource.getMessage(
                            "authentication.error.invalid_credential_inputs",
                            null,
                            Locale.forLanguageTag(command.getLocale())
                    )*/
                    "invalid_credential_inputs"
            );
        }

        // Find user by username
        Optional<User> userOptional = userSpringJpaRepository.findUserByUsername(username);

        // Check whether user is found or not
        if (userOptional.isEmpty()) {
            throw new AuthenticationUseCaseException(
                   /* messageSource.getMessage(
                            "authentication.error.user_is_not_found_by_username",
                            null,
                            Locale.forLanguageTag(command.getLocale())
                    )*/
                    "user_is_not_found_by_username"
            );
        }

        // Verify password
        if (!passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new AuthenticationUseCaseException(
                    /*messageSource.getMessage(
                            "authentication.error.password_is_not_valid",
                            null,
                            Locale.forLanguageTag(command.getLocale())
                    )*/
                    "password_is_not_valid"
            );
        }

        return new UseCaseFruit<>(
                new AuthenticationFruit(
                        new AuthenticatedUser(
                                userOptional.get().getId(),
                                command.getLocale().equals("en") ? userOptional.get().getFirstName() : userOptional.get().getFaFirstName(),
                                command.getLocale().equals("en") ? userOptional.get().getLastName() : userOptional.get().getFaLastName(),
                                userOptional.get()
                                        .getRoles()
                                        .stream()
                                        .map(role -> this.map(role, command.getLocale()))
                                        .collect(Collectors.toList())
                        ),
                        true
                ),
                true,
                "Authentication is done successfully"
        );
    }

    protected AuthenticatedRole map(Role role, String locale) {
        return new AuthenticatedRole(
                role
                        .getName(),
                locale.equals("fa") ? role.getUserRoleNameEnum().fa() : role.getUserRoleNameEnum().en(),
                role
                        .getPrivileges()
                        .stream()
                        .map(this::map)
                        .collect(Collectors.toList())
        );
    }

    protected AuthenticatedPrivilege map(Privilege privilege) {
        return new AuthenticatedPrivilege(
                privilege.getName()
        );
    }

}
