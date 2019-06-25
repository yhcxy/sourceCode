package com.midea.isc.admin.biz;

import com.midea.isc.admin.config.WebConfig;
import com.midea.isc.admin.mapper.ApplicationMapper;
import com.midea.isc.admin.model.Application;
import com.midea.isc.admin.model.Assigment;
import com.midea.isc.admin.model.Module;
import com.midea.isc.admin.model.Privilege;
import com.midea.isc.admin.model.Role;
import com.midea.isc.admin.param.ApplicationParam;
import com.midea.isc.admin.param.AssigmentParam;
import com.midea.isc.admin.param.ModuleParam;
import com.midea.isc.admin.param.PrivilegeParam;
import com.midea.isc.admin.param.RoleParam;
import com.midea.isc.admin.vo.*;
import com.midea.isc.common.annotation.DS;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.model.Profile;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationBiz extends BaseBiz<ApplicationMapper, Application, ApplicationParam> {

    @Autowired
    private MenuBiz menuBiz;

    @Autowired
    private RoleBiz roleBiz;

    @Autowired
    private AssigmentBiz assigmentBiz;

    @Autowired
    private PrivilegeBiz privilegeBiz;

    @Autowired
    private OprPathBiz oprPathBiz;

    @Autowired
    private OperationBiz operationBiz;

    @Autowired
    private WebConfig configuration;

    @Autowired
    private ModuleBiz moduleBiz;

    /**
     * 插入要用这个，还有别的逻辑
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int insertApp(ApplicationParamVo param) {
        int count = 0;
        param.setApplication(param.getApplication().toLowerCase());

        if ("inner".equals(param.getRelationship())) {
            MenuParamVo mp = new MenuParamVo();
            mp.setName(param.getTopMenu());
            mp.setLocalName(param.getName());
            mp.setDescription(param.getTopMenu());
            mp.setLevel(0);
            mp.setSeqNum(param.getDefaultSequence());
            mp.setDisplayed("Y");
            mp.setProfile(param.getProfile());
            mp.setApplication(param.getApplication());

            RoleParam rp = new RoleParam();
            rp.setRoleName("SYS_ADMIN");
            rp.setRoleDescription("System Administrator");
            rp.setEnableFlag("Y");
            rp.setProfile(param.getProfile());
            rp.setApplication(param.getApplication());
            if (menuBiz.insertMenu(mp) > 0 && roleBiz.insert(rp) > 0) {
                param.setTopMenuId(mp.getMenuId());
                return this.insert(param);
            }
        }
        else {
            return this.insert(param);
        }

        return count;
    }

    /**
     * 获取某人有权限的app
     * @param param
     * @return
     */
    public List<Application> getAccessibleApp(ApplicationParam param) {
        List<Application> applications = mapper.getAccessibleApp(param);

        return applications;
    }

    /**
     * 获取某人有权限的app
     * @param param
     * @return
     */
    public List<String> getAccessibleAppString(ApplicationParam param) {
        List<Application> applications = mapper.getAccessibleApp(param);
        List<String> ret = new ArrayList<>();

        for (Application a : applications){
            ret.add(a.getApplication());
        }
        return ret;
    }

    public HashMap<String, List<Module>> getAccessibleModule(ApplicationParam param) {
        HashMap<String, List<Module>> result = new HashMap<>();
        List<Application> applications = mapper.getAccessibleApp(param);
        String apps = "";
        if (applications != null && !applications.isEmpty()) {
            for (Application app : applications) {
                apps += app.getApplication() + ",";
                result.put(app.getApplication(), new ArrayList<Module>());
            }
            if (apps.length() > 0) {
                apps = apps.substring(0, apps.length() - 1);
                ModuleParam moduleParam = new ModuleParam();
                moduleParam.setProfile(param.getProfile());
                moduleParam.setApplicationCond("me");
                moduleParam.setApplication(apps);
                List<Module> modules = moduleBiz.find(moduleParam);
                for (Module module : modules) {
                    result.get(module.getApplication()).add(module);
                }
            }
        }

        return result;
    }

    /**
     * 是否有权限
     * @return
     */
    public void hasPermission(Profile profile, String app) throws IscException{
        ApplicationParam applicationParam = new ApplicationParam();
        applicationParam.setProfile(profile);
        List<String> apps = this.getAccessibleAppString(applicationParam);

        if(!apps.contains(app)) {
            throw new IscException("APP-001");
        }
    }

    /**
     * 复制app
     * @param param
     * @return
     * @throws Exception
     */
    public String clone(ApplicationParamVo param) throws Exception {
        String application = param.getApplication();
        if (configuration.getEnvTag().toLowerCase().equals("dev")) {
            String msg = "";
            for (String module : param.getModules()) {
                if ("privilege".equalsIgnoreCase(module)) {
                    menuBiz.clearMenu(application);
                    operationBiz.clearOperation(application);
                    oprPathBiz.clearOprPath(application);
                    roleBiz.clearRole(application);
                    assigmentBiz.clearAssigment(application);
                    privilegeBiz.clearPrivilege(application);

                    // Menu
                    int srcMenuId = menuBiz.selectRootMenuIdMigration(application);
                    int dstMenuId = menuBiz.selectRootMenuId(application);
                    MenuParamVo mp = new MenuParamVo();
                    mp.setProfile(param.getProfile());
                    mp.setMenuId(srcMenuId);
                    mp.setTargetMenuId(dstMenuId);
                    mp.setOnlySubMenu("Y");
                    menuBiz.migration(mp);

                    // Role
                    RoleParam rp = new RoleParam();
                    rp.setApplication(application);
                    List<Role> roles = roleBiz.selectList(rp);
                    List<Role> batch = new ArrayList<>();
                    for (Role role : roles) {
                        role.setRoleId(null);
                        batch.add(role);
                        if (batch.size() > 100) {
                            roleBiz.batchInsert(batch);
                            batch.clear();
                        }
                    }
                    if (batch.size() > 0)
                        roleBiz.batchInsert(batch);

                    // Assigment
                    AssigmentParamVO ap = new AssigmentParamVO();
                    ap.setApplication(application);
                    List<Assigment> assigments = assigmentBiz.selectListMigration(ap);
                    for (Assigment assigment : assigments) {
                        ap = new AssigmentParamVO();
                        CommUtil.copyFields(assigment, ap, "assigmentId");
                        ap.setApplication(application);
                        ap.setProfile(param.getProfile());
                        assigmentBiz.insertByName(ap);
                    }

                    // Privilege
                    PrivilegeParamVo pp = new PrivilegeParamVo();
                    pp.setApplication(application);
                    List<Privilege> privileges = privilegeBiz.selectListMigration(pp);
                    for (Privilege privilege : privileges) {
                        PrivilegeParamVo p = new PrivilegeParamVo();
                        CommUtil.copyFields(privilege, p, "privilegeId");
                        p.setApplication(application);
                        p.setProfile(param.getProfile());
                        privilegeBiz.insertByName(p);
                    }
                    msg += menuBiz.selectMenuCloneCount(application) + roleBiz.selectRoleCloneCount(application);
                }
                else if ("workflow".equalsIgnoreCase(module)) {
                    /*
                     * st_common.delete(statement("clearWfForm"), application);
                     * st_common .delete(statement("clearWfAttribute"),
                     * application); st_common.delete(statement("clearWfEvent"),
                     * application);
                     * st_common.delete(statement("clearWfInstance"),
                     * application);
                     * st_common.delete(statement("clearWfProcess"),
                     * application);
                     * 
                     * WfFormParam wp = new WfFormParam();
                     * wp.setApplication(application); List<WfForm> list =
                     * source .selectList(
                     * "com.midea.iapps.common.service.impl.WfFormServiceImpl.selectList",
                     * wp); for (WfForm form : list) { wp = new WfFormParam();
                     * wp.setFormId(form.getFormId());
                     * wp.setProfile(param.getProfile()); formDAO.migrate(wp,
                     * source); } msg +=
                     * st_common.selectOne(this.statement("selectWfCloneCount"),
                     * application);
                     */
                }
                else if ("excel".equalsIgnoreCase(module)) {
                    /*
                     * st_common.delete(statement("clearExcelTemplate"),
                     * application); st_common
                     * .delete(statement("clearExcelColumn"), application);
                     * 
                     * ExcelTemplateParam ep = new ExcelTemplateParam();
                     * ep.setApplication(application); List<ExcelTemplate> list
                     * = source .selectList(
                     * "com.midea.iapps.common.service.impl.ExcelTemplateServiceImpl.selectList",
                     * ep); for (ExcelTemplate template : list) { ep = new
                     * ExcelTemplateParam();
                     * ep.setTemplateId(template.getTemplateId());
                     * ep.setProfile(param.getProfile()); excelDAO.migrate(ep,
                     * source); } msg += st_common.selectOne(this.statement(
                     * "selectExcelCloneCount"), application); } else if
                     * ("lookup".equalsIgnoreCase(module)) {
                     * st_common.delete(statement("clearLookupType"),
                     * application); st_common
                     * .delete(statement("clearLookupValue"), application);
                     * st_common.delete(statement("clearLookupAccess"),
                     * application);
                     * 
                     * // Lookup LookupTypeParam lp = new LookupTypeParam();
                     * lp.setApplication(application); List<LookupType> list =
                     * source .selectList(
                     * "com.midea.iapps.common.service.impl.LookupTypeServiceImpl.selectList",
                     * lp); for (LookupType lookup : list) { lp = new
                     * LookupTypeParam(); lp.setLookupId(lookup.getLookupId());
                     * lp.setProfile(param.getProfile()); lookupDAO.migrate(lp,
                     * source); }
                     * 
                     * // Lookup Access LookupAccessParam ap = new
                     * LookupAccessParam(); ap.setApplication(application);
                     * List<LookupAccess> accesses = source .selectList(
                     * "com.midea.iapps.common.service.impl.LookupAccessServiceImpl.selectList",
                     * ap); for (LookupAccess access : accesses) { ap = new
                     * LookupAccessParam(); this.copyFields(access, ap,
                     * "accessId"); ap.setApplication(application);
                     * ap.setProfile(param.getProfile());
                     * accessDAO.insertByStrValues(ap); }
                     * 
                     * msg += st_common.selectOne(
                     * this.statement("selectLookupCloneCount"), application);
                     */
                }
            }
            return msg;
        }
        return null;
    }

    @DS("slave")
    public List<ApplicationVo> list(ApplicationParam param) {
        return mapper.list(param);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAll(@RequestBody ApplicationParamVo vo) throws IscException {
        List<Application> applications = new ArrayList<>();
        applications.addAll(vo.getUpdates());
        applications.addAll(vo.getAdds());

        List<String> names = new ArrayList<>();
        List<String> apps = new ArrayList<>();
        boolean name = false, app = false;

        for (Application a : applications) {
            if (names.indexOf(a.getName()) > -1) {
                throw new IscException("ADMIN-002");
            }
            names.add(a.getName());

            if (apps.indexOf(a.getApplication()) > -1) {
                throw new IscException("ADMIN-002");
            }
            apps.add(a.getApplication());
        }

        Map<String, List<String>> map = new HashMap<>();
        map.put("apps", apps);
        map.put("names", names);

        /*
         * int count = mapper.countDuplicate(map); if(count > 0){ throw new
         * IscException("ADMIN-002"); }
         */

        vo.getUpdates().forEach(o -> {
            o.setProfile(vo.getProfile());
            super.setWhoForUpdateInfo(o);
        });

        vo.getAdds().forEach(o -> {
            o.setProfile(vo.getProfile());
            super.setWho(o);
        });

        if (vo.getUpdates().size() > 0)
            mapper.updateBatch(vo.getUpdates());

        if (vo.getAdds().size() > 0)
            mapper.insertBatch(vo.getAdds());
    }

    public void deleteBatch(List<Application> apps) {
        mapper.deleteBatch(apps);
    }
}
