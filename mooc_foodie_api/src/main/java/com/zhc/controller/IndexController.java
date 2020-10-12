package com.zhc.controller;

import com.zhc.enums.YesOrNo;
import com.zhc.pojo.Carousel;
import com.zhc.pojo.Category;
import com.zhc.pojo.vo.CategoryVO;
import com.zhc.pojo.vo.NewItemsVO;
import com.zhc.service.CarouselService;
import com.zhc.service.CatService;
import com.zhc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {

        List<Carousel> carouselList = carouselService.queryAll(YesOrNo.YES.type);
        return IMOOCJSONResult.ok(carouselList);
    }

    @ApiOperation(value = "获取一级分类", notes = "获取一级分类")
    @GetMapping("/cats")
    public IMOOCJSONResult cat(){
        List<Category> categoryList = catService.queryAllCat();
        return IMOOCJSONResult.ok(categoryList);
    }

    @ApiOperation(value = "获取二三级分类", notes = "获取二三级分类")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "上级分类Id", required = true )
            @PathVariable("rootCatId") Integer rootCatId){
        if (rootCatId == null)
            return IMOOCJSONResult.errorMsg("分类不存在");
        List<CategoryVO> categoryList = catService.querySubCategory(rootCatId);
        return IMOOCJSONResult.ok(categoryList);
    }

    @ApiOperation(value = "查询一级分类下最新的6个商品", notes = "查询一级分类下最新的6个商品")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类Id", required = true )
            @PathVariable("rootCatId") Integer rootCatId){
        if (rootCatId == null)
            return IMOOCJSONResult.errorMsg("分类不存在");
        List<NewItemsVO> newItemsVOList = catService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(newItemsVOList);
    }
}
