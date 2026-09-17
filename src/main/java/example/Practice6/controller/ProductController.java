package example.Practice6.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.Practice6.dto.ProductDto;
import example.Practice6.service.ProductService;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/products")
@CrossOrigin ( value = "http://localhost:5173")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getProducts() {
        return productService.findAll();
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto dto) {
        return productService.save(dto);
    }

    @PutMapping
    public boolean updateProduct(@RequestBody ProductDto dto) {
        return productService.update(dto);
    }

    @DeleteMapping
    public boolean deleteProduct(@RequestParam("bno") Long bno) {
        return productService.delete(bno);
    }
}