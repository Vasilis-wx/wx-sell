package com.wx.control.buyer;

import com.wx.VO.ProductInfoVO;
import com.wx.VO.ProductVO;
import com.wx.VO.ResultVO;
import com.wx.model.ProductCategory;
import com.wx.model.ProductInfo;
import com.wx.service.ProductCategoryService;
import com.wx.service.ProductInfoService;
import com.wx.util.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wx
 * 2018/8/17
 */
//@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductControl {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 获取所有商品
     * @return
     */
    @GetMapping("/list")
//    @Cacheable(cacheNames = "buyer",key = "1234")
    @Cacheable(cacheNames = "buyer",key = "#buyerId",condition = "#buyerId.length() > 3"//conditon 包括的条件
            ,unless = "#buyerId.length() > 26")//不包含条件
    //第一次执行方法会进去，将数据保存进redis；
    //以后执行时会判断redis是否有数据，有：取redis中的数据；没有：执行方法，重新获取数据
    public ResultVO list(@RequestParam(value ="buyerId",defaultValue = "0") String buyerId){

        /***   1、获取所有的商品   ***/
        List<ProductInfo> productInfoList = productInfoService.findUpAll();

        /***   2、获取所有的类目   ***/
        //获取所有商品的类型
        //传统方法
//        List<Integer> list = new ArrayList<Integer>();
//        for(int i=0;i<productInfoList.size();i++){
//            list.add(productInfoList.get(i).getCategoryType());
//        }
        //jdk 1.8 lambda
        List<Integer> categoryTypelist = productInfoList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());

        List<ProductCategory> productCategoryList = productCategoryService.findByCategoryTypeIn(categoryTypelist);


        /***  3、拼接数据  ****/
        List<ProductVO> productVOList = new ArrayList<>();
        for(ProductCategory productCategory : productCategoryList){
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for(ProductInfo productInfo : productInfoList){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }

            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }


        return ResultVOUtil.succsee(productVOList);
    }
}
