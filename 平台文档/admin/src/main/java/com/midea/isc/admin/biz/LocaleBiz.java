package com.midea.isc.admin.biz;

import com.midea.isc.admin.feign.LanguageFeign;
import com.midea.isc.admin.mapper.LocaleMapper;
import com.midea.isc.admin.model.Locale;
import com.midea.isc.admin.param.LocaleParam;
import com.midea.isc.common.biz.BaseBiz;
import com.midea.isc.common.sys.IscException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocaleBiz extends BaseBiz<LocaleMapper, Locale, LocaleParam> {

    @Autowired
    private LanguageFeign languageFeign;

    public void add(LocaleParam param) throws Exception {
        if (mapper.countDuplicate(param) > 0) {
            throw new IscException("ADMIN-001", "重复");
        }

        this.insert(param);

        LocaleParam newParam = new LocaleParam();
        newParam.setApplication(param.getApplication());
        List<Locale> locales = mapper.find(newParam);

        List<String> langs = new ArrayList<>();
        if (null != locales)
            locales.forEach(o -> langs.add(o.getLanguage()));

        languageFeign.setAppLanguage(param.getApplication(), langs);
    }

    /**
     * 获取app下的语言
     * @param app
     * @return
     */
    public List<String> getLanguages(String app){
        LocaleParam localeParam = new LocaleParam();
        localeParam.setApplication(app);
        List<Locale> locales = this.selectList(localeParam);

        List<String> ret = new ArrayList<>();
        for(Locale locale : locales){
            ret.add(locale.getLanguage());
        }

        return ret;
    }
}
