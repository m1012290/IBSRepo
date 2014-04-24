/**
 * 
 */
package com.shrinfo.ibs.cmn.utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shrinfo.ibs.cmn.constants.CommonErrorKeys;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;


/**
 * This class has general utility methods for use across all layers.
 */
public class Utils {

	private Utils() {} /* To restrict instantiation of this class. */

	private static final Logger logger = Logger.getLogger(Utils.class);

	private static ApplicationContext appContext;

	private static final String appConfigPropertiesFile = "config/appconfig.properties";

	private static final String errorMessagePropertiesFile = "config/messages.properties";


	/**
	 * Returns true if collection is refering to null or the collection is empty
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		boolean target = true;
		if (collection != null) {
			if (!collection.isEmpty()) {
				target = false;
			}
		}
		return target;
	}

	/**
	 * Returns true if map is refering to null or the map is empty
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		boolean target = true;
		if (map != null) {
			if (!map.isEmpty()) {
				target = false;
			}
		}
		return target;
	}

	/**
	 * Returns true if object is refering to null
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		boolean target = true;
		if (object != null) {

			target = false;
		}
		return target;
	}

	/**
	 * Returns true if the size of object array or the objects equals to null
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean isEmpty(Object[] objects) {
		boolean target = true;
		if (objects != null) {
			if (objects.length != 0) {
				target = false;
			}
		}
		return target;
	}

	/**
	 * Returns true if string references to null or the string is empty
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		boolean target = true;
		if (string != null) {
			if (string.length() != 0) {
				target = false;
			}
		}
		return target;
	}

	/**
	 * Returns true if string references to null or the string is empty
	 * 
	 * @param string The string to be checked for emptiness
	 * @param trimBeforeCheck A boolean indicating if the String should be trimmed before checking
	 *        for emptiness. If true, then the String is trimmed, else it is equivalent to calling
	 *        <code>isEmpty( String )</code>.
	 * 
	 * @return true, if null or of 0 length
	 */
	public static boolean isEmpty(String string, boolean trimBeforeCheck) {
		boolean target = true;

		if (trimBeforeCheck) {
			if (string != null) {
				if (string.trim().length() != 0) {
					target = false;
				}
			}
		} else {
			target = isEmpty(string);
		}
		return target;
	}

	/**
	 * Trims all elements in the passed String array.
	 * 
	 * @param array
	 */
	public static void trimAllEntries(String[] array) {
		if (Utils.isEmpty(array)) {
			return;
		}

		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
	}

	public static boolean toDefaultFalseBoolean(String input) {
		boolean convertedResult = false;

		if ("YES".equalsIgnoreCase(input) || "Y".equalsIgnoreCase(input)
				|| "TRUE".equalsIgnoreCase(input)) {
			convertedResult = true;
		}

		return convertedResult;
	}

	/**
	 * This method can be used to ensure that null strings are converted to empty, so that there is
	 * no case of a null object being processed upon.
	 * 
	 * @param input
	 * @return Empty string if <code>null</code> is passed, else the passed string
	 */
	public static String toNonNullString(String input) {
		String retString = input;
		if (isEmpty(input)) {
			retString = "";
		}

		return retString;
	}

	/**
	 * Formats a String in the specified format from the passed date.
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateAsString(Date date, String format) {
		if (isEmpty(date)) {
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * Checks if the passed string is numeric in nature.
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNumber(String num) {
		boolean isNumber = true;
		try {
			java.text.NumberFormat nf = new java.text.DecimalFormat();
			nf.parse(num);
		} catch (java.text.ParseException e) {
			isNumber = false;
		}

		return isNumber;
	}

	/**
	 * Checks if the passed string is numeric in nature.
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNonZeroNumber(String num) {
		boolean isNonZeroNumber = true;
		try {
			java.text.NumberFormat nf = new java.text.DecimalFormat();
			Number number = nf.parse(num);
			if(0 == number.doubleValue()) {
				isNonZeroNumber = false;
			}
		} catch (java.text.ParseException e) {
			isNonZeroNumber = false;
		}
		return isNonZeroNumber;
	}

	/**
	 * A convenience method to concatenate multiple strings without the use of the '+' operator.
	 * 
	 * @param strings
	 * @return
	 */
	public static String concat(String... strings) {
		if (Utils.isEmpty(strings)) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		for (String s : strings) {
			sb.append(s);
		}

		return sb.toString();
	}

