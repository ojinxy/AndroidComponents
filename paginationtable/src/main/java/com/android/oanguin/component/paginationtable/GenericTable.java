package com.android.oanguin.component.paginationtable;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

/**
 * Creates a Dynamic Table layout that takes type T<?>
 * @author Ricardo Thompson
 * 
 */

public class GenericTable implements OnClickListener{
	
	Context activity;
	public TableLayout table;
	public List<GenericRow> genericRows;
	int otherRowColor;
	public int columnHeadersCount;
	GenericList genericList;
	public Button buttonNext,buttonPrevious,buttonFirst,buttonLast,buttonSort;
	public Spinner rowsToDisplayDropDown,sortByListDropDown;
	TextView pagingInfo, textAfterPaging;
	int paginationButtonColor,paginationButtonDisabledColor;
	OnClickListener rowListener;
	public static Typeface font;
		
	String sortValue;
	GenericSort.SORTORDER sortOrder;
	/**
	 * Creates a TableLayout that will take a variety of columns and column headers
	 * @param context Application context
	 * @param tableTitle The title that should be displayed on the table
	 * @param columnHeaders Array List of Strings containing the Titles for the columns
	 */
	public GenericTable(Context context,String tableTitle,ArrayList<String> columnHeaders,GenericList... genericList){
		
		boolean showColHeaders = true;
		font = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");

		this.activity = context;
		this.columnHeadersCount = columnHeaders.size();
		otherRowColor = Color.rgb(235, 245, 255);
		table = new TableLayout(context);
	    table.setPadding(0, 10, 10, 10);
	    
	    //Set Title text and attributes
		TableRow titleBar = new TableRow(activity);
		LinearLayout titleLayout = new LinearLayout(activity);
		titleLayout.setPadding(10, 0, 0, 0);
		titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		TextView titleText = new TextView(activity);
		titleText.setTextAppearance(activity, android.R.style.TextAppearance_Medium);
		titleBar.setBackgroundColor(Color.rgb(17, 129, 178));
		titleText.setTextColor(Color.WHITE);
		if(!StringUtils.isBlank(tableTitle))
			titleText.setText(tableTitle);
		titleLayout.addView(titleText);
		titleBar.addView(titleLayout);
		
		//Set Column Spans
		if(columnHeadersCount > 1){
			LayoutParams params = (LayoutParams) titleBar.getChildAt(0).getLayoutParams();
			params.span = columnHeadersCount;
			titleBar.getChildAt(0).setLayoutParams(params);
		}
		table.addView(titleBar);
		
		if(genericList != null && genericList.length > 0){
			

			this.genericList = genericList[0];
			
			showColHeaders = this.genericList.isShowColHeader();
			
		    //Set Pagination text and attributes
			TableRow paginationBar = new TableRow(activity);
			LinearLayout paginationLayout = new LinearLayout(activity);
			paginationLayout.setPadding(10, 0, 0, 0);
			paginationLayout.setOrientation(LinearLayout.HORIZONTAL);
			
			LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			buttonParams.setMargins(5, 0, 0, 0);
			buttonParams.leftMargin=10;
			int paginationButtonSize = 50;
			paginationButtonColor =  Color.parseColor("#87CEFA");
			paginationButtonDisabledColor =  Color.argb(100,181, 181, 182);
			buttonNext = new Button(activity);
			buttonNext.setText(R.string.next);
			buttonNext.setTypeface(font);
			buttonNext.setBackgroundResource(R.drawable.roundbutton);
			buttonNext.setOnClickListener(this);
			buttonNext.setLayoutParams(buttonParams);
			buttonNext.setTextColor(paginationButtonColor);
			buttonNext.setTextSize(paginationButtonSize);
			buttonPrevious = new Button(activity);
			buttonPrevious.setText(R.string.previous);
			buttonPrevious.setTypeface(font);
			buttonPrevious.setTextColor(ColorStateList.valueOf(paginationButtonColor));
			buttonPrevious.setTextSize(paginationButtonSize);
			buttonPrevious.setBackgroundResource(R.drawable.roundbutton);
			buttonPrevious.setLayoutParams(buttonParams);
			buttonPrevious.setOnClickListener(this);
			buttonFirst = new Button(activity);
			buttonFirst.setText(R.string.first);
			buttonFirst.setTypeface(font);
			buttonFirst.setBackgroundResource(R.drawable.roundbutton);
			buttonFirst.setOnClickListener(this);
			buttonFirst.setTextColor(paginationButtonColor);
			buttonFirst.setTextSize(paginationButtonSize);
			buttonLast = new Button(activity);
			buttonLast.setText(R.string.last);
			buttonLast.setTypeface(font);
			buttonLast.setBackgroundResource(R.drawable.roundbutton);
			buttonLast.setLayoutParams(buttonParams);
			buttonLast.setOnClickListener(this);
			buttonLast.setTextColor(paginationButtonColor);
			buttonLast.setTextSize(paginationButtonSize);
			pagingInfo = new TextView(context);
			pagingInfo.setTextSize(16);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
	                LinearLayout.LayoutParams.WRAP_CONTENT,
	                LinearLayout.LayoutParams.MATCH_PARENT);
			pagingInfo.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
			pagingInfo.setLayoutParams(lp);
			pagingInfo.setText(this.genericList.pagingInfo);
			rowsToDisplayDropDown = new Spinner(context);
			rowsToDisplayDropDown.setGravity(Gravity.CENTER_VERTICAL);
			ArrayAdapter<CharSequence> adapterRowsToDisplay = ArrayAdapter.createFromResource(context, R.array.demorowstodisplay, R.layout.simple_spinner_dropdown_item);
			rowsToDisplayDropDown.setAdapter(adapterRowsToDisplay);
			LinearLayout.LayoutParams rowsToDisplayParams = new LinearLayout.LayoutParams(100, LayoutParams.MATCH_PARENT);
			rowsToDisplayDropDown.setLayoutParams(rowsToDisplayParams);
			rowsToDisplayDropDown.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					GenericTable.this.genericList.changeRowsToDispaly(Integer.parseInt(rowsToDisplayDropDown.getItemAtPosition(position).toString()));
					paginationProcess(GenericTable.this.genericList.first());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
				}
				
			});
			TextView rowsToDisplayLabel = new TextView(context);
			rowsToDisplayLabel.setLayoutParams(lp);
			rowsToDisplayLabel.setGravity(Gravity.CENTER_VERTICAL);
			rowsToDisplayLabel.setPadding(10, 0, 0, 0);
			rowsToDisplayLabel.setText("Items: ");
			rowsToDisplayLabel.setTextSize(16);
			
			
			if(this.genericList.allowSorting){
				
				buttonSort = new Button(activity);
				buttonSort.setLayoutParams(lp);
				buttonSort.setGravity(Gravity.CENTER_VERTICAL);
				buttonSort.setText(R.string.sortdesc);
				buttonSort.setTypeface(font);
				buttonSort.setBackgroundResource(R.drawable.roundbutton);
				buttonSort.setOnClickListener(this);
				buttonSort.setTextColor(paginationButtonColor);
				buttonSort.setTextSize(25);
				
				sortByListDropDown = new Spinner(context);
				
				ArrayAdapter<CharSequence> adaptersortByList = ArrayAdapter.createFromResource(context, this.genericList.sortByArrayId, R.layout.simple_spinner_dropdown_item);
				sortByListDropDown.setAdapter(adaptersortByList);
				LinearLayout.LayoutParams sortByParams = new LinearLayout.LayoutParams(200, LayoutParams.MATCH_PARENT);
				
				sortByListDropDown.setLayoutParams(sortByParams);
				sortByListDropDown.setGravity(Gravity.CENTER_VERTICAL);
				
				sortOrder = GenericSort.SORTORDER.DESC;
				
				if(this.genericList.defaultSortOrder != null){
					sortOrder = this.genericList.defaultSortOrder;
					buttonSort.setText(sortOrder == GenericSort.SORTORDER.ASC?R.string.sortasc:R.string.sortdesc);
				}
				
				
				if(this.genericList.defaultSortType != null){
					int index;
					
					for(index = 0;index < adaptersortByList.getCount(); index ++){
						if(adaptersortByList.getItem(index).toString().equalsIgnoreCase(this.genericList.defaultSortType)){
							sortByListDropDown.setSelection(index);
							continue;
						}
					}
				}
				
				sortByListDropDown.setOnItemSelectedListener(new OnItemSelectedListener(){
	
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						sortValue = sortByListDropDown.getItemAtPosition(position).toString();
						paginationProcess(GenericTable.this.genericList.sort(sortOrder, 
								GenericTable.this.genericList.getFunctionNameForSorting(sortValue), GenericTable.this.genericList.getClassToBeUsedForReflection()));
						
					}
	
					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						
					}
					
				});
			}
			
			
			paginationLayout.addView(buttonFirst);
			paginationLayout.addView(buttonPrevious);
			paginationLayout.addView(pagingInfo);
			paginationLayout.addView(buttonNext);
			paginationLayout.addView(buttonLast);
			
			this.textAfterPaging = new TextView(context);
			this.textAfterPaging.setVisibility(TextView.GONE);
			this.textAfterPaging.setLayoutParams(lp);
			this.textAfterPaging.setGravity(Gravity.CENTER_VERTICAL);
			this.textAfterPaging.setTextSize(16);
			paginationLayout.addView(this.textAfterPaging);
			
			if(this.genericList.allowSorting){
				TextView sortByLabel = new TextView(context);
				sortByLabel.setLayoutParams(lp);
				sortByLabel.setGravity(Gravity.CENTER_VERTICAL);
				sortByLabel.setText("Sort By: ");
				sortByLabel.setTextSize(16);
				paginationLayout.addView(sortByLabel);
				paginationLayout.addView(sortByListDropDown);
			}
				
			
			
			if(this.genericList.allowSorting){
				paginationLayout.addView(buttonSort);
				paginationLayout.addView(rowsToDisplayLabel);
				paginationLayout.addView(rowsToDisplayDropDown);
			}
			paginationBar.addView(paginationLayout);
			
			//Set Column Spans
			if(columnHeadersCount > 1){
				LayoutParams params = (LayoutParams) paginationBar.getChildAt(0).getLayoutParams();
				params.span = columnHeadersCount;
				params.bottomMargin = 10;
				params.topMargin = 10;
				paginationBar.getChildAt(0).setLayoutParams(params);
			}
		
			table.addView(paginationBar);
			configurePaginationButtonsStatus();
		}
		
		//Set header Titles .i.e column headers can be less than(but greater than zero(0)) or equal to numOfColmns but not greater than numOfColmns 
		TableRow header = new TableRow(activity);
		header.setBackgroundColor(Color.rgb(203,236, 250));
		
		//Create Column Headers
		for(int x = 0; x<columnHeadersCount;x++){
		
			TextView textView = new TextView(activity);
			textView.setText(columnHeaders.get(x).toString());
			header.addView(textView);
			header.setPadding(10, 10, 0, 10);
			textView.setTextColor(Color.BLACK);
			textView.setTypeface(textView.getTypeface(),Typeface.BOLD);
			//textView.setPaintFlags(textView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
			textView.setTextSize(16);
			//initWidthDist += 100;
			
			
			//set column widths equally
			table.setColumnStretchable(x, true);
		}
		
		//conditionally add column header 
		if(showColHeaders){
			//add title and column headings to table
			table.addView(header);
		}
		
		//Instantiate rows
		genericRows = new ArrayList<GenericRow>();
		
		
	}
	
	/**
	 * Adds a row to the Generic table 
	 * @param genericObject Can be any Object with values to be extracted to the table rows E.g. Person()
	 * @param methodNames Is a list of strings containing the names of the methods or properties that the values are to be pull from. E.g. "getPersonName", "getValue" 
	 */
	public void addRow(Object genericObject,ArrayList<String> methodNames){
		
		boolean evenRow = false;
		//Decides of a row is even or odd
		if(table != null && table.getChildCount() % 2 == 0){
			evenRow = true;
		}
		//Creates instance of generic row
		GenericRow genericRow = new GenericRow(this.activity, this, evenRow, genericObject, methodNames);
		//Adds a row to a list of rows
		genericRows.add(genericRow);
		//Adds row to table layout
		table.addView(genericRow.row);
		//Turn off clickable rows
		genericRow.row.setClickable(false);
	}
	
	
	public void addRow(View view){
		TableRow row = new TableRow(this.activity);
		row.addView(view);
		table.addView(row);

	}
	
	/**
	 * Adds a row to the Generic table with a onClickListener
	 * @param genericObject Can be any Object with values to be extracted to the table rows E.g. Person()
	 * @param methodNames Is a list of strings containing the names of the methods or properties that the values are to be pull from. E.g. "getPersonName", "getValue" 
	 */
	public void addRow(Object genericObject,ArrayList<String> methodNames, OnClickListener clickListener){
		
		boolean evenRow = false;
		//Decides of a row is even or odd
		if(table != null && table.getChildCount() % 2 == 0){
			evenRow = true;
		}
		//Creates instance of generic row
		GenericRow genericRow = new GenericRow(this.activity, this, evenRow, genericObject, methodNames);
		//Adds a row to a list of rows
		
		genericRow.row.setClickable(true);
		
		genericRow.row.setOnClickListener(clickListener);
		
		
		genericRows.add(genericRow);
		//Adds row to table layout
		table.addView(genericRow.row);
		//Turn off clickable rows
		
				
	}
	
	
	/**
	 * Colours each row whether even or odd 
	 */
	public void recolorRows(){
		int i = 0;
		while(i < table.getChildCount()){
			if(i > 2 && !(i % 2 == 0)){
				table.getChildAt(i).setBackgroundColor(otherRowColor);
			}
			
			i++;
		}
	}

	@Override
	public void onClick(View v) {
		
				
		if(v.equals(buttonNext)){
			this.paginationProcess(genericList.next());
			configurePaginationButtonsStatus();
			this.pagingInfo.setText(genericList.pagingInfo);
		}else if(v.equals(buttonPrevious)){
			this.paginationProcess(genericList.previous());
			configurePaginationButtonsStatus();
			this.pagingInfo.setText(genericList.pagingInfo);
		}else if(v.equals(buttonFirst)){
			this.paginationProcess(genericList.first());
			configurePaginationButtonsStatus();
			this.pagingInfo.setText(genericList.pagingInfo);
		}else if(v.equals(buttonLast)){
			this.paginationProcess(genericList.last());
			configurePaginationButtonsStatus();
			this.pagingInfo.setText(genericList.pagingInfo);
		}else if(v.equals(buttonSort)){
			sortOrder = sortOrder == GenericSort.SORTORDER.DESC ? GenericSort.SORTORDER.ASC : GenericSort.SORTORDER.DESC;  
			buttonSort.setText(sortOrder == GenericSort.SORTORDER.DESC ? R.string.sortdesc : R.string.sortasc);
			paginationProcess(GenericTable.this.genericList.sort(sortOrder, 
					GenericTable.this.genericList.getFunctionNameForSorting(sortValue), GenericTable.this.genericList.getClassToBeUsedForReflection()));
		}
		
	}
	
	public void configurePaginationButtonsStatus(){
		if(genericList == null){
			return;
		}else{
			
			this.buttonFirst.setEnabled(genericList.isEnableFirst());
			this.buttonFirst.setTextColor(genericList.isEnableFirst() ? paginationButtonColor : paginationButtonDisabledColor);
			this.buttonNext.setEnabled(genericList.isEnableNext());
			this.buttonNext.setTextColor(genericList.isEnableNext() ? paginationButtonColor : paginationButtonDisabledColor);
			this.buttonPrevious.setEnabled(genericList.isEnablePrevious());
			this.buttonPrevious.setTextColor(genericList.isEnablePrevious() ? paginationButtonColor : paginationButtonDisabledColor);
			this.buttonLast.setEnabled(genericList.isEnableLast());
			this.buttonLast.setTextColor(genericList.isEnableLast() ? paginationButtonColor : paginationButtonDisabledColor);
		
			
		}
	}
	
	private void paginationProcess(List<Object> newRows){
		this.removeAllDataRowsAfterPagination(genericList.getRowsBeforeData());
		boolean recolorRows = true;
		for(Object row : newRows){
			if(row instanceof View){
				recolorRows = false;
				for(Object view :  genericList.genericList){
					if((ViewGroup)((View)view).getParent() != null){
						((ViewGroup)((View)view).getParent()).removeView(((View)view));
					}
				}
				addRow((View)row);
			}
			else if(rowListener != null){
				addRow(row,(ArrayList<String>)genericList.getMethodNames(),rowListener);
			}else{
				addRow(row,(ArrayList<String>)genericList.getMethodNames());
			}
			
		}
		configurePaginationButtonsStatus();
		this.pagingInfo.setText(genericList.pagingInfo);
		
		
		if(recolorRows){
			recolorRows();
		}
	}
	
	public void removeAllDataRowsAfterPagination(int rowsBeforeData){
		this.table.removeViews(rowsBeforeData, this.table.getChildCount() -rowsBeforeData);
	}

	
	public OnClickListener getRowListener() {
		return rowListener;
	}

	public void setRowListener(OnClickListener rowListener) {
		this.rowListener = rowListener;
	}
	
}
