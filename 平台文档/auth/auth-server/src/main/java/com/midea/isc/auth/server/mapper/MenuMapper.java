package com.midea.isc.auth.server.mapper;

import com.midea.isc.auth.server.model.Menu;
import com.midea.isc.auth.server.param.MenuParam;
import com.midea.isc.auth.server.vo.MenuNode;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu, MenuParam> {

    // 多语言操作

    public int insertTL(Menu model);

    public int updateTL(Menu model);

    public int updateFieldsTL(Menu model);

    public int copySourceLang(Menu model);

    /**
     * 查询菜单
     * @param param
     * @return
     */
    public List<MenuNode> listMenus(MenuParam param);
}
