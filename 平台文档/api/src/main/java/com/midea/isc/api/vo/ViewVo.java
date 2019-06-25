package com.midea.isc.api.vo;

import com.midea.isc.api.model.View;
import com.midea.isc.api.model.ViewFilter;
import com.midea.isc.api.model.ViewLayout;
import lombok.Data;

import java.util.List;

@Data
public class ViewVo extends View {
    private List<ViewFilter> filters;
    private List<ViewLayout> layouts;
}
