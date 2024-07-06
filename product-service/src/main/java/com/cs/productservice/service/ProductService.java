package com.cs.productservice.service;

import java.util.List;
import java.util.concurrent.Semaphore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cs.productservice.api.dto.ProductOrderItemDTO;
import com.cs.productservice.entity.Product;
import com.cs.productservice.repository.ProductRepository;
import com.cs.productservice.util.exception.ProductServiceException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;
	private Semaphore semaphore = new Semaphore(1);

	public Product save(Product entity) {
		return productRepository.save(entity);
	}

	public Page<Product> findAll(PageRequest pageRequest) {
		return productRepository.findAll(pageRequest);
	}

	public Product findById(long id) {
		return productRepository.findById(id).orElse(null);
	}

	public List<Product> decrementStock(List<ProductOrderItemDTO> items) {
		try {
			semaphore.acquire();

			List<Product> products = productRepository.findAllById(items.stream().map(item -> item.productId()).toList());

			if (products.size() != items.size()) {
				log.error("Can not found some of requested products");
				throw new ProductServiceException("Can not found some of requested products");
			}

			products.forEach(product -> {
				long quantity = items.stream()
						.filter(item -> item.productId() == product.getId())
						.map(item -> item.quantity()).findFirst().get();

				if (product.getStockCount() < quantity) {
					log.error("Insufficient stock count for product: {}", product.getId());
					throw new ProductServiceException("Insufficient stock count for product: " + product.getId());
				}

				product.setStockCount(product.getStockCount() - quantity);
			});

			return productRepository.saveAll(products);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			semaphore.release();
		}
	}

	public void incrementStock(List<ProductOrderItemDTO> items) {
		try {
			semaphore.acquire();

			List<Product> products = productRepository.findAllById(items.stream().map(item -> item.productId()).toList());

			if (products.size() != items.size()) {
				log.warn("Can not found some of requested products");
			}

			products.forEach(product -> {
				long quantity = items.stream()
						.filter(item -> item.productId() == product.getId())
						.map(item -> item.quantity()).findFirst().get();

				product.setStockCount(product.getStockCount() + quantity);
			});

			productRepository.saveAll(products);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			semaphore.release();
		}
	}
}
