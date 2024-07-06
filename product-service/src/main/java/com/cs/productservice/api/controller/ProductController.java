package com.cs.productservice.api.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.productservice.api.dto.ProductCreateDTO;
import com.cs.productservice.api.dto.ProductDTO;
import com.cs.productservice.api.mapper.ProductMapper;
import com.cs.productservice.entity.Product;
import com.cs.productservice.service.ProductService;
import com.cs.productservice.util.exception.ProductServiceException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/products")
public class ProductController {
	private final ProductService productService;
	private final ProductMapper productMapper;

	@GetMapping
	public ResponseEntity<Page<ProductDTO>> get(@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		log.info("Product request, page: {}, size: {}", page, size);

		Page<Product> data = productService.findAll(PageRequest.of(page.orElse(0), size.orElse(10)));

		return ResponseEntity.ok(data.map(p -> productMapper.mapToProductDTO(p)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> getById(@PathVariable("id") long id) {
		log.info("Product requested, id: {}", id);

		Product product = productService.findById(id);

		if (product == null) {
			log.error("Could not found product with given id: {}", id);
			throw new ProductServiceException("Can not found product with given id!");
		}

		log.info("Requested product is found, id: {}", id);
		return ResponseEntity.ok(productMapper.mapToProductDTO(product));
	}

	@PostMapping
	public ResponseEntity<ProductDTO> post(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
		log.info("A new product is about to be inserted");

		Product product = productService.save(productMapper.mapToProduct(productCreateDTO));

		log.info("Product is inserted successfully, id: {}", product.getId());

		return ResponseEntity.ok(productMapper.mapToProductDTO(product));
	}

	@PutMapping
	public ResponseEntity<ProductDTO> put(@Valid @RequestBody ProductDTO productDTO) {
		log.info("A product is about to be updated, id: {}", productDTO.id());

		Product product = productMapper.mapToProduct(productDTO);

		if (product == null) {
			log.error("Could not found product with given id: {}", productDTO.id());
			throw new ProductServiceException("Can not found product with given id!");
		}

		productService.save(product);

		log.info("Product is updated successfully, id: {}", product.getId());

		return ResponseEntity.ok(productMapper.mapToProductDTO(product));
	}
}
