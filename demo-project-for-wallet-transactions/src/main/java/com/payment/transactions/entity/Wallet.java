package com.payment.transactions.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "wallet")
@Getter
@Setter
public class Wallet {
    @Id
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "balance")
    private long balance;

    @Version
    @Column(name = "version")
    private long version;
}
