package com.android.oanguin.component.paginationtable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SortedMethod {
	int value();
}
