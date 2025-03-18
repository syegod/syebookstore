package io.syebookstore.api.account.repository;

import io.syebookstore.LocalDateTimeConverter;
import io.syebookstore.api.account.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@Table(name = "accounts")
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String email;
  private String description;

  @Column(name = "password_hash")
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  private AccountStatus status;

  @Column(name = "created_at")
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime updatedAt;

  public Long id() {
    return id;
  }

  public Account id(Long id) {
    this.id = id;
    return this;
  }

  public String username() {
    return username;
  }

  public Account username(String username) {
    this.username = username;
    return this;
  }

  public String email() {
    return email;
  }

  public Account email(String email) {
    this.email = email;
    return this;
  }

  public String description() {
    return description;
  }

  public Account description(String description) {
    this.description = description;
    return this;
  }

  public String passwordHash() {
    return passwordHash;
  }

  public Account passwordHash(String passwordHash) {
    this.passwordHash = passwordHash;
    return this;
  }

  public AccountStatus status() {
    return status;
  }

  public Account status(AccountStatus status) {
    this.status = status;
    return this;
  }

  public LocalDateTime createdAt() {
    return createdAt;
  }

  public Account createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime updatedAt() {
    return updatedAt;
  }

  public Account updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("username='" + username + "'")
        .add("email='" + email + "'")
        .add("description='" + description + "'")
        .add("passwordHash='" + passwordHash + "'")
        .add("status=" + status)
        .add("createdAt=" + createdAt)
        .add("updatedAt=" + updatedAt)
        .toString();
  }
}
