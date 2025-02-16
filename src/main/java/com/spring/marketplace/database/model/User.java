package com.spring.marketplace.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "username", "email"})
@Entity
@Table(name = "users")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class User extends AuditingEntity<BigInteger> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "username", nullable = false, length = 128)
    private String username;

    @Column(name = "email", nullable = false, length = 128)
    private String email;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @ColumnDefault("USER")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();

    @NotAudited
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Favorite> favorites = new HashSet<>();

    @NotAudited
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Message> messages = new HashSet<>();

    @NotAudited
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UserChat> usersChats = new HashSet<>();

    @Override
    public BigInteger getId() {
        return id;
    }

    @Override
    public void setId(BigInteger id) {
        this.id = id;
    }
}
