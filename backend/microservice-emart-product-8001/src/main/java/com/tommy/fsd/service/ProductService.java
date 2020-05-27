package com.tommy.fsd.service;

import com.tommy.fsd.entities.ProductInfo;
import com.tommy.fsd.enums.ProductStatusEnum;
import com.tommy.fsd.enums.ResultEnum;
import com.tommy.fsd.exception.MyException;
import com.tommy.fsd.repo.ProductInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    @Autowired
    ProductInfoRepo productInfoRepo;

    @Autowired
    CategoryService categoryService;

    public ProductInfo findOne(String productId){
        ProductInfo productInfo = productInfoRepo.findByProductId(productId);
        return productInfo;
    }

    public Page<ProductInfo> findUpAll(Pageable pageable) {
        return productInfoRepo.findAllByProductStatusOrderByProductIdAsc(ProductStatusEnum.UP.getCode(),pageable);
    }

    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepo.findAllByOrderByProductId(pageable);
    }

    public Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable) {
        return productInfoRepo.findAllByCategoryTypeOrderByProductIdAsc(categoryType, pageable);
    }

    @Transactional
    public void increaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() + amount;
        productInfo.setProductStock(update);
        productInfoRepo.save(productInfo);
    }

    @Transactional
    public void decreaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() - amount;
        if(update <= 0) throw new MyException(ResultEnum.PRODUCT_NOT_ENOUGH );

        productInfo.setProductStock(update);
        productInfoRepo.save(productInfo);
    }

    @Transactional
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == ProductStatusEnum.DOWN.getCode()) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //update
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return productInfoRepo.save(productInfo);
    }

    @Transactional
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == ProductStatusEnum.UP.getCode()) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //update
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return productInfoRepo.save(productInfo);
    }

    public ProductInfo update(ProductInfo productInfo) {

        // if null throw exception
        categoryService.findByCategoryType(productInfo.getCategoryType());
        if(productInfo.getProductStatus() > 1) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }


        return productInfoRepo.save(productInfo);
    }

    public ProductInfo save(ProductInfo productInfo) {
        return update(productInfo);
    }

    public void delete(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
        productInfoRepo.delete(productInfo);

    }

}
