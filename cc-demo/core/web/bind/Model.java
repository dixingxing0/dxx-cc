/**
 * Model.java 4:44:35 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package web.bind;

import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
@SuppressWarnings("serial")
public class Model extends LinkedHashMap<String, Object> {
	public static String DEFAULT_KEY = "defaultKey_";

	/**
	 * Construct a new, empty <code>Model</code>.
	 */
	public Model() {
	}

	@SuppressWarnings("unchecked")
	public Model(HttpServletRequest request) {
		Enumeration<String> it = request.getParameterNames();
		for (; it.hasMoreElements();) {
			String key = it.nextElement();
			put(key, request.getParameter(key));
		}
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
	public Model addAttribute(Object attributeValue) {
		if (attributeValue instanceof Collection
				&& ((Collection<?>) attributeValue).isEmpty()) {
			return this;
		}
		return addAttribute(DEFAULT_KEY + Object.class.getSimpleName(),
				attributeValue);
	}

	/**
	 * Copy all attributes in the supplied <code>Collection</code> into this
	 * <code>Map</code>, using attribute name generation for each element.
	 * 
	 * @see #addAttribute(Object)
	 */
	public Model addAllAttributes(Collection<?> attributeValues) {
		if (attributeValues != null) {
			for (Object attributeValue : attributeValues) {
				addAttribute(attributeValue);
			}
		}
		return this;
	}

	/**
	 * Copy all attributes in the supplied <code>Map</code> into this
	 * <code>Map</code>.
	 * 
	 * @see #addAttribute(String, Object)
	 */
	public Model addAllAttributes(Map<String, ?> attributes) {
		if (attributes != null) {
			putAll(attributes);
		}
		return this;
	}

	/**
	 * Copy all attributes in the supplied <code>Map</code> into this
	 * <code>Map</code>, with existing objects of the same name taking
	 * precedence (i.e. not getting replaced).
	 */
	public Model mergeAttributes(Map<String, ?> attributes) {
		if (attributes != null) {
			for (String key : attributes.keySet()) {
				if (!containsKey(key)) {
					put(key, attributes.get(key));
				}
			}
		}
		return this;
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
