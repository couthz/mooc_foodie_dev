package com.zhc.service.impl;

import com.zhc.enums.CommentLevel;
import com.zhc.mapper.*;
import com.zhc.pojo.*;
import com.zhc.pojo.vo.CommentLevelCountsVO;
import com.zhc.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example itemsImgExample = new Example(ItemsImg.class);
        /*构建条件*/
        Example.Criteria itemsImgCriteria = itemsImgExample.createCriteria();
        itemsImgCriteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(itemsImgExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example itemsSpecExample = new Example(ItemsSpec.class);
        /*构建条件*/
        Example.Criteria itemsSpecCriteria = itemsSpecExample.createCriteria();
        itemsSpecCriteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(itemsSpecExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example itemsParamExample = new Example(ItemsParam.class);
        /*构建条件*/
        Example.Criteria itemsParamCriteria = itemsParamExample.createCriteria();
        itemsParamCriteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(itemsParamExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        CommentLevelCountsVO commentLevelCountsVO = new CommentLevelCountsVO();
        Integer goodCounts = commentCounts(itemId, CommentLevel.positive.type);
        Integer normalCounts = commentCounts(itemId, CommentLevel.middle.type);
        Integer badCounts = commentCounts(itemId, CommentLevel.negative.type);
        commentLevelCountsVO.setGoodCounts(goodCounts);
        commentLevelCountsVO.setNormalCounts(normalCounts);
        commentLevelCountsVO.setBadCounts(badCounts);
        commentLevelCountsVO.setTotalCounts(goodCounts+normalCounts+badCounts);
        return commentLevelCountsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    private Integer commentCounts(String itemId, Integer commentLevel) {
        /*构建条件,不用Example*/
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        condition.setCommentLevel(commentLevel);
        return itemsCommentsMapper.selectCount(condition);
    }


}
