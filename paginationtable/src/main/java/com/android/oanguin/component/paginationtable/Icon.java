/**
 * 
 */
package com.android.oanguin.component.paginationtable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author oanguin
 * This annotation will be used as an indicator for generic table should use an Icon. 
 * When this annotation is on the function the text view will be used with font awesome fonts to display 
 * icons in places of escape code such as the following <b>&#xf01d;</b>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Icon {
	String fonttype();
	float fontsize();
	String fontcolor();
}
