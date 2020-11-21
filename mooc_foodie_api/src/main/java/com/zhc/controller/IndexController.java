package com.zhc.controller;

import com.zhc.enums.YesOrNo;
import com.zhc.pojo.Carousel;
import com.zhc.pojo.Category;
import com.zhc.pojo.vo.CategoryVO;
import com.zhc.pojo.vo.NewItemsVO;
import com.zhc.service.CarouselService;
import com.zhc.service.CatService;
import com.zhc.utils.IMOOCJSONResult;
import com.zhc.utils.JsonUtils;
import com.zhc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

//@Controller springmvc用的比较多，做页面跳转
@Api(value ="首页",tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CatService catService;
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {

        List<Carousel> carouselList;
        String carouselStr = redisOperator.get("carousel");
        //cache aside读写模式中的读模式
        //缓存无数据，穿透
        if (StringUtils.isBlank(carouselStr)) {
            carouselList = carouselService.queryAll(YesOrNo.YES.type);
            //同时写缓存
            redisOperator.set("carousel", JsonUtils.objectToJson(carouselList));
        }
        //缓存有数据
        else {
            carouselList = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }
        return IMOOCJSONResult.ok(carouselList);
    }

    @ApiOperation(value = "获取一级分类", notes = "获取一级分类")
    @GetMapping("/cats")
    public IMOOCJSONResult cat(){
        List<Category> categoryList;
        String categoryStr = redisOperator.get("category");
        if (StringUtils.isBlank(categoryStr)) {
            categoryList = catService.queryAllCat();
            //同时写缓存
            redisOperator.set("category", JsonUtils.objectToJson(categoryList));
        }
        else {
            categoryList = JsonUtils.jsonToList(categoryStr, Category.class);
        }

        return IMOOCJSONResult.ok(categoryList);
    }

    @ApiOperation(value = "获取二三级分类", notes = "获取二三级分类")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "上级分类Id", required = true )
            @PathVariable("rootCatId") Integer rootCatId){
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> subCategoryList;
        String subCategoryStr = redisOperator.get("subCat:" + rootCatId);
        if (StringUtils.isBlank(subCategoryStr)) {
            subCategoryList = catService.querySubCategory(rootCatId);
            //同时写缓存
            redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(subCategoryList));
        }
        else {
            subCategoryList = JsonUtils.jsonToList(subCategoryStr, CategoryVO.class);
        }
        return IMOOCJSONResult.ok(subCategoryList);
    }

    /**
     * 1. 后台运营系统，一旦广告（轮播图）发生更改，就可以删除缓存，然后重置
     * 2. 定时重置，比如每天凌晨三点重置,删除重置
     * 3. 每个轮播图都有可能是一个广告，每个广告都会有一个过期时间，过期了，再重置
     */

    @ApiOperation(value = "查询一级分类下最新的6个商品", notes = "查询一级分类下最新的6个商品")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类Id", required = true )
            @PathVariable("rootCatId") Integer rootCatId){
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> newItemsVOList = catService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(newItemsVOList);
    }
}
