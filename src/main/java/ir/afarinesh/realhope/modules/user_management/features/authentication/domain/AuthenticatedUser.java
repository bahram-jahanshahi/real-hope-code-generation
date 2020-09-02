package ir.afarinesh.realhope.modules.user_management.features.authentication.domain;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class AuthenticatedUser {

    private Long userId;

    private String firstName;

    private String lastName;

    private List<AuthenticatedRole> authenticatedRoleList;
}
