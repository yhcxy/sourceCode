package com.midea.isc.admin.biz;

import com.alibaba.fastjson.JSON;
import com.midea.isc.admin.mapper.MenuMapper;
import com.midea.isc.admin.model.Menu;
import com.midea.isc.admin.model.Operation;
import com.midea.isc.admin.model.OprPath;
import com.midea.isc.admin.param.MenuParam;
import com.midea.isc.admin.param.OperationParam;
import com.midea.isc.admin.param.OprPathParam;
import com.midea.isc.admin.param.ResourceRoleParam;
import com.midea.isc.admin.vo.*;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.CommUtil;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MenuBiz extends BaseBiz<MenuMapper, Menu, MenuParam> {

    @Autowired
    private OperationBiz operationBiz;

    @Autowired
    private OprPathBiz oprPathBiz;

    @DS("master")
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
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
    @Transactional(readOnly = false, rollbackFor = Exception.class)
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

    /**
     * 新增菜单(新增菜单还有逻辑应该用这个)
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer insertMenu(MenuParamVo param) {
        int count = 0;

        if (param.getParentId() != null) {
            MenuParam dto = new MenuParam();
            dto.setMenuId(param.getParentId());
            Menu parent = mapper.selectOne(dto);
            String parentPath = this.nodePath(parent.getParentPath(), parent.getMenuId());
            int level = parentPath == null ? 0 : parentPath.length() - parentPath.replace(".", "").length() + 1;

            param.setParentPath(parentPath);
            param.setLevel(Integer.valueOf(level));
        }
        this.setWho(param);
        count += mapper.insert(param);
        if (count > 0) {
            if (param.getLocalName() == null) {
                param.setLocalName(param.getName());
            }
            mapper.insertTL(param);

        }

        if (param.getOperationParams() != null) {
            operationBiz.insertOperation(param);
        }

        return count;
    }

    /**
     * 创建菜单的path
     *
     * @param parentPath
     * @param menuId
     * @return
     */
    private String nodePath(String parentPath, int menuId) {
        return (parentPath == null ? "" : parentPath) + (CommUtil.isEmpty(parentPath) ? "" : ".")
                + String.valueOf(menuId);
    }

    /**
     * 删除app对应的Menu
     *
     * @param app
     * @return
     */
    public int clearMenu(String app) {
        return mapper.clearMenu(app);
    }

    @DS("migration")
    public int selectRootMenuIdMigration(String app) {
        return mapper.selectRootMenuId(app);
    }

    @DS("slave")
    public int selectRootMenuId(String app) {
        return mapper.selectRootMenuId(app);
    }

    @Transactional(rollbackFor = Exception.class)
    public String migration(MenuParamVo param) throws Exception {
        String msg = "Follow menu(s) has been ignored due to operation duplication:";
        String log = "Process Log:";
        MenuParamVo mp = new MenuParamVo();
        mp.setProfile(param.getProfile());
        mp.setMenuId(param.getMenuId());
        MenuVo src = mapper.selectMenu(mp);

        mp.setPath((src.getLevel() == 0 ? "" : src.getParentPath() + ".") + src.getMenuId());
        List<MenuVo> menus = mapper.selectMenuCascade(mp);

        HashMap<Integer, Menu> map = new HashMap<Integer, Menu>();
        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            log += "\nbegin migrate menu:" + menu.getName();
            if (i > 0 || "N".equals(param.getOnlySubMenu())) {
                map.put(menu.getMenuId(), menu);

                mp.setProfile(param.getProfile());
                CommUtil.copyFields(menu, mp, null);
                // menus必须按层次递增排序，以保证父菜单首先被迁移到新环境，这样才能保证多个菜单移植后父子关系不变
                mp.setParentId(// 当前菜单的parentId是源系统带过来，
                        // 如果父菜单已迁移到当前系统则调整当前菜单的parentId为新系统父菜单的menuId，
                        // 如果map中没有父菜单记录说明当前菜单是直接挂到targetMenuId下的
                        map.containsKey(menu.getParentId()) ? map.get(menu.getParentId()).getMenuId()
                                : param.getTargetMenuId());

                boolean ignored = false;
                OperationParamVo operationParam = new OperationParamVo();
                operationParam.setMenuId(menu.getMenuId());

                List<Operation> operations = operationBiz.selectList(operationParam);
                List<OperationParamVo> objs = new ArrayList<>();
                for (Operation operation : operations) {
                    operationParam.setMenuId(operation.getMenuId());
                    operationParam.setCode(operation.getCode());
                    operationParam.setApplication(mp.getApplication());
                    operationParam.setMenuName(mp.getName());
                    if (operationBiz.countOperationMigration(operationParam) > 0) {
                        msg += "\n " + mp.getName();
                        ignored = true;
                        break;
                    }

                    OprPathParam opp = new OprPathParam();
                    opp.setMenuId(operation.getMenuId());
                    opp.setCode(operation.getCode());

                    List<OprPath> list = oprPathBiz.selectList(opp);
                    OperationVo vo = (OperationVo) operation;
                    vo.setPaths(list);
                    // operation.setPathJson(CommUtil.json(list));
                    log += "\nset paths(json=" + JSON.toJSONString(list) + ")";
                }

                if (ignored)
                    continue;

                mp.setOperationParams(objs);
                log += "\nset operation(json=" + JSON.toJSONString(objs) + ")";
                Menu temp = this.getMenuByName(mp.getName(), mp.getParentId());
                if (temp == null) {
                    mp.setMenuId(null);
                    this.insertMenu(mp);
                    log += "\ninsert menu(name=" + mp.getName() + ")";
                }
                else {
                    mp.setMenuId(temp.getMenuId());
                    this.updateMenu(mp);
                    log += "\nupdate menu(name=" + mp.getName() + ")";
                }

                // 记录新的menuId，后面会用作子菜单新的parentId
                menu.setMenuId(mp.getMenuId());
            }
            log += "\nend migrate menu:" + menu.getName();
        }
        return msg + log;
    }

    @DS("slave")
    private Menu getMenuByName(String name, int parentId) {
        MenuParam param = new MenuParam();
        param.setName(name);
        param.setParentId(parentId);
        return mapper.selectMenu(param);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateMenu(MenuParamVo param) {
        int count = 0;

        if (param.getOperationParams() != null) {
            OperationParam tmp = new OperationParam();
            tmp.setMenuId(param.getMenuId());
            operationBiz.deleteMigration(tmp);
            for (OperationParamVo op : param.getOperationParams()) {
                op.setApplication(param.getApplication());
                op.setProfile(param.getProfile());

                if (op.getPaths() != null) {
                    OprPathParam opp = new OprPathParam();
                    opp.setMenuId(param.getMenuId());
                    opp.setCode(op.getCode());
                    oprPathBiz.deleteMigration(opp);
                }

                operationBiz.insertOperation(param);// migration
            }
        }

        this.setWhoForUpdateInfo(param);
        if (count == mapper.updateMenu(param)) {
            mapper.copySourceLang(param);//
            // mapper.updateMenu(param)
        }

        return count;
    }

    @DS("slave")
    public String selectMenuCloneCount(String app) {
        return mapper.selectMenuCloneCount(app);
    }

    /**
     * 验证是否存在同名的menu
     *
     * @param param
     */
    @DS("slave")
    public void validate(MenuParam param) throws Exception {
        if (mapper.selectCount(param) > 0) {
            throw new IscException("ADMIN-001", "");
        }
    }

    /**
     * 获取菜单树
     *
     * @return
     */
    public MenuNode treeByRole(MenuParamVo param) {
        List<MenuNode> tree = null;
        List<MenuVo> menus = null;
        if (param.getParentId() != null) {
            Menu menu = mapper.selectMenu(param);
            String path = nodePath(menu.getParentPath(), menu.getMenuId());
            param.setPath(path);
        }
        menus = mapper.selectAllWebHeaderMenu(param);
        OperationParamVo op = new OperationParamVo();
        op.setRoleId(param.getRoleId());
        op.setApplication(param.getApplication());
        List<OperationVo> operations = operationBiz.selectAllOperations(op);
        tree = makeTree(menus, 0, 999, "Y".equals(param.getIncludeOperations()), operations);
        return tree == null ? null : tree.get(0);
    }

    private List<MenuNode> makeTree(List<MenuVo> menus, int startLevel, int stopLevel, boolean includeOperations,
            List<OperationVo> operations) {
        if (startLevel > stopLevel || menus == null || menus.size() == 0) {
            return null;
        }
        else {
            List<MenuNode> tree = new ArrayList<>();
            Iterator<MenuVo> it = menus.iterator();
            while (it.hasNext()) {
                MenuVo menu = it.next();
                int level = menu.getLevel() - startLevel;
                if (level == 0) {
                    MenuNode node = new MenuNode(menu);
                    if (includeOperations) {
                        setOperations(node, operations);
                    }
                    tree.add(node);
                    it.remove();
                }
                else {
                    break;
                }
            }

            if (menus.size() > 0) {
                List<MenuNode> subTree = this.makeTree(menus, startLevel + 1, stopLevel, includeOperations, operations);
                for (MenuNode child : subTree) {
                    for (MenuNode node : tree) {
                        // 当前的路径（包括当前节点），注意parentPath存储了节点的父路径（不包含当前节点）
                        String path = nodePath(node.getParentPath(), node.getMenuId());
                        // LOG.println(path + "/" + child.getParentPath());
                        // 如果patch和子节点的父路径一致则将子菜单树添加至当前节点
                        if (path.equals(child.getParentPath())) {
                            if (node.getSubMenus() == null) {
                                List<MenuNode> subMenu = new ArrayList<>();
                                node.setSubMenus(subMenu);
                            }
                            node.getSubMenus().add(child);
                            break;
                        }
                    }
                }
            }
            return tree;
        }
    }

    private void setOperations(MenuNode node, List<OperationVo> operations) {
        Iterator<OperationVo> it = operations.iterator();
        int grantCount = 0;
        int leafCount = 0;
        while (it.hasNext()) {
            OperationVo operation = it.next();
            if (operation.getMenuId().equals(node.getMenuId())) {
                MenuNode temp = new MenuNode();
                temp.setMenuId(operation.getMenuId());
                temp.setCode(operation.getCode());
                temp.setName(operation.getCode()); // 前端TreeData转换时,用name字段
                temp.setUrl(operation.getUrl());
                temp.setTemplateUrl(operation.getTemplateUrl());
                temp.setGrantFlag(operation.getGrantFlag());
                grantCount += "Y".equals(operation.getGrantFlag()) ? 1 : 0;
                leafCount++;
                if (node.getSubMenus() == null) {
                    node.setSubMenus(new ArrayList<>());
                }
                node.getSubMenus().add(temp);
                it.remove();
            }
            else if (operation.getMenuId() > node.getMenuId()) {
                break;
            }
        }
        if (leafCount > 0) {
            node.setGrantCount(grantCount);
            node.setLeafCount(leafCount);
        }
    }

    /**
     * 获取顶级菜单
     *
     * @return
     */
    @DS("slave")
    public List<Menu> listTop() {
        return mapper.listTop();
    }

    @DS("slave")
    public MenuNode listWithResource(ResourceRoleParam param) {
        List<MenuVo> menus = mapper.listWithResource(param);

        Map<Integer, MenuNode> map = new HashMap<>();
        MenuNode node = null;
        Integer parentId = null;

        for (MenuVo vo1 : menus) {
            MenuNode menu = new MenuNode(vo1);
            parentId = menu.getParentId();
            if (null == parentId) {
                node = menu;
            }
            else {
                map.get(parentId).getSubMenus().add(menu);
            }

            map.put(menu.getMenuId(), menu);
            int count = menu.getResources().size();
            if (count > 0) {
                int grantCount = count;
                for (ResourceVo vo : menu.getResources()) {
                    if (!"Y".equals(vo.getGrantFlag())) {
                        grantCount--;
                    }
                }
                MenuNode tmp = menu;

                while (null != parentId) {
                    MenuNode p = map.get(parentId);
                    p.setLeafCount(p.getLeafCount() + count);
                    p.setGrantCount(p.getGrantCount() + grantCount);
                    parentId = p.getParentId();
                }
            }
        }
        return node;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertWithOperation(MenuParamVo vo) {
        super.setWho(vo);
        mapper.insert(vo);
        mapper.insertTL(vo);

        if (0 < vo.getOperations().size()) {
            for (Operation param : vo.getOperations()) {
                param.setMenuId(vo.getMenuId());
                param.setApplication(vo.getApplication());
                param.setProfile(vo.getProfile());
            }
            operationBiz.insertBulk(vo.getOperations());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWithOperation(MenuParamVo vo) {
        List<Operation> adds = Lists.newArrayList();
        List<Operation> updates = Lists.newArrayList();
        // Update menu header
        int count = mapper.update(vo);
        if (count == 1) {
            mapper.updateTL(vo);
        }
        // Update operation
        for (Operation op : vo.getOperations()) {
            if (null == op.getMenuId()) {
                op.setMenuId(vo.getMenuId());
                adds.add(op);
            }
            else {
                updates.add(op);
            }
        }

        if (updates.size() > 0) {
            for (Operation param : updates) {
                param.setProfile(vo.getProfile());
                param.setApplication(vo.getApplication());
            }
            operationBiz.updateBulk(updates);
        }

        if (adds.size() > 0) {
            for (Operation param : adds) {
                param.setProfile(vo.getProfile());
                param.setApplication(vo.getApplication());
            }
            operationBiz.insertBulk(adds);
        }

        if (null != vo.getDeletes() && vo.getDeletes().size() > 0) {
            operationBiz.deleteBatch(vo.getDeletes());
        }
    }

    @DS("slave")
    public List<MenuNode> tree(MenuParamVo param) {
        List<Menu> menus = mapper.find(param);

        return this.makeTree(menus);
    }

    @DS("slave")
    public List<MenuNode> treeWithResource(MenuParamVo param) {
        List<MenuVo> menus = mapper.listWithResourceByUserId(param);

        return this.makeTree(menus);
    }

    private List<MenuNode> makeTree(List menus) {
        Map<Integer, MenuNode> map = new HashMap<>();
        List<MenuNode> nodes = Lists.newArrayList();
        List<MenuNode> ret = Lists.newArrayList();
        menus.forEach(o -> {
            MenuNode node = null;
            if (o instanceof MenuVo) {
                node = new MenuNode((MenuVo) o);
            }
            else {
                node = new MenuNode((Menu) o);
            }
            nodes.add(node);
            map.put(node.getMenuId(), node);
        });

        nodes.forEach(o -> {
            if (o.getLevel() == 0) {
                ret.add(o);
            }
            else {
                map.get(o.getParentId()).getSubMenus().add(o);
            }
        });

        return ret;
    }

    @DS("slave")
    public List<MenuVo> listWithOperation(MenuParamVo param) {
        return mapper.listWithOperation(param);
    }

    public void displayed(MenuParamVo param) {
        param.getMenus().forEach(o -> {
            o.setProfile(param.getProfile());
            super.setWhoForUpdateInfo(o);
        });
        mapper.displayed(param.getMenus());
    }
}
