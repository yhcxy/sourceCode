package com.midea.isc.common.vo;

import com.midea.isc.common.model.Profile;
import com.midea.isc.common.param.BasicParam;
import com.midea.isc.common.util.BaseContextHandler;
import lombok.Data;

import java.util.List;

@Data
public class BulkStruct<V extends BasicParam> extends BasicParam {
    private List<V> adds;
    private List<V> updates;
    private List<V> deletes;

    public void setAdds(List<V> adds){
        Profile profile = BaseContextHandler.getProfile();
        adds.forEach(o ->{
            o.setProfile(profile);
        });
        this.adds = adds;
    }

    public void setUpdates(List<V> updates){
        Profile profile = BaseContextHandler.getProfile();
        updates.forEach(o ->{
            o.setProfile(profile);
        });
        this.updates = updates;
    }

    public void setDeletes(List<V> deletes){
        Profile profile = BaseContextHandler.getProfile();
        deletes.forEach(o ->{
            o.setProfile(profile);
        });
        this.deletes = deletes;
    }
}
