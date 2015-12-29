package com.android.oanguin.component.paginationtable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.common.collect.Ordering;

public abstract class GenericList implements GenericPagination, GenericSort {

	
	int rowsToDisplay = 0;
	String sortBy = "";
	List<String> headers;
	List<String> methodNames;
	int lastIndex = 0;
	int firstIndex = 0;
	int totalAmountOfRows = 0;
	List<Object> genericList;
	String pagingInfo = "";
	String pagingInfoType = "";
	boolean enablePrevious,enableNext,enableFirst,enableLast;
	boolean showColHeader = true;
	int rowsBeforeData= 0;
	public boolean allowSorting = true;
	public int sortByArrayId;
	public boolean useView = false;
	public SORTORDER defaultSortOrder;
	public String defaultSortType;
	
	public GenericList() {
		headers = new ArrayList<String>();
		methodNames = new ArrayList<String>();
		this.genericList = new ArrayList<Object>();
		this.totalAmountOfRows = 0;
		rowsToDisplay = 10;
		pagingInfoType = "";
		initiliazeIndexes();
	}
	
	public GenericList(List<Object> genericList,int rowsToDisplay,String pagingInfoType, boolean showColHeader, int rowsBeforeData,boolean allowSorting,SORTORDER defaultSortOrder,String defaultSortType) {
		headers = new ArrayList<String>();
		methodNames = new ArrayList<String>();
		this.genericList = genericList;
		this.totalAmountOfRows = this.genericList.size();
		this.rowsToDisplay = rowsToDisplay;
		this.pagingInfoType = pagingInfoType;
		initiliazeIndexes();
		this.showColHeader = showColHeader;
		this.rowsBeforeData = rowsBeforeData;
		this.allowSorting = allowSorting;
		this.defaultSortOrder = defaultSortOrder;
		this.defaultSortType = defaultSortType;
	}
	
	

	@Override
	public List<Object> sort(SORTORDER sortOrder,final String methodNameSortedBy,Object objectClass) {
	
		if(allowSorting == false){
			initiliazeIndexes();
			return this.genericList.subList(firstIndex, lastIndex);
		}
		Ordering<Object> orderingValues = new Ordering<Object>() {

			@Override
			public int compare(Object left, Object right) {
				int valueToReturn = 0;
				try {
					
					Method methodLeft = left.getClass().getDeclaredMethod(methodNameSortedBy);
					Method methodRight = right.getClass().getDeclaredMethod(methodNameSortedBy);
					methodLeft.setAccessible(true);
					methodRight.setAccessible(true);
					Object leftValue = methodLeft.invoke(left);
					Object rightValue = methodRight.invoke(right);

					if(leftValue instanceof String && rightValue instanceof String){
						valueToReturn = ((String)leftValue).compareTo((String)rightValue);
					}else if(leftValue instanceof Date && rightValue instanceof Date){
						valueToReturn = ((Date)leftValue).compareTo((Date)rightValue);
					}else if(leftValue instanceof Integer && rightValue instanceof Integer){
						valueToReturn =  ((Integer)leftValue).compareTo((Integer)rightValue);
					}else{
						valueToReturn =  0;
					}
					
				} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					
					e.printStackTrace();
				}
				
				return valueToReturn;
				
			}


		};
			
		switch(sortOrder){
			case ASC:
				genericList = orderingValues.nullsFirst().sortedCopy(genericList);
				break;
			case DESC:
				genericList = orderingValues.nullsFirst().reverse().sortedCopy(genericList);
				break;
		}
		
