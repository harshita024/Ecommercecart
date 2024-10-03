package com.example.demo.service.product;

import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.imagedto;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Image;
import com.example.demo.model.Product;
import com.example.demo.request.AddProductRequest;
import com.example.demo.request.ProductUpdateRequest;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements iProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    @Override
    public Product addProduct(AddProductRequest request) {
        // Check if the category is found in the db
        // if yes set it as the new product category
        //if no then save it as new category
        //then set as a new product category
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }
    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not Found"));
    }

    @Override
    public void deleteProductById(long id) {
           productRepository.findById(id)
                   .ifPresentOrElse(productRepository ::delete,
                           ()->{throw new ProductNotFoundException("Product not found");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateexistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product updateexistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(request.getCategory());
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name) ;
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<imagedto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, imagedto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
