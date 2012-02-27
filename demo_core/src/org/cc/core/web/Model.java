package org.cc.core.web;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
@SuppressWarnings("serial")
public class Model extends LinkedHashMap<String, Object> {
	public static final String DEFAULT_KEY = "defaultKey_";

	public Model() {
	}

	/**
	 * Add the supplied attribute under the supplied name.
	 * 
	 * @param attributeName
	 *            the name of the model attribute (never <code>null</code>)
	 * @param attributeValue
	 *            the model attribute value (can be <code>null</code>)
	 */
	public Model addAttribute(String attributeName, Object attributeValue) {
		put(attributeName, attributeValue);
		return this;
	}

	/**
	 * Add the supplied attribute to this <code>Map</code> using a
	 * {@link org.springframework.core.Conventions#getVariableName generated name}.
	 * <p>
	 * <emphasis>Note: Empty {@link Collection Collections} are not added to the
	 * model when using this method because we cannot correctly determine the
	 * true convention name. View code should check for <code>null</code>
	 * rather than for empty collections as is already done by JSTL tags.</emphasis>
	 * 
	 * @param attributeValue
	 *            the model attribute value (never <code>null</code>)
	 */
	@SuppressWarnings("unchecked")
	public Model addAttribute(Object attributeValue) {
		if (attributeValue instanceof Collection
				&& ((Collection<?>) attributeValue).isEmpty()) {
			return this;
		}
		return addAttribute(DEFAULT_KEY + Object.class.getSimpleName(),
				attributeValue);
	}

	/**
	 * Does this model contain an attribute of the given name?
	 * 
	 * @param attributeName
	 *            the name of the model attribute (never <code>null</code>)
	 * @return whether this model contains a corresponding attribute
	 */
	public boolean containsAttribute(String attributeName) {
		return containsKey(attributeName);
	}
}
