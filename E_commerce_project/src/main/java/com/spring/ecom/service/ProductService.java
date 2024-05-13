package com.spring.ecom.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.ecom.entities.Cart;
import com.spring.ecom.entities.Category;
import com.spring.ecom.entities.Product;
import com.spring.ecom.exceptions.APIException;
import com.spring.ecom.exceptions.ResourceNotFoundException;
import com.spring.ecom.repository.CartRepo;
import com.spring.ecom.repository.CategoryRepo;
import com.spring.ecom.repository.ProductRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class  ProductService {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private CartService cartService;

	@Autowired
	private FileService fileService;

	

	@Value("${project.image}")
	private String path;


	public Product addProduct(Long categoryId, Product product) {

		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		boolean isProductNotPresent = true;

		List<Product> products = category.getProducts();

		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getProductName().equals(product.getProductName())
					&& products.get(i).getDescription().equals(product.getDescription())) {

				isProductNotPresent = false;
				break;
			}
		}

		if (isProductNotPresent) {
			product.setImage("default.png");

			product.setCategory(category);

			double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
			product.setSpecialPrice(specialPrice);

			Product savedProduct = productRepo.save(product);

			return savedProduct;
		} else {
			throw new APIException("Product already exists !!!");
		}
	}

	/*
	 * public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize,
	 * String sortBy, String sortOrder) {
	 * 
	 * Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
	 * Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	 * 
	 * Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
	 * 
	 * Page<Product> pageProducts = productRepo.findAll(pageDetails);
	 * 
	 * List<Product> products = pageProducts.getContent();
	 * 
	 * List<ProductDTO> productDTOs = products.stream().map(product ->
	 * modelMapper.map(product, ProductDTO.class)) .collect(Collectors.toList());
	 * 
	 * ProductResponse productResponse = new ProductResponse();
	 * 
	 * productResponse.setContent(productDTOs);
	 * productResponse.setPageNumber(pageProducts.getNumber());
	 * productResponse.setPageSize(pageProducts.getSize());
	 * productResponse.setTotalElements(pageProducts.getTotalElements());
	 * productResponse.setTotalPages(pageProducts.getTotalPages());
	 * productResponse.setLastPage(pageProducts.isLast());
	 * 
	 * return productResponse; }
	 */
	/*
	 * @Override public ProductResponse searchByCategory(Long categoryId, Integer
	 * pageNumber, Integer pageSize, String sortBy, String sortOrder) {
	 * 
	 * Category category = categoryRepo.findById(categoryId) .orElseThrow(() -> new
	 * ResourceNotFoundException("Category", "categoryId", categoryId));
	 * 
	 * Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
	 * Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	 * 
	 * Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
	 * 
	 * Page<Product> pageProducts = productRepo.findAll(pageDetails);
	 * 
	 * List<Product> products = pageProducts.getContent();
	 * 
	 * if (products.size() == 0) { throw new APIException(category.getCategoryName()
	 * + " category doesn't contain any products !!!"); }
	 * 
	 * List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p,
	 * ProductDTO.class)) .collect(Collectors.toList());
	 * 
	 * ProductResponse productResponse = new ProductResponse();
	 * 
	 * productResponse.setContent(productDTOs);
	 * productResponse.setPageNumber(pageProducts.getNumber());
	 * productResponse.setPageSize(pageProducts.getSize());
	 * productResponse.setTotalElements(pageProducts.getTotalElements());
	 * productResponse.setTotalPages(pageProducts.getTotalPages());
	 * productResponse.setLastPage(pageProducts.isLast());
	 * 
	 * return productResponse; }
	 */
	/*
	 * @Override public ProductResponse searchProductByKeyword(String keyword,
	 * Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) { Sort
	 * sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
	 * Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	 * 
	 * Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
	 * 
	 * Page<Product> pageProducts = productRepo.findByProductNameLike(keyword,
	 * pageDetails);
	 * 
	 * List<Product> products = pageProducts.getContent();
	 * 
	 * if (products.size() == 0) { throw new
	 * APIException("Products not found with keyword: " + keyword); }
	 * 
	 * List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p,
	 * ProductDTO.class)) .collect(Collectors.toList());
	 * 
	 * ProductResponse productResponse = new ProductResponse();
	 * 
	 * productResponse.setContent(productDTOs);
	 * productResponse.setPageNumber(pageProducts.getNumber());
	 * productResponse.setPageSize(pageProducts.getSize());
	 * productResponse.setTotalElements(pageProducts.getTotalElements());
	 * productResponse.setTotalPages(pageProducts.getTotalPages());
	 * productResponse.setLastPage(pageProducts.isLast());
	 * 
	 * return productResponse; }
	 */


	public Product updateProduct(Long productId, Product product) {
		Product productFromDB = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (productFromDB == null) {
			throw new APIException("Product not found with productId: " + productId);
		}

		product.setImage(productFromDB.getImage());
		product.setProductId(productId);
		product.setCategory(productFromDB.getCategory());

		double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
		product.setSpecialPrice(specialPrice);

		Product savedProduct = productRepo.save(product);

		List<Cart> carts = cartRepo.findCartsByProductId(productId);
        return savedProduct;
	}

	public Product updateProductImage(Long productId, MultipartFile image) throws IOException {
		Product productFromDB = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (productFromDB == null) {
			throw new APIException("Product not found with productId: " + productId);
		}
		
		String fileName = fileService.uploadImage(path, image);
		
		productFromDB.setImage(fileName);
		
		Product updatedProduct = productRepo.save(productFromDB);
		
		return updatedProduct;
	}
	
	
	public String deleteProduct(Long productId) {

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		List<Cart> carts = cartRepo.findCartsByProductId(productId);

		carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

		productRepo.delete(product);

		return "Product with productId: " + productId + " deleted successfully !!!";
	}

}
