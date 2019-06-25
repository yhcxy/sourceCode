package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.Menu;
import com.midea.isc.admin.param.MenuParam;
import com.midea.isc.admin.param.ResourceRoleParam;
import com.midea.isc.admin.vo.MenuParamVo;
import com.midea.isc.admin.vo.MenuVo;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu, MenuParam> {
    // 多语言操作

    int insertTL(Menu model);

    int updateTL(Menu model);

    public int updateFieldsTL(Menu model);

    int copySourceLang(Menu model);

    /**
     * 删除app对应的menu
     * @param app
     * @return
     */
    int clearMenu(String app);

    /**
     * 根据app对应的menuId
     * @param app
     * @return
     */
    int selectRootMenuId(String app);

    MenuVo selectMenu(MenuParam param);

    List<MenuVo> selectMenuCascade(MenuParam param);

    int updateMenu(MenuParam param);

    String selectMenuCloneCount(String app);

    List<MenuVo> selectAllWebHeaderMenu(MenuParam param);

    List<Menu> listTop();

    List<MenuVo> listWithResource(ResourceRoleParam param);

    List<MenuVo> listWithResourceByUserId(MenuParamVo param);

    List<MenuVo> listWithOperation(MenuParamVo param);

    void displayed(List<MenuParam> params);
}
