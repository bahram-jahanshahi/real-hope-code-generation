package ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter

public class AuthenticationCommand {

    private String username;

    private String password;
}
