package com.android.oanguin.component.paginationtable;

import java.util.List;

public interface GenericSort {

	public enum SORTORDER{
		ASC,DESC;
	}
	public List<Object> sort(SORTORDER sortOrder, String methodNameSortedBy, Object objectClass);
	
	public String getFunctionNameForSorting(String valueToCheck);
	
	public Class<?> getClassToBeUsedForReflection();
}
