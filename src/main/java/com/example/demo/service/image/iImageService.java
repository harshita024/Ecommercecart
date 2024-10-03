package com.example.demo.service.image;

import com.example.demo.dto.imagedto;
import com.example.demo.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface iImageService {
    Image getImageById(long id);
    void deleteImageById(long id);
    List<imagedto> saveImages(List<MultipartFile> files , long productId);
    void updateImage(MultipartFile file ,long imageId);
}
