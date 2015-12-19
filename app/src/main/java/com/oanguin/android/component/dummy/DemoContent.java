package com.oanguin.android.component.dummy;

import android.content.Context;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.android.oanguin.component.paginationtable.*;
import java.util.Arrays;

public class DemoContent {

    public static Context context;

    public DemoContent(Context context) {
        this.context = context;
        addItem(createDemoItem("1","Pagination Table",DEMOITEMENUM.PAGINATIONTABLE,context));
    }

    public static enum DEMOITEMENUM{
        PAGINATIONTABLE;

    }
    public static final List<DemoItem> ITEMS = new ArrayList<DemoItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DemoItem> ITEM_MAP = new HashMap<String, DemoItem>();

    private static final int COUNT = 1;

    /*static {
        addItem(createDemoItem("1","Pagination Table",DEMOITEMENUM.PAGINATIONTABLE,context));

    }*/

    private static void addItem(DemoItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DemoItem createDemoItem(String id, String description,DEMOITEMENUM demoitem,Context context) {
        return new DemoItem(id,description,demoitem,context);
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class DemoItem {
        public final String id;
        public final LinearLayout content;
        public final String details;

        public DemoItem(String id,String details,DEMOITEMENUM demoitem,Context context) {
            this.id = id;
            content = new LinearLayout(context);
            content.setOrientation(LinearLayout.VERTICAL);
            this.details = details;

            if(demoitem == DEMOITEMENUM.PAGINATIONTABLE){
                //Add Dummy Table Data
                DemoList demoList = new DemoList(DemoList.generateDummyValuesForRow(20),10,
                        "Pagination Demo Table",true,3,true, GenericSort.SORTORDER.DESC,"Column 1");

                GenericTable gentable = new GenericTable(context,"Demo Table",new ArrayList<String>(
                        Arrays.asList("Column 1", "Column 2", "Column 3")),demoList);



                content.addView(gentable.table);
            }
        }

       public LinearLayout getContent(){
           return content;
       }
    }
}
