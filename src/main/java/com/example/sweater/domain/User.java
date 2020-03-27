package com.example.sweater.domain;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Username can't be empty")
    private String username;
    @NotBlank(message = "Password can't be empty")
    private String password;
    // Transient - говорит о том, что не нужно пытаться получить это поле из БД или как-то его туда добавить
    // Это поле необходимо для валидации
    private boolean active;

    /**
     * Для оповещения пользователя нам нужен email and activationCode, чтобы подтвердить, что пользователь
     * реально владеет этим email
     */
    @Email(message = "Email isn't correct")
    @NotBlank(message = "Email can't be empty")
    private String email;
    private String activationCode;

    // ElementCollection позволяет избавиться от головной боли по формированию дополнительной таблицы для хранения Enum
    // fetch - это параметр который определяет как данные значения будут подгружаться относительно основной сущности usr
    // Когда мы загружаем пользователя, роли его хранятся в отдельной таблице и их необходимо загружать либо Жадным либо Ленивым
    // Жадный, Hibernate сразу же при запросе пользователя будет подгружать все его роли
    // В случае с ленивой подгрузкой он подгрузит роли, только когда пользователь реально обратиться к полю
    // EAGER хорош, когда у нас мало данных, как в случае с ролями
    // Lazy хорош, когда у нас много записей, например Класс Институт, который содержит несколько тысяч студентов
    // их не надо подгружать сразу, они нужны по мере необходимости, тут лучше поставить Lazy
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    // Позволяет создать табличку для набора ролей, user_role, которая будет соединятся с текущей табличкой через user_id
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }
}
