package ir.afarinesh.realhope.modules.user_management.features.authentication.domain;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class AuthenticatedRole {

    private String name;

    private String userRoleName;

    private List<AuthenticatedPrivilege> authenticatedPrivilegeList;
}
