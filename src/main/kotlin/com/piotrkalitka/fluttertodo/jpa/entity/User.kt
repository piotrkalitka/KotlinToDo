package com.piotrkalitka.fluttertodo.jpa.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @NotBlank
    @Size(max = 20)
    var username: String,

    @NotBlank
    @Size(max = 50)
    @Email
    var email: String,

    @NotBlank
    @Size(max = 120)
    var password: String,

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role::class)
    @JoinTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")], inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: Set<Role> = mutableSetOf(),

    ) {
    constructor(username: String, email: String, password: String) : this(null, username, email, password, emptySet())
}
