package com.zhc.controller;

import com.zhc.enums.YesOrNo;
import com.zhc.pojo.*;
import com.zhc.pojo.vo.CategoryVO;
import com.zhc.pojo.vo.CommentLevelCountsVO;
import com.zhc.pojo.vo.ItemInfoVO;
import com.zhc.pojo.vo.NewItemsVO;
import com.zhc.service.CarouselService;
import com.zhc.service.CatService;
import com.zhc.service.ItemService;
import com.zhc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller springmvc用的比较多，做页面跳转
@Api(value ="商品接口",tags = {"商品信息展示的的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemService itemService;


    @ApiOperation(value = "查询商品详情", notes = "查询商品详情")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult info(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemParams(itemsParam);
        itemInfoVO.setItemSpecList(itemSpecList);
        return IMOOCJSONResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价等级数量", notes = "查询商品评价等级数量")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        CommentLevelCountsVO commentLevelCountsVO= itemService.queryCommentCounts(itemId);
        return IMOOCJSONResult.ok(commentLevelCountsVO);
    }


}
