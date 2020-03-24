package com.zhc.service.impl;

import com.zhc.mapper.CategoryMapper;
import com.zhc.pojo.Category;
import com.zhc.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class CatServiceImpl implements CatService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryAllCat(Integer fatherId, Integer type){
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", type);
        criteria.andEqualTo("fatherId", fatherId);

        List<Category> result = categoryMapper.selectByExample(example);
        return result;
    }
}
