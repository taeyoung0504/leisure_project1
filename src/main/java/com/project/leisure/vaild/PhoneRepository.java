package com.project.leisure.vaild;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Integer> {

	Phone findByPhoneNumber(String phoneNumber);

}
