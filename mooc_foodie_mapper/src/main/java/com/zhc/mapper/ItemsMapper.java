package com.zhc.mapper;

import com.zhc.my.mapper.MyMapper;
import com.zhc.pojo.Items;
import com.zhc.pojo.vo.ItemCommentVO;
import com.zhc.pojo.vo.SearchItemsVO;
import com.zhc.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapper extends MyMapper<Items> {
    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

    public List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);

    public List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

    public List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List<String>list);


}