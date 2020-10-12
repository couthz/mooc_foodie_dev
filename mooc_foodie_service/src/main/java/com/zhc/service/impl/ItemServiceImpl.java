package com.zhc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhc.enums.CommentLevel;
import com.zhc.enums.YesOrNo;
import com.zhc.mapper.*;
import com.zhc.pojo.*;
import com.zhc.pojo.vo.CommentLevelCountsVO;
import com.zhc.pojo.vo.ItemCommentVO;
import com.zhc.pojo.vo.SearchItemsVO;
import com.zhc.pojo.vo.ShopcartVO;
import com.zhc.service.ItemService;
import com.zhc.utils.DesensitizationUtil;
import com.zhc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

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
    Integer commentCounts(String itemId, Integer commentLevel) {
        /*构建条件,不用Example*/
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        condition.setCommentLevel(commentLevel);
        return itemsCommentsMapper.selectCount(condition);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId,
                                              Integer level,
                                              Integer page,
                                              Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);

        // mybatis-pagehelper

        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);

        List<ItemCommentVO> list = itemsMapper.queryItemComments(map);
        for (ItemCommentVO vo : list) {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }

        return setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort,
                                       Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keywords);
        map.put("sort", sort);

        // mybatis-pagehelper

        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);

        List<SearchItemsVO> list = itemsMapper.searchItems(map);

        return setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort,
                                                 Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);

        // mybatis-pagehelper

        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);

        List<SearchItemsVO> list = itemsMapper.searchItemsByThirdCat(map);

        return setterPagedGrid(list, page);
    }

    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String ids[] = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList, ids);
        return itemsMapper.queryItemsBySpecIds(specIdsList);
    }

    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String itemSpecId, int buyCounts) {
        //库存是公共资源，并发问题

        int result = itemsMapper.decreaseItemSpecStock(itemSpecId, buyCounts);
        if(result != 1) {
            throw new RuntimeException("订单创建失败: 库存不足!");
        }
    }


    private PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
