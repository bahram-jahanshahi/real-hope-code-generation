package ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.fruits;

import ir.afarinesh.realhope.modules.user_management.features.authentication.domain.AuthenticatedUser;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class AuthenticationFruit {

    private AuthenticatedUser authenticatedUser;

    private boolean authenticated;
}
