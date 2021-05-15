package com.hundred.percent.capstone.Invoicify.address.repository;

import com.hundred.percent.capstone.Invoicify.address.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