	/* TODO getSingleValueAppSpecificConfig() to be implemented */
	public static String getSingleValueAppConfig(String key) {
		return Configurations.INSTANCE.getProperty(key, appConfigPropertiesFile);
	}

	/**
	 * Returns an app config value for the passed key. If the key is not found or is empty,
	 * <code>default</code> is returned.
	 * 
	 * @param key The key for which the value is required
	 * @param defaultValue The default value to be used if the key is not present or is set to empty
	 *        string
	 * 
	 * @return
	 */
	public static String getSingleValueAppConfig(String key, String defaultValue) {
		String value = getSingleValueAppConfig(key);
		if (Utils.isEmpty(value)) {
			value = defaultValue;
		}

		return value;
	}

	/**
	 * Returns an app config value split at ','. This can be used by all app config entries that can
	 * have multiple values that don't use ',' in their values. In most cases, this kind of
	 * configuration works. However, if ',' is expected to be part of the value itself, then
	 * consider using the overloaded method <code>getMultiValueAppConfig( String, String )</code>
	 * which takes a separator.
	 * 
	 * @param key
	 * @return
	 */
	public static String[] getMultiValueAppConfig(String key) {
		return Configurations.INSTANCE.getMultiValuedProperty(key, appConfigPropertiesFile);
	}

	/**
	 * Returns an app config value split at the specified <code>separator</code>. This can be used
	 * by all app config entries that can have multiple values that may have ',' in their values.
	 * 
	 * @param key The key for the property
	 * @param separator The separator that should be used to split the value configured
	 * @return
	 */
	public static String[] getMultiValueAppConfig(String key, String separator) {
		return Configurations.INSTANCE.getMultiValuedProperty(key, appConfigPropertiesFile,
				separator);
	}

	/**
	 * Returns an app config value split at ',' and '#' and transformed into a Map. This can be used
	 * by all app config entries which will not have ',' or '#' in their values. This will work in
	 * most cases. However, if a different pair of separator is required, consider using the
	 * overloaded method <code>getMultiValueAppConfigAsMap( String, String, String )</code>.
	 * 
	 * @param key The key for the property
	 * @return
	 */
	public static Map<String, String> getMultiValueAppConfigAsMap(String key) {
		return getMultiValueAppConfigAsMap(key, ",", "#");
	}

	/**
	 * Returns an app config value split at the specified <code>separator</code>. This can be used
	 * by all app config entries that can have multiple values that may have ',' in their values.
	 * 
	 * @param key The key for the property
	 * @param multiValueSeparator The multi-value separator that should be used to split the value
	 *        configured into name-value pairs
	 * @param nvPairSeparator The separator for the name-value pair
	 * 
	 * @return
	 */
	public static Map<String, String> getMultiValueAppConfigAsMap(String key,
			String multiValueSeparator, String nvPairSeparator) {

		Map<String, String> configAsMap = null;

		String[] multiValues =
				Configurations.INSTANCE.getMultiValuedProperty(key, appConfigPropertiesFile,
						multiValueSeparator);
		if (!Utils.isEmpty(multiValues)) {
			configAsMap = new HashMap<String, String>();

			for (String value : multiValues) {
				String[] nvPair = value.split(nvPairSeparator);
				String mapValue = (nvPair.length > 1 ? nvPair[1] : null);
				configAsMap.put(nvPair[0], mapValue);
			}
		}

		return configAsMap;
	}

	public static String getAppErrorMessage(String errorKey) {
		return Configurations.INSTANCE.getProperty(errorKey, errorMessagePropertiesFile);
	}

