package com.shrinfo.ibs.cmn.reflect;

/**
 * This interface should be implemented by all classes which expected their methods to be 
 * invoked through the invokeMethod() of this interface. This method has been introduced to
 * avoid usage of Java reflection APIs.
 */
public interface IMethodInvocation{
	/**
	 * This method must be implemented by the classes that implement this interface so
	 * that any method in the class that has the name as that passed in the <code>methodName</code>
	 * parameter is invoked using the passed arguments. If the target method doesn't take parameters,
	 * then the parameter <code>args</code> is ignored, otherwise a check for "null-ness" is made.
	 * 
	 * A sample implementation of this method could be:
	 * <code><pre>
	 * public Object invokeMethod( String methodName, Object... args ){
		Object returnValue = null;
		
		try{
			if( methodName.equals( "print" ) ){
				print(); //Invoke the argument-less method print(), which should be defined in the class.
			}
			else if( methodName.equals( "getString" ) ){
				returnValue = getString(); //Invoke the argument-less method getString(), which returns a value.
			}
			else if( methodName.equals( "print2" ) ){
				if( args == null || args.length < 1 ){
					throw new SystemException( "Argument excepted for print2()", "cmn.argExpected" );
				}
				print2( (String) args[ 0 ] ); //Invoke the method print2(), which takes java.lang.String.
			}
		}
		catch( ClassCastException e ){
			throw new SystemException( "Wrong arguments passed", "cmn.wrongArgTypePassed" );
		}
		
		return returnValue;
	}
	 * </pre></code>
	 * 
	 * A few rules that will need to be followed for consistent implementation are:
	 * (a) If the target method needs an argument and no argument is passed, the method should throw
	 *     a SystemException with error code "cmn.argExpected".
	 * (b) If the target method needs an argument and a wrong argument type is passed, the method 
	 *     should throw a SystemException with error code "cmn.wrongArgTypePassed".
	 * (c) If the number of arguments passed is not equal to what the target method needs, the method 
	 *     should throw a SystemException with error code "cmn.wrongNumberOfArgsPassed".
	 * (d) ClassCastException should be caught in the method itself and a SystemException thrown akin
	 *     to (b) above.
	 * (e) The class that implements this method should not use overloaded methods. It must use different
	 *     names for different methods.
	 * (f) All "invocable" methods should be private or protected, so that they cannot be invoked in a 
	 *     different manner.
	 * 
	 * @param methodName
	 * @param args
	 * @return
	 */
	public Object invokeMethod( String methodName, Object... args );
}
