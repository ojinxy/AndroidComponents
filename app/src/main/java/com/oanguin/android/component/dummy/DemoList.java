package com.oanguin.android.component.dummy;

import com.android.oanguin.component.paginationtable.GenericList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.oanguin.android.component.R;

/**
 * Created by oanguin on 12/18/2015.
 */
public class DemoList extends GenericList {

    List<DemoRow> demoRows;

    public DemoList(List<DemoRow> genericList, int rowsToDisplay, String pagingInfoType, boolean showColHeader, int rowsBeforeData, boolean allowSorting, SORTORDER defaultSortOrder, String defaultSortType) {
        super(Arrays.asList(genericList.toArray()), rowsToDisplay, pagingInfoType, showColHeader, rowsBeforeData, true, defaultSortOrder, defaultSortType);
        this.demoRows = genericList;
        this.setRowsToDisplay(rowsToDisplay);
        this.sortByArrayId = R.array.demosort;
        commonConstructorCode();
    }


    private void commonConstructorCode(){
        this.getMethodNames().add(0, "getCol1");
        this.getMethodNames().add(1, "getCol2");
        this.getMethodNames().add(2, "getCol3");
        this.getHeaders().add(0,"Column 1");
        this.getHeaders().add(1,"Column 2");
        this.getHeaders().add(2,"Column 3");

    }

    public static List<DemoRow> generateDummyValuesForRow(int size){
        List<DemoRow> demoRows = new ArrayList<DemoRow>() ;
        for(int i=1; i < size; i++){
            DemoRow row = new DemoRow("Row " + i + " x Column 1",
                    "Row " + i + " x Column 2","&#xf219;");

            demoRows.add(row);
        }

        return demoRows;
    }
    @Override
    public String getFunctionNameForSorting(String valueToCheck) {
        if(valueToCheck.equalsIgnoreCase("Column 1")){
            return "getCol1";
        }else if(valueToCheck.equalsIgnoreCase("Column 2")){
            return "getCol2";
        }else if(valueToCheck.equalsIgnoreCase("Column 3")) {
            return "getCol3";
        }else{return "getCol1";}
    }

    @Override
    public Class<?> getClassToBeUsedForReflection() {
        return DemoRow.class;
    }
}
