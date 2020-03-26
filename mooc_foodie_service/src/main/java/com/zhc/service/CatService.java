package com.zhc.service;

import com.zhc.pojo.Carousel;
import com.zhc.pojo.Category;
import com.zhc.pojo.vo.CategoryVO;
import com.zhc.pojo.vo.NewItemsVO;

import java.util.List;

public interface CatService {

    public List<Category> queryAllCat();

    public List<CategoryVO> querySubCategory(Integer rootCatId);

    /**
     * 查询六条新的一级分类商品推荐
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);

}
