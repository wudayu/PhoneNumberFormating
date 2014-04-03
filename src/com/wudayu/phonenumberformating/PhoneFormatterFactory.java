package com.wudayu.phonenumberformating;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.telephony.PhoneNumberUtils;

/**
 * 电话号码格式工厂
 * 
 * @author David Wu
 * 
 * @note
 * 	本工厂提供不同国家的号码格式化器 以及其他与号码相关的静态方法
 */
public class PhoneFormatterFactory {

	public static final String CN = "+86";

	private static PhoneFormatterFactory instance = null;

	private PhoneFormatterFactory() {

	}

	public static PhoneFormatter getFormmater(String countryCode) {
		if (instance == null) {
			instance = new PhoneFormatterFactory();
		}

		if (countryCode != null && countryCode.charAt(0) != '+')
			countryCode = '+' + countryCode;

		if ("+86".equals(countryCode))
			return instance.new CNPhoneFormatter();
		// TODO 继续添加其他国家Formatter

		// 默认通用Formatter，也就是 PhoneNumberUtils
		return instance.new UniversalPhoneFormatter(countryCode);
	}

	/**
	 * 电话号码格式工厂接口
	 * 
	 */
	public interface PhoneFormatter {
		String format(String phoneNumber);
	}

	/**
	 * Formatter 国际通用PhoneNumberUtils
	 * 
	 */
	public class UniversalPhoneFormatter implements PhoneFormatter {
		String countryCode;

		public UniversalPhoneFormatter(String countryCode) {
			this.countryCode = countryCode;
		}

		@Override
		public String format(String phoneNumber) {
			if (countryCode == null)
				return formatNumberOnlyDigits(phoneNumber);

			return PhoneNumberUtils.formatNumber(countryCode + phoneNumber)
					.replaceAll("\\" + countryCode, "");
		}
	}

	/**
	 * Formatter 中国号码
	 * 
	 */
	public class CNPhoneFormatter implements PhoneFormatter {
		@Override
		public String format(String phoneNumber) {
			StringBuffer sb = new StringBuffer(
					formatNumberOnlyDigits(phoneNumber));

			if (sb.length() > 7)
				sb.insert(7, ' ');

			if (sb.length() > 3)
				sb.insert(3, ' ');

			String ret = sb.toString().trim();

			return ret.length() > 13 ? ret.substring(0, 13) : ret;
		}
	}

	/**
	 * 修改号码以方便内部存储或更改格式 仅包含数字的号码格式
	 * 
	 * @param number
	 * @return 纯数字的号码
	 */
	public static String formatNumberOnlyDigits(String number) {
		return number.replaceAll("[^0-9]", "");
	}

	/**
	 * 获取四位国家区号 <br>
	 * 不足四位，前面补零
	 * 
	 * @param code
	 * @return
	 */
	public static String getCountryCodeDesc(String code) {
		if (code.contains("+")) {
			code = code.replace("+", "");
		}
		int length = code.length();
		if (length == 4) {
			return code;
		}
		int delta = 4 - length;
		for (int i = 0; i < delta; i++) {
			code = "0" + code;
		}
		return code;
	}

	/**
	 * 从国家区号描述，解析数字<br>
	 * 0086 -> 86
	 * 
	 * @param codeDesc
	 * @return
	 */
	public static String getCountryCode(String codeDesc) {
		if (codeDesc.startsWith("0")) {
			return getCountryCode(codeDesc.substring(1));
		} else {
			return codeDesc;
		}
	}

	/**
	 * 判断是否是合法的手机号码
	 * 
	 * @note 如果运营商发布新号段，需要更新该方法
	 * @param phone
	 * @return
	 */
	public static boolean isValidMobilePhoneNumber(String phone) {
		Pattern pattern = Pattern
				.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
		Matcher mc = pattern.matcher(phone);
		return mc.matches();
	}

	/**
	 * 判断是否是合法的固定电话号码
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isValidPhoneNumber(String phone) {
		Pattern pattern = Pattern
				.compile("^(\\(\\d{3,4}-)|(\\d{3,4}-)?\\d{7,8}$");
		Matcher mc = pattern.matcher(phone);
		return mc.matches();
	}
}