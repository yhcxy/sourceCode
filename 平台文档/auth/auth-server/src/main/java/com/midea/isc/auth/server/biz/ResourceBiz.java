package com.midea.isc.auth.server.biz;

import com.midea.isc.auth.server.mapper.ResourceMapper;
import com.midea.isc.auth.server.model.Resource;
import com.midea.isc.auth.server.param.ResourceParam;
import com.midea.isc.auth.server.vo.ResourceVo;
import com.midea.isc.common.biz.BaseBiz;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@Service
public class ResourceBiz extends BaseBiz<ResourceMapper,Resource,ResourceParam> {
	public List<ResourceVo> listPermission(ResourceVo vo){
        return mapper.listPermission(vo);
    }

    public static void main(String[] a) throws Exception {
        File file = new File("D:\\MyData\\wujm13\\Documents\\WeChat Files\\ac88888_wu\\FileStorage\\File\\2019-04\\RVYOKA01_077");
        InputStream is = new FileInputStream(file);
        StringBuffer sb = new StringBuffer();
        int i = is.read();
        while(i > 0){
            sb.append((char)i);
            i = is.read();
        }
        System.out.println(i);
    }
}

