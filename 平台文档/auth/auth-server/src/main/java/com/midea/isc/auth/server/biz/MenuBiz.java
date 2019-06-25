package com.midea.isc.auth.server.biz;

import com.midea.isc.auth.server.mapper.MenuMapper;
import com.midea.isc.auth.server.model.Menu;
import com.midea.isc.auth.server.param.MenuParam;
import com.midea.isc.auth.server.vo.MenuNode;
import com.midea.isc.auth.server.vo.MenuParamVo;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuBiz extends BaseBiz<MenuMapper, Menu, MenuParam> {

    @DS("master")
    @Override
    @Transactional(value = "dataSourceTransactionManager", readOnly = false, rollbackFor = Exception.class)
    public int insert(Menu model) {
        this.setWho(model);
        int count = mapper.insert(model);
        if (count > 0) {
            mapper.insertTL(model);
        }

        return count;
    }

    @DS("master")
    @Override
    @Transactional(value = "dataSourceTransactionManager", readOnly = false, rollbackFor = Exception.class)
    public int update(Menu model) {
        this.setWhoForUpdateInfo(model);
        int count = mapper.update(model);
        int countTL = mapper.updateTL(model);
        if (countTL == 0) {
            mapper.copySourceLang(model);
            countTL = mapper.updateTL(model);
        }

        return count;
    }

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int updateFields(Menu model) {
        this.setWhoForUpdateInfo(model);
        int count = mapper.updateFields(model);
        int countTL = mapper.updateFieldsTL(model);
        if (countTL == 0) {
            mapper.copySourceLang(model);
            countTL = mapper.updateFieldsTL(model);
        }

        return count;
    }

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int updateByOtsLock(Menu model) throws IscException {
        this.setWhoForUpdateInfo(model);
        int count = mapper.updateByOtsLock(model);
        if (count == 0) {
            throw new IscException("ISC-929");
        }
        int countTL = mapper.updateTL(model);
        if (countTL == 0) {
            mapper.copySourceLang(model);
            countTL = mapper.updateTL(model);
        }

        return count;
    }

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int updateFieldsByOtsLock(Menu model) throws IscException {
        this.setWhoForUpdateInfo(model);
        int count = mapper.updateFieldsByOtsLock(model);
        if (count == 0) {
            throw new IscException("ISC-929");
        }
        int countTL = mapper.updateFieldsTL(model);
        if (countTL == 0) {
            mapper.copySourceLang(model);
            countTL = mapper.updateFieldsTL(model);
        }

        return count;
    }

    @DS("slave")
    public List<MenuNode> tree(MenuParamVo param) {
        List<MenuNode> vos = mapper.listMenus(param);
        List<MenuNode> ret = new ArrayList<>();
        Map<Integer, MenuNode> voMap = new HashMap<>();

        for (MenuNode vo : vos) {
            int level = vo.getLevel();
            voMap.put(vo.getMenuId(), vo);

            // 只返回大于0的
            if (param.getShowHead() == 0 && 0 == level) {
                continue;
            }

            voMap.put(vo.getMenuId(), vo);
            if (param.getShowHead() == 1 && 0 == level) {
                ret.add(vo);
                continue;
            }
            else if (param.getShowHead() == 0 && level == 1) {
                ret.add(vo);
                continue;
            }

            Integer parentId = vo.getParentId();
            if (null != parentId)
                voMap.get(parentId).getSubMenus().add(vo);
        }

        return ret;
    }
}
