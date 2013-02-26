package org.ow2.play.commons.accesscontrol.api;

import static eu.play_project.play_commons.constants.Namespace.GROUP;

public enum Group {

	/*
	 * PLAY pre-defines groups:
	 */
	Public(GROUP.getUri() + "public"),
	Developers(GROUP.getUri() + "developers");

	private final String name;

	Group(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
