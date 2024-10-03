package com.example.demo.service.product;

import com.example.demo.dto.ProductDto;
import com.example.demo.model.Product;
import com.example.demo.request.AddProductRequest;
import com.example.demo.request.ProductUpdateRequest;

import java.util.List;

public interface iProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(long id);
    void deleteProductById(long id);
    Product updateProduct(ProductUpdateRequest product, long productId );
    List<Product> getAllProducts();
    List <Product> getProductsByCategory(String category);
    List <Product> getProductsByBrand(String brand);
    List <Product> getProductsByCategoryAndBrand(String category, String brand);
    List <Product> getProductsByName(String name);
    List <Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
