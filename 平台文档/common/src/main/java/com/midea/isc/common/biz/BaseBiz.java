package com.midea.isc.common.biz;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.midea.isc.common.mapper.BaseMapper;
import com.midea.isc.common.param.BasicParam;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.BaseContextHandler;
import com.midea.isc.common.vo.BulkStruct;

/**
 * Created by wangxk Date: 18/11/19 Time: 15:13 Version 1.0.0
 */
public abstract class BaseBiz<M extends BaseMapper<T, P>, T extends BasicParam, P extends BasicParam> {
    @Autowired
    protected M mapper;

    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(P param) {
        return mapper.selectOne(param);
    }

    public List<T> selectList(P param) {
        return mapper.selectList(param);
    }

    public int selectCount(P param) {
        return mapper.selectCount(param);
    }

    public int insert(T model) {
        setWho(model);
        return mapper.insert(model);
    }

    /**
     * 暂不支持多语言表和Oracle的批量
     * 
     * @param list
     * @return
     */
    public int insertBulk(List<T> list) {
        list.forEach(o -> {
            setWho(o);
        });
        return mapper.insertBulk(list);
    }

    public int update(T model) {
        setWhoForUpdateInfo(model);
        return mapper.update(model);
    }

    public int updateFields(T model) {
        setWhoForUpdateInfo(model);
        return mapper.updateFields(model);
    }

    public int updateByOtsLock(T model) throws IscException {
        setWhoForUpdateInfo(model);
        int count = mapper.updateByOtsLock(model);
        if (count == 0) {
            throw new IscException("ISC-929");
        }
        return count;
    }

    public int updateFieldsByOtsLock(T model) throws IscException {
        setWhoForUpdateInfo(model);
        int count = mapper.updateFieldsByOtsLock(model);
        if (count == 0) {
            throw new IscException("ISC-929");
        }
        return count;
    }

    /**
     * 暂不支持多语言表的批量
     * 
     * @param list
     * @return
     */
    public int updateBulk(List<T> list) {
        list.forEach(o -> {
            setWhoForUpdateInfo(o);
        });
        return mapper.updateBulk(list);
    }

    /**
     * 暂不支持多语言表的批量
     * 
     * @param list
     * @return
     */
    public int updateFieldsBulk(List<T> list) {
        list.forEach(o -> {
            setWhoForUpdateInfo(o);
        });
        return mapper.updateFieldsBulk(list);
    }

    public int delete(T model) {
        return mapper.delete(model);
    }

    /**
     * @param list
     * @return
     */
    public int deleteBulk(List<T> list) {
        return mapper.deleteBulk(list);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int saveFieldsBulk(BulkStruct<T> bulk) {
        int result = 0;
        if (bulk.getAdds() != null && !bulk.getAdds().isEmpty()) {
            result += insertBulk(bulk.getAdds());
        }
        if (bulk.getUpdates() != null && !bulk.getUpdates().isEmpty()) {
            result += updateFieldsBulk(bulk.getUpdates());
        }
        if (bulk.getDeletes() != null && !bulk.getDeletes().isEmpty()) {
            result += deleteBulk(bulk.getDeletes());
        }

        return result;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int saveBulk(BulkStruct<T> bulk) {
        int result = 0;
        if (bulk.getAdds() != null && !bulk.getAdds().isEmpty()) {
            result += insertBulk(bulk.getAdds());
        }
        if (bulk.getUpdates() != null && !bulk.getUpdates().isEmpty()) {
            result += updateBulk(bulk.getUpdates());
        }
        if (bulk.getDeletes() != null && !bulk.getDeletes().isEmpty()) {
            result += deleteBulk(bulk.getDeletes());
        }

        return result;
    }

    public List<T> find(P param) {
        return mapper.find(param);
    }

    public int total(P param) {
        return mapper.total(param);
    }

    protected void setWho(T model) {
        if (model != null) {
            if (model.getProfile() == null) {
                if (BaseContextHandler.getProfile() != null) {
                    model.setProfile(BaseContextHandler.getProfile());
                }
                else {
                    return;
                }
            }
            Class<?> clazz = model.getClass();
            while (!clazz.getSimpleName().equals("BasicModel")) {
                Class<?> superClazz = clazz.getSuperclass();
                if (superClazz != null) {
                    clazz = superClazz;
                }
                else {
                    clazz = model.getClass();
                    break;
                }
            }
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].isAccessible())
                    fields[i].setAccessible(true);
                if ("createdBy".equals(fields[i].getName()) || "lastUpdatedBy".equals(fields[i].getName())) {
                    try {
                        if (fields[i].getGenericType().toString().equals("class java.lang.String")) {
                            fields[i].set(model, model.getProfile().get__userName());
                        }
                    }
                    catch (IllegalAccessException e) {
                    }
                }
            }
        }
    }

    protected void setWhoForUpdateInfo(T model) {
        if (model != null) {
            if (model.getProfile() == null) {
                if (BaseContextHandler.getProfile() != null) {
                    model.setProfile(BaseContextHandler.getProfile());
                }
                else {
                    return;
                }
            }
            Class<?> clazz = model.getClass();
            while (!clazz.getSimpleName().equals("BasicModel")) {
                Class<?> superClazz = clazz.getSuperclass();
                if (superClazz != null) {
                    clazz = superClazz;
                }
                else {
                    clazz = model.getClass();
                    break;
                }
            }
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].isAccessible())
                    fields[i].setAccessible(true);
                if ("lastUpdatedBy".equals(fields[i].getName())) {
                    try {
                        if (fields[i].getGenericType().toString().equals("class java.lang.String")) {
                            fields[i].set(model, model.getProfile().get__userName());
                        }
                    }
                    catch (IllegalAccessException e) {
                    }
                }
            }
        }
    }
}
