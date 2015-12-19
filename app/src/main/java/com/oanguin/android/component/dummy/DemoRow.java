package com.oanguin.android.component.dummy;

import com.android.oanguin.component.paginationtable.Icon;
import com.android.oanguin.component.paginationtable.SortedMethod;

/**
 * Created by oanguin on 12/18/2015.
 */
public class DemoRow {
    public String col1;
    public String col2;
    public String col3;

    public DemoRow(String col1, String col2, String col3) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
    }

    @SortedMethod(value=1)
    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    @SortedMethod(value=2)
    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    @SortedMethod(value=3)
    @Icon(fonttype="fontawesome-webfont.ttf",fontsize=30f,fontcolor="#000033")
    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }
}
