package com.tommy.fsd.service;

import com.tommy.fsd.entities.OrderMain;
import com.tommy.fsd.entities.ProductInOrder;
import com.tommy.fsd.entities.ProductInfo;
import com.tommy.fsd.enums.OrderStatusEnum;
import com.tommy.fsd.enums.ResultEnum;
import com.tommy.fsd.exception.MyException;
import com.tommy.fsd.repo.OrderRepo;
import com.tommy.fsd.repo.ProductInfoRepo;
import com.tommy.fsd.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ProductInfoRepo productInfoRepo;
    @Autowired
    ProductService productService;

    public Page<OrderMain> findAll(Pageable pageable) {
        return orderRepo.findAllByOrderByOrderStatusAscCreateTimeDesc(pageable);
    }

    public Page<OrderMain> findByStatus(Integer status, Pageable pageable) {
        return orderRepo.findAllByOrderStatusOrderByCreateTimeDesc(status, pageable);
    }

    public Page<OrderMain> findByBuyerEmail(String email, Pageable pageable) {
        return orderRepo.findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(email, pageable);
    }

    public Page<OrderMain> findByBuyerPhone(String phone, Pageable pageable) {
        return orderRepo.findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(phone, pageable);
    }

    public OrderMain findOne(Long orderId) {
        OrderMain orderMain = orderRepo.findByOrderId(orderId);
        if(orderMain == null) {
            throw new MyException(ResultEnum.ORDER_NOT_FOUND);
        }
        return orderMain;
    }

    @Transactional
    public OrderMain finish(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderRepo.save(orderMain);
        return orderRepo.findByOrderId(orderId);
    }

    @Transactional
    public OrderMain cancel(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderRepo.save(orderMain);

        // Restore Stock
        Iterable<ProductInOrder> products = orderMain.getProducts();
        for(ProductInOrder productInOrder : products) {
            ProductInfo productInfo = productInfoRepo.findByProductId(productInOrder.getProductId());
            if(productInfo != null) {
                productService.increaseStock(productInOrder.getProductId(), productInOrder.getCount());
            }
        }
        return orderRepo.findByOrderId(orderId);

    }
}