		initiliazeIndexes();
		return this.genericList.subList(firstIndex, lastIndex);
		
	}

	@Override
	public List<Object> next() {
		this.calculateIndexes(GenericPagination.OPTION.NEXT);
		return this.genericList.subList(firstIndex, lastIndex);
		
	}

	@Override
	public List<Object> previous() {
		this.calculateIndexes(GenericPagination.OPTION.PREVIOUS);
		return this.genericList.subList(firstIndex, lastIndex);
	}
	

	@Override
	public List<Object> current() {
		if(firstIndex >= 0  && lastIndex <= totalAmountOfRows){
			return this.genericList.subList(firstIndex, lastIndex);
		}else{
			return null;
		}
		
	}
	
	@Override
	public List<Object> first() {
		this.calculateIndexes(GenericPagination.OPTION.FIRST);
		return this.genericList.subList(firstIndex, lastIndex);
	}

	@Override
	public List<Object> last() {
		this.calculateIndexes(GenericPagination.OPTION.LAST);
		return this.genericList.subList(firstIndex, lastIndex);
	}

	public int getRowsToDisplay() {
		return rowsToDisplay;
	}

	public void setRowsToDisplay(int rowsToDisplay) {
		this.rowsToDisplay = rowsToDisplay;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<String> getMethodNames() {
		return methodNames;
	}

	public void setMethodNames(List<String> methodNames) {
		this.methodNames = methodNames;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	public int getTotalAmountOfRows() {
		return totalAmountOfRows;
	}

	public void setTotalAmountOfRows(int totalAmountOfRows) {
		this.totalAmountOfRows = totalAmountOfRows;
	}

	public List<Object> getGenericList() {
		return genericList;
	}

	public void setGenericList(List<Object> genericList) {
		this.genericList = genericList;
	}
	
	
	public boolean isEnablePrevious() {
		return enablePrevious;
	}

	public void setEnablePrevious(boolean enablePrevious) {
		this.enablePrevious = enablePrevious;
	}

	public boolean isEnableNext() {
		return enableNext;
	}

	public void setEnableNext(boolean enableNext) {
		this.enableNext = enableNext;
	}

	public boolean isEnableFirst() {
		return enableFirst;
	}

	public void setEnableFirst(boolean enableFirst) {
		this.enableFirst = enableFirst;
	}

	public boolean isEnableLast() {
		return enableLast;
	}

	public void setEnableLast(boolean enableLast) {
		this.enableLast = enableLast;
	}

	public void initiliazeIndexes(){
		firstIndex = 0;
		if(rowsToDisplay >= totalAmountOfRows){			
			lastIndex = totalAmountOfRows;
		}else{
			lastIndex = rowsToDisplay;
		}
		
		checkValidSelections();
		compilePagingInfo();
	}
	
	public void calculateIndexes(GenericPagination.OPTION option){
		
		switch(option){
			case FIRST:
				if(rowsToDisplay >= totalAmountOfRows || firstIndex == 0){
					System.err.println("First button should have been disabled.");
				}else{
					firstIndex = 0;
					lastIndex = rowsToDisplay;
				}
				break;
			case LAST:
				if(rowsToDisplay >= totalAmountOfRows || lastIndex >= totalAmountOfRows){
					System.err.println("Last button should have been disabled.");
				}else{
					int modResults = totalAmountOfRows % rowsToDisplay;
					if(modResults == 0){
						firstIndex = totalAmountOfRows - rowsToDisplay;
						lastIndex = totalAmountOfRows;
								
					}else{
						firstIndex = totalAmountOfRows - modResults;
						lastIndex = totalAmountOfRows;
					}
				}
				break;
			case NEXT:
				if(lastIndex  >= totalAmountOfRows){
					System.out.println("Last Index is " + lastIndex);
					System.out.println("Rows to display " + rowsToDisplay);
					System.out.println("Total Amoun of rows " + totalAmountOfRows);
					System.err.println("Next button should have been disabled.");
				}else{
					firstIndex = lastIndex;
					int indexCheck = lastIndex + rowsToDisplay;
					if(indexCheck <= totalAmountOfRows){
						lastIndex = indexCheck;
					}else{
						lastIndex = totalAmountOfRows;							
					}
					
				}
				break;
			case PREVIOUS:
				if(firstIndex <= 0){
					System.err.println("Previous button should have been disabled.");
				}else{
					int indexCheck = firstIndex - rowsToDisplay;
					if(indexCheck > 0){
						firstIndex = indexCheck;
						
						int lastIndexCheck = lastIndex - rowsToDisplay;
						if((lastIndexCheck - firstIndex) < rowsToDisplay){
							lastIndex =  lastIndexCheck + (rowsToDisplay - (lastIndexCheck - firstIndex));
						}else{
							lastIndex = lastIndexCheck;
						}
						
					}else{
						initiliazeIndexes();
					}				
					
					
				}
				break;
			default:
				break;
		}
		
		checkValidSelections();
		compilePagingInfo();
	}
	
	
	public void checkValidSelections(){
		if(firstIndex == 0){
			enableFirst = false;
			enablePrevious = false;
		}else{
			enableFirst = true;
			enablePrevious = true;
		}
		
		if(lastIndex == totalAmountOfRows){
			enableLast = false;
			enableNext = false;
		}else{
			enableLast = true;
			enableNext = true;
		}
		
		
	}
	
	public void compilePagingInfo(){
		StringBuffer pagingInfo = new StringBuffer("Results: ");
		pagingInfo.append((firstIndex + 1 ) + " - " + (lastIndex) + " of " + totalAmountOfRows);
		pagingInfo.append(" " + pagingInfoType);
		this.pagingInfo = pagingInfo.toString();
		
	}
	
	public void changeRowsToDispaly(int rowsToDisplay){
		this.rowsToDisplay = rowsToDisplay;
		this.initiliazeIndexes();
	}

	public boolean isShowColHeader() {
		return showColHeader;
	}

	public void setShowColHeader(boolean showColHeader) {
		this.showColHeader = showColHeader;
	}

	public int getRowsBeforeData() {
		return rowsBeforeData;
	}

	public void setRowsBeforeData(int rowsBeforeData) {
		this.rowsBeforeData = rowsBeforeData;
	}
	
}
