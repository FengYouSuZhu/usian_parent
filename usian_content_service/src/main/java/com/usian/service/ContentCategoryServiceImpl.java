package com.usian.service;

import com.usian.mapper.TbContentCategoryMapper;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.NameList;

import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    /**
     * 根据当前节点 ID 查询子节点
     * @param id
     * @return
     */
    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long id) {
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        return list;
    }

    /**
     * 添加内容分类
     * @param tbContentCategory
     * @return
     */
    @Override
    public Integer insertContentCategory(TbContentCategory tbContentCategory) {
        //补齐数据
        Date date = new Date();
        tbContentCategory.setUpdated(date);
        tbContentCategory.setCreated(date);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        int insertSelective = tbContentCategoryMapper.insertSelective(tbContentCategory);

        //查询当前新节点的父节点
        TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
        System.out.println(contentCategory.getIsParent()+"///////////////////");
        //判读当前节点的父节点
        if(!contentCategory.getIsParent()){
            contentCategory.setIsParent(true);
            contentCategory.setUpdated(date);
            tbContentCategoryMapper.updateByPrimaryKey(contentCategory);
        }
        return insertSelective;
    }

    /**
     * 分类内容管理删除接口
     * @param categoryId
     * @return
     */
    @Override
    public Integer deleteContentCategoryById(Long categoryId) {
        //1.1查询是否为父节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(categoryId);
        //1.2父节点 不允许删除
        if (tbContentCategory.getIsParent()==true){
            return  0;
        }
        //1.3不是父节点  删除
        tbContentCategoryMapper.deleteByPrimaryKey(categoryId);
        //查询该节点的父节点下是否还有节点
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(tbContentCategory.getParentId());
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        //如果父类没有节点，就讲父类isParent状态设为false
        if (list.size() == 0){
            TbContentCategory contentCategory = new TbContentCategory();
            contentCategory.setId(tbContentCategory.getParentId());
            contentCategory.setIsParent(false);
            contentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        }
        return 200;
    }

    /**
     * 内容分类修改
     * @param tbContentCategory
     * @return
     */
    @Override
    public Integer updateContentCategory(TbContentCategory tbContentCategory) {
        tbContentCategory.setUpdated(new Date());
        int updateByPrimaryKeySelective = tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);


        return updateByPrimaryKeySelective;
    }
}
