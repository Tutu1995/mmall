package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface  ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> selectByNameAndProductId(@Param(value = "productName") String productName, @Param(value = "productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param(value = "productName") String productName, @Param("categoryIdList") List<Integer> categoryIdList);

    int checkProductNumber();

    // must return integer because Integer can be null. Sometimes the item will be deleted, and return a null
    Integer selectStockByProductId(Integer id);
}