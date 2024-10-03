package com.example.demo.service.image;

import com.example.demo.Repository.ImageRepository;
import com.example.demo.dto.imagedto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Image;
import com.example.demo.model.Product;
import com.example.demo.service.product.iProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ImageService implements iImageService {
    private final ImageRepository imageRepository;
    private final iProductService productService;
    @Override
    public Image getImageById(long id) {
        return  imageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(long id) {
         imageRepository.findById(id).ifPresentOrElse(imageRepository::delete ,()->{
             throw new ResourceNotFoundException("Image not found");
         });
    }

    @Override
    public List<imagedto> saveImages(List<MultipartFile> files, long productId) {
        Product product = productService.getProductById(productId);
        List<imagedto> savedImagedto = new ArrayList<>();
        for (MultipartFile file : files) {
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download";
                String downloadUrl = buildDownloadUrl+ image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage=imageRepository.save(image);
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                imagedto ImageDto = new imagedto();
                ImageDto.setImageId(savedImage.getId());
                ImageDto.setImageName(savedImage.getFileName());
                ImageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImagedto.add(ImageDto);
            } catch(IOException | SQLException e){
                 throw new RuntimeException(e.getMessage());
            }
        }
        return savedImagedto ;
    }

    @Override
    public void updateImage(MultipartFile file, long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
