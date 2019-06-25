package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.MenuBiz;
import com.midea.isc.admin.model.Menu;
import com.midea.isc.admin.param.MenuParam;
import com.midea.isc.admin.vo.MenuNode;
import com.midea.isc.admin.vo.MenuParamVo;
import com.midea.isc.admin.vo.MenuVo;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/menu")
public class MenuController extends BasicController<MenuBiz, Menu, MenuParam> {

    /**
     * 验证名字是否重复(没报错就是没有重复)
     * @param param
     * @throws Exception
     */
    @PostMapping("/validate")
    public void validate(@RequestBody MenuParam param) throws Exception {
        baseBiz.validate(param);
    }

    @RequestMapping(value = "/listTop")
    public List<Menu> listTop() {
        return baseBiz.listTop();
    }

    @PostMapping("/insertWithOperation")
    public void insertWithOperation(@RequestBody MenuParamVo param) {
        baseBiz.insertWithOperation(param);
    }

    @PostMapping("/updateWithOperation")
    public void updateWithOperation(@RequestBody MenuParamVo param) {
        baseBiz.updateWithOperation(param);
    }

    @PostMapping("/tree")
    public List<MenuNode> tree(@RequestBody MenuParamVo param) {
        return baseBiz.tree(param);
    }

    @PostMapping("/getTreeByRole")
    public MenuNode getTreeByRole(@RequestBody MenuParamVo param) {
        return baseBiz.treeByRole(param);
    }

    @PostMapping("/treeWithResource")
    public List<MenuNode> treeWithResource(@RequestBody MenuParamVo param) {
        return baseBiz.treeWithResource(param);
    }

    @PostMapping("/listWithOperation")
    public List<MenuVo> listWithOperation(@RequestBody MenuParamVo param) {
        return baseBiz.listWithOperation(param);
    }

    /*保存菜单的显示*/
    @PostMapping("/displayed")
    public void displayed(@RequestBody MenuParamVo param) {
        baseBiz.displayed(param);
    }
}