	/**
	 * Loads spring context and returns ApplicationContext type
	 * 
	 * @param appContextPath
	 * @return
	 */
	public static ApplicationContext loadSpringBeansFactory(String appContextPath) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(appContextPath);
		return context;
	}

	/**
	 * This method sets Spring application context for use in the Utils method.
	 * 
	 * @param context The Spring application context expected to have been loaded by the web
	 *        container.
	 */
	public static <T extends ApplicationContext> void setAppContext(T context) {
		appContext = context;
	}


	/**
	 * This method returns the bean instance based on the bean name passed.
	 * 
	 * @return
	 */
	public static Object getBean(String beanName) {
		Object beanInstance = null;
		if (Utils.isEmpty(beanName)) {
			throw new SystemException(CommonErrorKeys.UNKNOWN_ERROR, null,
					"BeanName should not be empty");
		}
		// if( Utils.isEmpty( appContext ) || Utils.isEmpty( appContext.get() ) ){
		if (Utils.isEmpty(appContext)) {
			throw new SystemException(CommonErrorKeys.UNKNOWN_ERROR, null,
					"applicationContext.xml is not loaded");
		}
		beanInstance = appContext.getBean(beanName);

		return beanInstance;
	}


	public static URL getClassPathResourceAsURL(String resource) {
		URL resourceURL = null;

		if (!Utils.isEmpty(resource)) {
			resourceURL = Utils.class.getClassLoader().getResource(resource);
		}
		return resourceURL;
	}

	/**
	 * Loads the class specified the passed fully qualified name. The class is loaded using the
	 * ClassLoader that loaded Utils.class.
	 * 
	 * @param <T>
	 * @param className Fully qualified name of the class to be loaded.
	 * @return
	 */
	public static <T> Class<T> loadClass(String className) {
		Class<T> clazz = null;
		try {
			clazz = (Class<T>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new SystemException(CommonErrorKeys.UNKNOWN_ERROR, e, "Class with name [",
					className, "] not found");
		}

		return clazz;
	}

	/**
	 * Checks if <code>childClass</code> is a child class of <code>superClass</code>.
	 * 
	 * @param <T>
	 * @param <U>
	 * @param childClass Class representing the child class
	 * @param superClass Class representing the super class
	 * @return
	 */
	public static <T, U> boolean isSubClassOf(Class<T> childClass, Class<U> superClass) {
		boolean isChildClass = false;
		if (Utils.isEmpty(childClass) || Utils.isEmpty(superClass)) {
			return isChildClass;
		}

		try {
			childClass.asSubclass(superClass);
			isChildClass = true;
		} catch (ClassCastException e) {}

		return isChildClass;
	}

	/**
	 * Returns a new instance of the class as indicated by the passed string
	 * 
	 * @param classNameAsString Fully qualified name of the class of which a new instance is
	 *        expected
	 * @return
	 * @throws SystemException if class could not be loaded
	 */
	public static Object newInstance(String classNameAsString) throws SystemException {
		return newInstance(classNameAsString, 0);
	}

	/**
	 * Returns a new instance of the class as indicated by the passed string
	 * 
	 * @param classNameAsString Fully qualified name of the class of which a new instance is
	 *        expected
	 * @param numElems The number of elements that need to be instantiated and added to the
	 *        collection or array (if the passed class is that of a collection or an array)
	 * @return
	 * @throws SystemException if class could not be loaded
	 */
	public static Object newInstance(String classNameAsString, int numElems) throws SystemException {
		Object instance = null;

		try {
			if (classNameAsString != null) {
				Class<?> clazz = Class.forName(classNameAsString);
				instance = newInstance(clazz, numElems);
			}
		} catch (ClassNotFoundException e) {
			throw new SystemException(CommonErrorKeys.CLASS_NOT_FOUND, e, classNameAsString,
					" is not a qualified class name");
		}
		return instance;
	}

	/**
	 * returns instance of the passed class
	 * 
	 * @param clazz only the name of the class
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz) throws SystemException {
		return newInstance(clazz, 0);
	}

	/**
	 * returns instance of the passed class
	 * 
	 * @param clazz only the name of the class
	 * @param numElems The number of elements that need to be instantiated and added to the
	 *        collection or array (if the passed class is that of a collection or an array)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> clazz, int numElems) throws SystemException {
		T instance = null;
		try {
			if (clazz != null) {
				instance = (T) checkAndInstantiateForSpecialCase(clazz, numElems);

				if (Utils.isEmpty(instance)) {
					instance = clazz.newInstance();
				}
			}
		} catch (InstantiationException e) {
			throw new SystemException("cmn.instantiationFail", e, "instantiation of class failed");
		} catch (IllegalAccessException e) {
			throw new SystemException("cmn.illegalMethod", e, "illegal excess of methods");
		}
		return instance;
	}

	/**
	 * Creates an instance of a class using a non-default constructor.
	 * 
	 * @param <T>
	 * @param clazz
	 * @param constructorArgs
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz, Object... constructorArgs) {
		T instance = null;
		try {
			instance =
					clazz.getConstructor(getClassArrayForObjects(constructorArgs)).newInstance(
							constructorArgs);
		} catch (Exception e) {
			if (logger.isError()) {
				logger.error(e, "Error while trying to instantiate class [", clazz.getName(), "]");
			}
		}

		return instance;
	}

	private static Class[] getClassArrayForObjects(Object... objects) {
		if (Utils.isEmpty(objects)) {
			return null;
		}

		Class[] classArray = new Class[objects.length];
		int index = 0;
		for (Object o : objects) {
			classArray[index] = o.getClass();
			index++;
		}

		return classArray;
	}

	private static Object checkAndInstantiateForSpecialCase(Class<?> clazz, int numElems) {
		Object instance = null;
		String className = clazz.getName();

		if (clazz.isArray()) {
			Class c = clazz.getComponentType();
			instance = Array.newInstance(c, numElems); // TODO We have to figure out a way to
			// mention size.
		} else if (isSubClassOf(clazz, java.util.Map.class)) {
			instance = new java.util.HashMap();
		} else if (isSubClassOf(clazz, java.util.List.class)) {
			instance = new java.util.ArrayList();
		} else if (isSubClassOf(clazz, java.util.Set.class)) {
			instance = new java.util.HashSet();
		} else if (className.equals("java.lang.Integer")) {
			instance = new java.lang.Integer(-9999); /*
			 * Since we don't have an input value and
			 * java.lang.Integer requires an initial value,
			 * we are setting to a default value, -9999.
			 * There are risks involved here which will
			 * need to be taken care of after the usage of
			 * the method.
			 */
		} else if (className.equals("java.lang.Long")) {
			instance = new java.lang.Long(-9999L); /*
			 * Since we don't have an input value and
			 * java.lang.Long requires an initial value, we
			 * are setting to a default value, -9999. There
			 * are risks involved here which will need to be
			 * taken care of after the usage of the method.
			 */
		} else if (className.equals("java.lang.Double")) {
			instance = new java.lang.Double(-9999.0);/*
			 * Since we don't have an input value and
			 * java.lang.Double requires an initial value,
			 * we are setting to a default value, -9999.
			 * There are risks involved here which will
			 * need to be taken care of after the usage of
			 * the method.
			 */
		}

		return instance;
	}

	/**
	 * This method returns an actual parametrized type from the passed class, which is the child of
	 * a generic class. <code>index</code> refers to the position in the order in which the generic
	 * types were declared in the class.
	 * 
	 * @param <T> Any type which has a generic super class
	 * @param genericClass An instance of java.lang.Class for <code>T</code>
	 * @param index The position in the order in which the generic types were declared in the super
	 *        type of T
	 * 
	 * @return
	 */
	public static <T> Class<?> getParametrizedType(Class<T> genericClass, int index) {
		Class<?> parametrizedType = null;

		try {
			ParameterizedType pType = (ParameterizedType) genericClass.getGenericSuperclass();

			Type[] pTypes = pType.getActualTypeArguments();

			if (!Utils.isEmpty(pTypes) && (pTypes.length > index)) {
				parametrizedType = (Class) pTypes[index];
			}
		} catch (Exception e) {
			if (logger.isError()) {
				logger.error(e, "Exception when looking for actual types in the generic class ");
			}
		}

		return parametrizedType;
	}

	/**
	 * Checks if the passed class is abstract. Returns <code>true</code>, if yes; <code>false</code>
	 * , otherwise. Please note that it will return <code>true</code> even for interfaces.
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isClassAbstract(Class<?> clazz) {
		if (Utils.isEmpty(clazz)) {
			return false;
		}
		return clazz.getModifiers() >= java.lang.reflect.Modifier.ABSTRACT ? true : false;
	}

	/**
	 * A convenience method to invoke a method through reflection. Catches all the JDK exceptions
	 * and throws SystemException.
	 * 
	 * @param method The method to be invoked
	 * @param onWhat The object on which the method has to be invoked
	 * @param args Arguments to the method
	 * 
	 * @return The return value from the method being invoked
	 */
	public static Object invokeMethod(Method method, Object onWhat, Object... args) {
		try {
			return method.invoke(onWhat, args);
		} catch (IllegalArgumentException e) {
			throw new SystemException(CommonErrorKeys.UNKNOWN_ERROR, e,
					"IllegalArgumentException: Could not invoke method [", method.getName(),
					"] on instance of [", onWhat.getClass().getName(), "]");
		} catch (IllegalAccessException e) {
			throw new SystemException(CommonErrorKeys.UNKNOWN_ERROR, e,
					"IllegalAccessException: Could not invoke method [", method.getName(),
					"] on instance of [", onWhat.getClass().getName(), "]");
		} catch (InvocationTargetException e) {
			throw new SystemException(CommonErrorKeys.UNKNOWN_ERROR, e,
					"InvocationTargetException: Could not invoke method [", method.getName(),
					"] on instance of [", onWhat.getClass().getName(), "]");
		}
	}

}