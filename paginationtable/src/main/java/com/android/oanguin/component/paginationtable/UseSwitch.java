/**
 * 
 */
package com.android.oanguin.component.paginationtable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author oanguin
 * This annotation will be used as an indicator for generic table to see if a switch should be displayed on the row.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UseSwitch {
	boolean enabled();
}
