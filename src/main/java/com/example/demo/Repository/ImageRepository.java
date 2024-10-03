package com.example.demo.Repository;

import com.example.demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
     List<Image> findByProductId(long id) ;

}
