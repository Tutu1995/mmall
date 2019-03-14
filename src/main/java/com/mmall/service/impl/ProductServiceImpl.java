package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by tino on 10/13/18.
 */

@Service("iProductService")
public class ProductServiceImpl implements IProductService{
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if(StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0) product.setMainImage(subImageArray[0]);
                if(product.getId() != null) {
                    int rowCount = productMapper.updateByPrimaryKey(product);
                    if (rowCount > 0) {
                        return ServerResponse.createBySuccess("Product updated successfully");
                    } else {
                        return ServerResponse.createByErrorMessage("Cannot updated product");
                    }
                }
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("Product added successfully");
                } else {
                    return ServerResponse.createByErrorMessage("Cannot add product");
                }
            }
        }
        return ServerResponse.createByErrorMessage("Incorrect index for adding or updating product");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if(productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0) {
            return ServerResponse.createBySuccess("Product status set successfully");
        }
        return ServerResponse.createByErrorMessage("Cannot set product status");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if(productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null) {
            return ServerResponse.createByErrorMessage("Product is deleted or no longer sold");
        }
        // vo -- value object
        // pojo --> vo
        // future: pojo --> bo(business object) --> vo (view object)
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setStock(product.getStock());

        // imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.taozihao.xyz/"));
        // parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setCategoryId(0);// set as root
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        // createTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        // updateTime
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    @Override
    // use mybatis page helper plugin
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        // startPage--start
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectList();
        // how to request sql
        List<ProductListVo> productListVosList = Lists.newArrayList();
        for (Product productItem : list) {
            productListVosList.add(assembleProductListVo(productItem));
        }
        // pageHelper ending
        PageInfo pageResult = new PageInfo(list);
        pageResult.setList(productListVosList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setName(product.getName());
        productListVo.setStatus(product.getStatus());
        productListVo.setPrice(product.getPrice());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.taozihao.xyz/"));
        return productListVo;
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if(StringUtils.isNotBlank(productName)) {
            // productName = new StringBuilder().append("%").append(productName).append("%").toString();
            productName = "%" + productName + "%";
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVosList = Lists.newArrayList();
        for (Product productItem : productList) {
            productListVosList.add(assembleProductListVo(productItem));
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVosList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if(productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null || product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("Product is deleted or no longer sold");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if(StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<>();
        if(categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)) {
                // this is not an error, should return a blank list
                PageHelper.startPage(pageNum, pageSize);
                List<Product> list = productMapper.selectList();
                List<ProductListVo> productListVosList = Lists.newArrayList();
                PageInfo pageResult = new PageInfo(productListVosList);
                //pageResult.setList(productListVosList); don't need set if it is a blank list
                return ServerResponse.createBySuccess(pageResult);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)) {
            keyword = '%' + keyword + '%';
        }
        PageHelper.startPage(pageNum, pageSize);
        // order by price
        //PageHelper.orderBy("object OrderMethod")
        if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            String[] orderByArray = orderBy.split("_");
            PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
        }
        List<Product> list = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null:keyword, categoryIdList.size() == 0 ? null:categoryIdList);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product listItem : list) {
            ProductListVo productListVo = assembleProductListVo(listItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
