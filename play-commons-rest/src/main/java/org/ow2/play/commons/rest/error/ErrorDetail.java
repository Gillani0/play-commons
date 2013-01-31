/**
 *
 * Copyright (c) 2013, Linagora
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA 
 *
 */
package org.ow2.play.commons.rest.error;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author chamerling
 * 
 */
public class ErrorDetail {

	public static final String CODE_MISSING = "missing";

	public static final String CODE_MISSING_FIELD = "missing_field";

	public static final String CODE_INVALID = "invalid";

	public static final String CODE_ALREADY_EXISTS = "already_exists";

	/**
	 * Used with {@link #field} so that client can display where the problem is
	 */
	@XmlElement(name = "resource")
	public String resource;

	/**
	 * Used with {@link #resource} so that client can display where the problem
	 * is
	 */
	@XmlElement(name = "field")
	public String field;

	/**
	 * Details on what's wrong with the field. Possible values are listed
	 * above...
	 */
	@XmlElement(name = "code")
	public String code;

}
