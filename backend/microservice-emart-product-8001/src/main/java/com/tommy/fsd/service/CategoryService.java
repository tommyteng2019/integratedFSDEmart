package com.tommy.fsd.service;

import com.tommy.fsd.entities.ProductCategory;
import com.tommy.fsd.enums.ResultEnum;
import com.tommy.fsd.exception.MyException;
import com.tommy.fsd.repo.ProductCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    ProductCategoryRepo productCategoryRepo;

    public List<ProductCategory> findAll() {
        List<ProductCategory> res = productCategoryRepo.findAllByOrderByCategoryType();
        return res;
    }

    public ProductCategory findByCategoryType(Integer categoryType) {
        ProductCategory res = productCategoryRepo.findByCategoryType(categoryType);
        if(res == null) throw new MyException(ResultEnum.CATEGORY_NOT_FOUND);
        return res;
    }

    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        List<ProductCategory> res = productCategoryRepo.findByCategoryTypeInOrderByCategoryTypeAsc(categoryTypeList);
        //res.sort(Comparator.comparing(ProductCategory::getCategoryType));
        return res;
    }

    @Transactional
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepo.save(productCategory);
    }

}
