package com.usian.service;

import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemCatExample;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<TbItemCat> selectItemCategoryByParentId(Long id){
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        criteria.andStatusEqualTo(1);
       return tbItemCatMapper.selectByExample(example);

    }

    @Override
    public CatResult selectItemCategoryAll() {
        CatResult catResult = new CatResult();
        catResult.setData(getCatList(0L));

        return catResult;
    }

    private List<?> getCatList(Long parentId){
        //创建查询条件
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> itemCatList = tbItemCatMapper.selectByExample(tbItemCatExample);
        //创建数组 存入查询后的数据
        List resultList = new ArrayList();
        //控制商品分类18层
        int count=0;
        for (int i = 0; i < itemCatList.size(); i++) {
            TbItemCat tbItemCat =  itemCatList.get(i);
                if (tbItemCat.getIsParent()){
                    CatNode catNode = new CatNode();
                    catNode.setName(tbItemCat.getName());
                    catNode.setItem(getCatList(tbItemCat.getId()));
                    resultList.add(catNode);
                    count++;
                    if (count == 18){
                        break;
                    }
                }else{
                    resultList.add(tbItemCat.getName());
                }
        }
        return resultList;

                
    }


}
