package com.android.oanguin.component.paginationtable;

import java.util.ArrayList;
import java.util.List;

public interface GenericPagination {

	enum OPTION{
		PREVIOUS,NEXT,FIRST,LAST;
	}
	List<Object> genericObjectList = new ArrayList<Object>();
	
	public List<Object> next();
	
	public List<Object> previous();
	
	public List<Object> current();
	
	public List<Object> first();
	
	public List<Object> last();
	
	
}
