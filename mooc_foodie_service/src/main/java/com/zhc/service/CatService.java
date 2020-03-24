package com.zhc.service;

import com.zhc.pojo.Carousel;
import com.zhc.pojo.Category;

import java.util.List;

public interface CatService {

    public List<Category> queryAllCat(Integer fatherId, Integer type);
}
