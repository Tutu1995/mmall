package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by tino on 10/12/18.
 */

@Service("iCategoryService")
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if(parentId == null || org.apache.commons.lang3.StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("Incorrect category index");
        }
        int resultCount = categoryMapper.checkDuplication(parentId, categoryName);
        if(resultCount > 0) {
            return ServerResponse.createByErrorMessage("Category exists");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0) {
            return ServerResponse.createBySuccess("Successfully add category");
        }
        return ServerResponse.createByErrorMessage("Cannot add category");
    }

    @Override
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId){
        if(categoryId == null || org.apache.commons.lang3.StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("Incorrect category index");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        // just update categoryId's categoryName
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0) {
            return ServerResponse.createBySuccess("Successfully update category");
        }
        return ServerResponse.createByErrorMessage("Cannot update category");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        // tell if the collection is null or the collection has no element
        if(CollectionUtils.isEmpty(categoryList)) {
            log.info("Cannot find children of this category");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        // guava
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    // need rewrite hashcode of Category to be used in set
    // recursion
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null) {
            categorySet.add(category);
        }
        // find children node, recursion has a condition to exit
        // if categoryList is null, jump out of for loop, return set
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
