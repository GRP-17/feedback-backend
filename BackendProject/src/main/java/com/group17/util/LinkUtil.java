package com.group17.util;

import org.springframework.hateoas.Link;

public class LinkUtil {
	
	private LinkUtil() {}
	
	public static Link removeParameters(Link link) {
		String ref = link.getHref();
		if(ref.contains("{?")) {
			return new Link(ref.substring(0, ref.indexOf('{')), link.getRel());
		} else if(ref.contains("{/")) {
			return new Link(ref.substring(0, ref.indexOf('{')), link.getRel());	
		} else {
			if(ref.contains("?")) {
				return new Link(ref.substring(0, ref.indexOf('?')), link.getRel());
			} else {
				return link;
			}
		}
	}

}
