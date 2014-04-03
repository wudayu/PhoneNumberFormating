package com.wudayu.phonenumberformating;

import android.test.AndroidTestCase;

public class JUnitTest extends AndroidTestCase {

	public void test() {
		System.out.println(PhoneFormatterFactory.getFormmater("81").format("18651817673"));
	}
}
