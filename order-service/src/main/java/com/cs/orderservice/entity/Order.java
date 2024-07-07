package com.cs.orderservice.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String userId;

	@Enumerated(EnumType.ORDINAL)
	@Column
	private OrderStatus status;

	@Column
	private double totalPrice;

	@CreatedDate
	@Column(updatable = false)
	private Date createdDate;

	@LastModifiedDate
	@Column
	private Date lastModifiedDate;

	@Version
    private int version;

	@Transient
	private List<OrderItem> items;
}
