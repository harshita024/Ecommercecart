package com.example.demo.Controller;

import com.example.demo.dto.imagedto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Image;
import com.example.demo.response.apiResponse;
import com.example.demo.service.image.iImageService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class imageController {
    private final iImageService imageService;
    @PostMapping("/uploads")
    public ResponseEntity<apiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam long productId) {
        try {
            List<imagedto> images = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new apiResponse("Upload Success", images));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new apiResponse("Upload failed", e.getMessage()));
        }
    }
    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return  ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
                .body((Resource) resource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<apiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new apiResponse("Update success!", null));
            }
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new apiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new apiResponse("Update failed!", INTERNAL_SERVER_ERROR));
    }


    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<apiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null) {
                imageService.deleteImageById( imageId);
                return ResponseEntity.ok(new apiResponse("Delete success!", null));
            }
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new apiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new apiResponse("Delete failed!", INTERNAL_SERVER_ERROR));
    }
}