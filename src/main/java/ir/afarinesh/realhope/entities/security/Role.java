package ir.afarinesh.realhope.entities.security;

import ir.afarinesh.realhope.entities.security.enums.UserRoleNameEnum;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleNameEnum userRoleNameEnum;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "RolePrivilege",
            joinColumns = @JoinColumn(name = "RoleId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "PrivilegeId", referencedColumnName = "id")
    )
    private Collection<Privilege> privileges;

    @Override
    public String getAuthority() {
        return "ROLE_"+getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Long getId() {
        return id;
    }

    public UserRoleNameEnum getUserRoleNameEnum() {
        return userRoleNameEnum;
    }

    public void setUserRoleNameEnum(UserRoleNameEnum userRoleNameEnum) {
        this.userRoleNameEnum = userRoleNameEnum;
    }

}
