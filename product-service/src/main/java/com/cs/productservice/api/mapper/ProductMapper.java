package com.cs.productservice.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import com.cs.productservice.api.dto.ProductCreateDTO;
import com.cs.productservice.api.dto.ProductDTO;
import com.cs.productservice.entity.Product;
import com.cs.productservice.service.ProductService;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
	@Autowired
	private ProductService productService;
	
	public abstract ProductDTO mapToProductDTO(Product source);
	public abstract List<ProductDTO> mapToProductDTOList(List<Product> source);
	public abstract Product mapToProduct(ProductDTO source);
	public abstract Product mapToProduct(ProductCreateDTO source);

	@ObjectFactory
    public Product resolveProduct(@NonNull ProductDTO source, @TargetType Class<Product> targetType) {
        return productService.findById(source.id());
    }
}
