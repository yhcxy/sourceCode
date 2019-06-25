package com.midea.isc.common.mapper;

import java.util.List;

import com.midea.isc.common.param.BasicParam;

public interface BaseMapper<T extends BasicParam, P extends BasicParam> {
    public T selectOne(P param);

    public List<T> selectList(P param);

    public int selectCount(P param);

    public int insert(T model);

    public int insertBulk(List<T> list);

    public int update(T model);

    public int updateFields(T model);

    public int updateByOtsLock(T model);

    public int updateFieldsByOtsLock(T model);

    public int updateBulk(List<T> list);

    public int updateFieldsBulk(List<T> list);

    public int delete(T model);

    public int deleteBulk(List<T> list);

    public List<T> find(P param);

    public int total(P param);

}
