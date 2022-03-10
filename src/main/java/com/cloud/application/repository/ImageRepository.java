package com.cloud.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloud.application.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	Optional<Image> findByUserId(String userId);
}
