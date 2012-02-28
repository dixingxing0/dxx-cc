package org.cc.core.validation;

import java.io.Serializable;

public class ArgumentException extends RuntimeException implements Serializable {

	public ArgumentException(String message) {

		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3048316340541023244L;

}
