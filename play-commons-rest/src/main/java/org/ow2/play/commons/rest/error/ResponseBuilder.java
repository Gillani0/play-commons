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

/**
 * @author chamerling
 * 
 */
public class ResponseBuilder {

	protected Error error;

	private ResponseBuilder() {
	}

	public static final ResponseBuilder error(Exception e) {
		ResponseBuilder builder = new ResponseBuilder();
		Error error = new Error();
		error.message = e.getMessage();
		builder.error = error;
		return builder;
	}

	public static final ResponseBuilder error(String pattern, Object... args) {
		ResponseBuilder builder = new ResponseBuilder();
		Error error = new Error();
		error.message = String.format(pattern, args);
		builder.error = error;
		return builder;
	}

	public static final ResponseBuilder error(String message) {
		ResponseBuilder builder = new ResponseBuilder();
		Error error = new Error();
		error.message = message;
		builder.error = error;
		return builder;
	}

	public final ResponseBuilder details(String resource, String field,
			String code) {
		ErrorDetail detail = new ErrorDetail();
		detail.resource = resource;
		detail.field = field;
		detail.code = code;
		error.errors.add(detail);
		return this;
	}

	public final javax.ws.rs.core.Response build() {
		return Response.error(this.error);
	}

}
