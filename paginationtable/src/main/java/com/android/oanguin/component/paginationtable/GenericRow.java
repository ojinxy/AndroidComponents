package com.android.oanguin.component.paginationtable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;

/**
 * Creates a Dynamic Table rows that takes type T<?>
 * @author Ricardo Thompson
 *
 */
public class GenericRow {

	GenericTable genericTable;
	Object genericObject;
	public boolean evenRow; 
	public TableRow row;
	TextView textView;
	ImageView tick;
	public enum TAGS{
		SWITCH;
	}
	
	/**
	 * Creates Dynamic rows that are to be added to table layout. This is achieved using reflections.
	 * @param activity Application Activity
	 * @param genericTable Instance of Generic Table class
	 * @param evenRow Boolean of whether row is even or odd
	 * @param genericObject Any object that has values to be added to table layout
	 * @param methodNames List of Strings stating the names of the methods or properties to retrieve their values
	 */
	
	public GenericRow(Context activity,final GenericTable genericTable,boolean evenRow,final Object genericObject,ArrayList<String> methodNames){
		
		this.genericTable = genericTable;
		this.genericObject = genericObject;
		this.evenRow = evenRow;
		this.row = new TableRow(activity);
		//Gets the number of columns that were created
		int columnCount = genericTable.columnHeadersCount;
		
		//List to store the confirmed method names
		ArrayList<String> selectedGenericMethodNames = new ArrayList<String>();
		
		try {
			//Get the full name of the Class entered and make if or type Class
			final Class<?> genericClass = Class.forName(genericObject.getClass().getName());
			//Retrieve a list of all methods within the above class
			//modified to use methods on the main class only 
			Method[] methods = genericClass.getDeclaredMethods();
			
			//Collections.sort(methods);
			Arrays.sort(methods,new Comparator<Method>() {

				@Override
				public int compare(Method lhs, Method rhs) {
					SortedMethod sm1 = lhs.getAnnotation(SortedMethod.class);
					SortedMethod sm2 = rhs.getAnnotation(SortedMethod.class);
					
					if (sm1 != null && sm2 != null) {
		                    return sm1.value() - sm2.value();
		                } else
		                if (sm1 != null && sm2 == null) {
		                    return -1;
		                } else
		                if (sm1 == null && sm2 != null) {
		                    return 1;
		                }
						if(sm1 == null && sm2 == null)
							return 2;
		           return ((Integer)(sm1.value())).compareTo(sm2.value());
				}
			});
			
			for (Method method : methods) {
				//confirm that the methods or properties within the list actually exists in the Class
				if(methodNames.contains(method.getName()))
					selectedGenericMethodNames.add(method.getName());
			}
			
			//Checks that there isnt more columns that are suppose to exist
			if(!isSelectedLargerThanColmnCount(selectedGenericMethodNames.size(),columnCount)){
				for(int x = 0; x < columnCount; x++){
					//get the method from the list of confirmed methods
					Method m = genericClass.getMethod(selectedGenericMethodNames.get(x));
					
					//remove method restrictions
					m.setAccessible(true);
					String result = "";
					//invoke the use of the method
					Object resultObject = m.invoke(genericObject, null);
					
					/*The part of the if class checking for attended is used where the invoke function value returns null
					 * and and attendance switch needs to be created.*/
					if(m.getName().toLowerCase().contains("attended") && resultObject == null){
						resultObject = new Boolean(false);
					}
					
					LinearLayout columnLayout = new LinearLayout(activity);
					columnLayout.setOrientation(LinearLayout.HORIZONTAL);
					columnLayout.setPadding(0, 0, 10, 0);
					
					//Checks if value returned is of Type Boolean
					if (resultObject instanceof Boolean) {
						if(m.isAnnotationPresent(UseSwitch.class)){
							final Switch switchAttended = new Switch(activity);
							switchAttended.setTrackResource(R.drawable.switch_track_selector);
							switchAttended.setThumbResource(R.drawable.switch_thumb_selector);
							switchAttended.setTextOff("NO");
							switchAttended.setTextOn("YES");
							switchAttended.setChecked((Boolean)resultObject);							
							switchAttended.setSwitchTextAppearance(activity, R.style.switchStyle);
							switchAttended.setTag(TAGS.SWITCH);
							columnLayout.addView(switchAttended);
							switchAttended.setEnabled(((UseSwitch)m.getAnnotation(UseSwitch.class)).enabled());
							
							/*Get Attended Comment*/
							Method mComment = genericClass.getMethod("getAttendedComment");
							mComment.setAccessible(true);
							Object objectComment = mComment.invoke(genericObject, null);
							String comment = "";
							if(objectComment != null){
								comment = objectComment.toString();
							}
							
							final EditText editText = new EditText(activity);						
							editText.setText(comment);
							LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
							editText.setLayoutParams(lparams);
							editText.setSingleLine(false);
							editText.setMaxWidth(20);
							InputFilter[] filters = new InputFilter[1];
							filters[0] = new InputFilter.LengthFilter(200); //Filter to 10 characters
							editText .setFilters(filters);
							editText.setHint("Comment");
							columnLayout.addView(editText);
							
							/*TODO It may be possible to make this aspect more generic by passing in the input set methods along with their parameters.*/
							switchAttended.setOnCheckedChangeListener(new OnCheckedChangeListener() {
								
								@Override
								public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
									
									try {
										Method m = genericClass.getMethod("setAttended",Boolean.class,String.class);
										m.setAccessible(true);
										m.invoke(genericObject, switchAttended.isChecked(),editText.getText().toString());
									} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
										e.printStackTrace();
									}
								}
							});
							
							editText.addTextChangedListener(new TextWatcher() {
								
								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									
								}
								
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count,
										int after) {
								}
								
								@Override
								public void afterTextChanged(Editable s) {
									try{
										Method m = genericClass.getMethod("setAttended",Boolean.class,String.class);
										m.setAccessible(true);
										m.invoke(genericObject, switchAttended.isChecked(),s.toString());
									} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
										e.printStackTrace();
									}
									
								}
							});
							
						}else{
							result = Boolean.toString((Boolean) resultObject);
							if(result.equalsIgnoreCase("false")){
								result = "";
								textView = new TextView(activity);
								textView.setMaxWidth(250);
								textView.setSingleLine(false);
								textView.setEllipsize(TruncateAt.MARQUEE);
								textView.setText(result);
								textView.setPadding(30, 0, 0, 0);
								columnLayout.addView(textView);
							}else{
								tick = new ImageView(activity);
								tick.setImageResource(R.drawable.tick16x17);
								tick.setMaxWidth(250);
								tick.setPadding(30, 0, 0, 0);
								columnLayout.addView(tick);
							}
						}
						
					}else if(m.isAnnotationPresent(Icon.class) && resultObject != null){
						String fontType = ((Icon)m.getAnnotation(Icon.class)).fonttype();
						result = (String)resultObject.toString();
						textView = new TextView(activity);
						//textView.setTypeface(Typeface.createFromAsset( activity.getAssets(), fontType ));
						textView.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/" + fontType));
						textView.setTextSize(((Icon)m.getAnnotation(Icon.class)).fontsize());
						
						String textColor = " ";
						try{
							Method iconMethod = genericClass.getMethod("getIconColor");						
							iconMethod.setAccessible(true);
							textColor = (String)iconMethod.invoke(genericObject, null);
							
						}catch(NoSuchMethodException e){
							textColor = ((Icon)m.getAnnotation(Icon.class)).fontcolor();
							
						}catch(Exception e){
							textColor = null;
						}
						
						if(StringUtils.isNotBlank(textColor)){
							int color = Color.parseColor(textColor);
							textView.setTextColor(color);
						}
						
						
						/*if(result.toLowerCase().contains("start")){
							textView.setTextColor(Color.parseColor("#00FF7F"));
						}else if(result.toLowerCase().contains("comp")){
							textView.setTextColor(Color.BLUE);
						}*/
						textView.setText(Html.fromHtml(result));
						textView.setPadding(0, 10, 0, 0);
						columnLayout.addView(textView);
					}
					else{
						if(resultObject != null)
						{result = (String)resultObject.toString();
							textView = new TextView(activity);
							//textView.setMaxWidth(250);
							textView.setSingleLine(false);
							textView.setEllipsize(TruncateAt.MARQUEE);
							textView.setPadding(10, 0, 0, 0);
							textView.setText(Html.fromHtml(result));//added to recognise HTML tags
							columnLayout.addView(textView);
						}
					}
					//add row to layout
					row.addView(columnLayout);
					//colour row background
					if(!evenRow){
						row.setBackgroundColor(genericTable.otherRowColor);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This function takes a view object and displays the data as a row.
	 * @param activity
	 * @param genericTable
	 * @param view
	 */
	public GenericRow(Context activity,final GenericTable genericTable,View view){
		this.row = new TableRow(activity);
		this.row.addView(view);
	}
	
	public boolean isSelectedLargerThanColmnCount(int selectedMethodNames,int headerCounts){
		
		if(selectedMethodNames > 0 && ( selectedMethodNames < headerCounts || selectedMethodNames == headerCounts))
			return false;
		
		return true;
	}

	public Object getGenericObject() {
		return genericObject;
	}

	public void setGenericObject(Object genericObject) {
		this.genericObject = genericObject;
	}
}
