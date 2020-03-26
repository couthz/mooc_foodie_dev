package com.zhc.mapper;

import com.zhc.my.mapper.MyMapper;
import com.zhc.pojo.Category;
import com.zhc.pojo.vo.CategoryVO;
import com.zhc.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapper extends MyMapper<Category> {
     public List<CategoryVO> querySubCategory(Integer rootCatId);

     public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}