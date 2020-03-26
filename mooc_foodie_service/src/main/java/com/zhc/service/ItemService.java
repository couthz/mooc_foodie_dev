package com.zhc.service;

import com.zhc.pojo.*;
import com.zhc.pojo.vo.CategoryVO;
import com.zhc.pojo.vo.CommentLevelCountsVO;
import com.zhc.pojo.vo.NewItemsVO;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品id查询详情
     * @param id
     * @return
     */
    public Items queryItemById(String id);

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格列表
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询商品的评价等级数量
     * @param itemId
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

}
