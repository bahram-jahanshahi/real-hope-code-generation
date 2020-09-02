package ir.afarinesh.realhope.entities.security;

import ir.afarinesh.realhope.entities.security.enums.UserRoleNameEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String faFirstName;

    @Column(nullable = false)
    private String faLastName;

    @Column(nullable = false)
    private String username; // Returns: the username used to authenticate the user.

    @Column(nullable = false)
    private String password; // Returns: the password used to authenticate the user.

    private boolean accountNonExpired; // Returns: true if the user's account is valid (ie non-expired), false if no longer valid (ie expired)

    private boolean accountNonLocked; // Returns: true if the user is not locked, false otherwise

    private boolean credentialsNonExpired; // Returns: true if the user's credentials are valid (ie non-expired), false if no longer valid (ie expired)

    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "UserRole",
            joinColumns = @JoinColumn(name = "UserId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "RoleId", referencedColumnName = "id")
    )
    private Collection<Role> roles;

    public User() {

    }

    private User(String firstName,
                 String lastName,
                 String faFirstName,
                 String faLastName,
                 String username,
                 String password,
                 Collection<Role> roles,
                 boolean accountNonExpired,
                 boolean accountNonLocked,
                 boolean credentialsNonExpired,
                 boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.faFirstName = faFirstName;
        this.faLastName = faLastName;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : roles) {
            grantedAuthorities.add(role);
            Collection<Privilege> privileges = role.getPrivileges();
            for (Privilege privilege : privileges) {
                grantedAuthorities.add(privilege);
            }
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFaFirstName() {
        return faFirstName;
    }

    public String getFaLastName() {
        return faLastName;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public boolean hasRole(Set<UserRoleNameEnum> expectedRoles) {
        if (getRoles() == null || getRoles().size() == 0) {
            return false;
        }
        return getRoles()
                .stream()
                .anyMatch(role -> expectedRoles.stream().anyMatch(expectedRole -> expectedRole.equals(role.getUserRoleNameEnum())));
    }
}
